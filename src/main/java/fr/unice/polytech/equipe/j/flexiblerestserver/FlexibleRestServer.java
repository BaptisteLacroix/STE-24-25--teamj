package fr.unice.polytech.equipe.j.flexiblerestserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

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

public class FlexibleRestServer {
    private int port;
    private String serverRoot;
    private Map<Class<?>, Object> controllerInstances = new HashMap<>();
    private Map<String, Map<HttpMethod, HttpHandler>> contextHandlers = new HashMap<>();

    public FlexibleRestServer(String serverRoot, int port) {
        this.port = port;
        this.serverRoot = serverRoot;
    }

    public void start() {
        HttpServer server;
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
            // get controller instance (put it in the controller map if non-existent)
            Object controller = this.controllerInstances.compute(controllerClass, (k, v) -> {
                if (v == null) {
                    try {
                        Object newInstance = controllerClass.getDeclaredConstructor().newInstance();
                        this.controllerInstances.put(controllerClass, newInstance);
                        return newInstance;
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                             InvocationTargetException e) {
                        try {
                            exchange.sendResponseHeaders(500, -1);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                return this.controllerInstances.get(controllerClass);
            });

            // get request parameters
            Map<String, String> linkParameters = this.getQueryToMap(exchange.getRequestURI().getQuery());
            Map<String, String> bodyParameters = this.getQueryToMap(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            List<Parameter> parameters = List.of(method.getParameters());

            Object[] methodParams = new Object[parameters.size()];
            for (int i = 0; i < parameters.size(); i++) {
                Parameter param = parameters.get(i);
                // or handle default values
                if (linkParameters.containsKey(param.getName())) {
                    methodParams[i] = linkParameters.get(param.getName());
                } else {
                    methodParams[i] = bodyParameters.getOrDefault(param.getName(), null);
                }
            }

            // invoke route handler and get its result
            Object result;
            try {
                result = method.invoke(controller, methodParams);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            // serialize handler result and send it as a json response
            String jsonResponse = new ObjectMapper().writeValueAsString(result);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.length());

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsonResponse.getBytes());
            }
        });
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

    public static void main(String[] args) {
        new FlexibleRestServer("fr.unice.polytech.equipe.j", 5003).start();
    }
}