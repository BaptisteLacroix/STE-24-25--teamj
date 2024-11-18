package fr.unice.polytech.equipe.j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.QueryParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.httpresponse.ResponseUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlexibleRestServer {
    private HttpServer server;
    private int port;
    private String serverRoot;
    private Map<Class<?>, Object> controllerInstances = new HashMap<>();
    private Map<String, Map<HttpMethod, HttpHandler>> contextHandlers = new HashMap<>();

    public FlexibleRestServer(String serverRoot, int port) {
        this.port = port;
        this.serverRoot = serverRoot;
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException("could not create server on port : " + port);
        }
        List<Class<?>> controllerClasses;
        try {
            controllerClasses = ClassScanner.findClassesInPackage(serverRoot);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("could not scan classes in package " + this.serverRoot);
        }
        for (Class<?> controllerClass : controllerClasses) {
            if (!controllerClass.isAnnotationPresent(Controller.class)) continue;
            Controller controllerAnnotation = controllerClass.getAnnotation(Controller.class);
            String basePath = controllerAnnotation.value();
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(Route.class)) continue;

                Route routeAnnotation = method.getAnnotation(Route.class);
                String fullPath = basePath + routeAnnotation.value();
                HttpMethod httpMethod = routeAnnotation.method();

                addContextHandler(controllerClass, method, fullPath, httpMethod);
            }
        }

        for (Map.Entry<String, Map<HttpMethod, HttpHandler>> entry : contextHandlers.entrySet()) {
            server.createContext(entry.getKey(), (exchange) -> {
                HttpMethod requestMethod = HttpMethod.valueOf(exchange.getRequestMethod());
                HttpHandler handler = entry.getValue().get(requestMethod);
                if (handler != null) {
                    handler.handle(exchange);
                } else {
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                }
            });
        }

        server.setExecutor(null);
        System.out.println("Server running on port " + port);
        server.start();
    }

    private void addContextHandler(Class<?> controllerClass, Method method, String fullPath, HttpMethod httpMethod) {
        contextHandlers.computeIfAbsent(fullPath, k -> new HashMap<>()).put(httpMethod, (exchange) -> {
            // Extract controller instance
            Object controller = this.controllerInstances.computeIfAbsent(controllerClass, k -> {
                try {
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create controller instance", e);
                }
            });

            // Parse URI and extract path/query parameters
            String requestPath = exchange.getRequestURI().getPath();
            Map<String, String> pathParams = extractPathParams(requestPath, fullPath);
            Map<String, String> queryParams = this.getQueryToMap(exchange.getRequestURI().getQuery());

            // Read and parse request body
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            // Prepare method arguments
            Object[] methodParams = new Object[method.getParameterCount()];
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                Class<?> paramType = param.getType();

                // Process annotations
                if (param.isAnnotationPresent(QueryParam.class)) {
                    String paramName = param.getAnnotation(QueryParam.class).value();
                    methodParams[i] = convertType(queryParams.get(paramName), paramType);
                } else if (param.isAnnotationPresent(PathParam.class)) {
                    String paramName = param.getAnnotation(PathParam.class).value();
                    methodParams[i] = convertType(pathParams.get(paramName), paramType);
                } else if (param.isAnnotationPresent(BeanParam.class)) {
                    if (!requestBody.isBlank()) {
                        methodParams[i] = new ObjectMapper().readValue(requestBody, paramType);
                    }
                } else {
                    methodParams[i] = null; // Default to null for unannotated parameters
                }
            }

            // Invoke the method and return the response
            try {
                Object result = method.invoke(controller, methodParams);

                if (result instanceof HttpResponse<?> response) {
                    String jsonResponse = new ObjectMapper().writeValueAsString(response.getContent());
                    exchange.sendResponseHeaders(response.getCode(), jsonResponse.getBytes(StandardCharsets.UTF_8).length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
                    }
                }
            } catch (Exception e) {
                exchange.sendResponseHeaders(500, -1);
                e.printStackTrace();
            }
        });
    }

    private Map<String, String> extractPathParams(String path, String routePattern) {
        String regex = routePattern.replaceAll("\\{([^/}]+)}", "(?<$1>[^/]+)");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);

        Map<String, String> params = new HashMap<>();
        if (matcher.matches()) {
            Pattern namedGroupPattern = Pattern.compile("\\{([^/}]+)}");
            Matcher namedGroupMatcher = namedGroupPattern.matcher(routePattern);
            while (namedGroupMatcher.find()) {
                String groupName = namedGroupMatcher.group(1);
                String value = matcher.group(groupName);
                if (value != null) params.put(groupName, value);
            }
        }
        return params;
    }

    private Object convertType(String value, Class<?> type) {
        if (value == null) return null;

        if (type.equals(String.class)) {
            return value;
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(UUID.class)) {
            return UUID.fromString(value);
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        }
        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }



    public Map<String, String> getQueryToMap(String query) {
        if (query == null) {
            return new HashMap<>();
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public void stop() {
        server.stop(0);
    }

    public static void main(String[] args) {
        new FlexibleRestServer("fr.unice.polytech.equipe.j", 5003).start();
    }
}
