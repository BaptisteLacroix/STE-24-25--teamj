package fr.unice.polytech.equipe.j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.QueryParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A flexible REST server that can be customized to handle HTTP requests
 * and map them to annotated controller methods.
 */
public class FlexibleRestServer {

    private HttpServer server;
    private final int port;
    private final String serverRoot;
    private String classpath;  // Add classpath to specify where to scan
    private final Map<Class<?>, Object> controllerInstances = new HashMap<>();
    private final Map<String, Map<HttpMethod, HttpHandler>> contextHandlers = new HashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor to initialize the server with a root package and port.
     * @param serverRoot The root package to scan for controller classes.
     * @param port The port on which the server will run.
     */
    public FlexibleRestServer(String serverRoot, String classpath, int port) {
        this(serverRoot, port);
        this.classpath = classpath;  // Store classpath
    }

    public FlexibleRestServer(String serverRoot, int port) {
        this.port = port;
        this.serverRoot = serverRoot;
        objectMapper.registerModule(new JavaTimeModule());
    }


    /**
     * Starts the HTTP server, scans for controller classes, and initializes handlers.
     */
    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException("Could not create server on port: " + port, e);
        }

        List<Class<?>> controllerClasses = scanControllerClasses();
        initializeControllerHandlers(controllerClasses);

        server.createContext("/", this::handleRequest);
        server.setExecutor(null); // Use the default executor
        System.out.println("Server running on port " + port);
        server.start();
    }

    /**
     * Scans for controller classes in the specified root package.
     * @return A list of controller classes.
     */
    private List<Class<?>> scanControllerClasses() {
        try {
            return ClassScanner.findClassesInPackage(serverRoot, classpath);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not scan classes in package " + serverRoot, e);
        }
    }

    /**
     * Initializes the handlers for all controller methods that are annotated with @Route.
     * @param controllerClasses The list of controller classes to process.
     */
    private void initializeControllerHandlers(List<Class<?>> controllerClasses) {
        for (Class<?> controllerClass : controllerClasses) {
            if (controllerClass.isAnnotationPresent(Controller.class)) {
                String basePath = controllerClass.getAnnotation(Controller.class).value();
                for (Method method : controllerClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Route.class)) {
                        initializeRouteHandler(controllerClass, method, basePath);
                    }
                }
            }
        }
    }

    /**
     * Initializes a handler for a specific route defined by the method's @Route annotation.
     * @param controllerClass The controller class containing the method.
     * @param method The controller method.
     * @param basePath The base path for the controller.
     */
    private void initializeRouteHandler(Class<?> controllerClass, Method method, String basePath) {
        Route route = method.getAnnotation(Route.class);
        String fullPath = normalizePath(basePath + route.value());
        HttpMethod httpMethod = route.method();
        List<String> dynamicSegments = extractDynamicSegments(route.value());

        addContextHandler(controllerClass, method, fullPath, httpMethod, dynamicSegments);
    }

    /**
     * Extracts dynamic segments from a route pattern, such as {id}.
     * @param routeValue The route pattern string.
     * @return A list of dynamic segment names.
     */
    private List<String> extractDynamicSegments(String routeValue) {
        String[] routeSegments = routeValue.split("/");
        List<String> dynamicSegments = new ArrayList<>();
        for (String segment : routeSegments) {
            if (segment.startsWith("{") && segment.endsWith("}")) {
                dynamicSegments.add(segment.substring(1, segment.length() - 1));
            }
        }
        return dynamicSegments;
    }

    /**
     * Handles incoming HTTP requests by matching the request path to registered routes.
     * @param exchange The HTTP exchange containing the request.
     * @throws IOException If an I/O error occurs while handling the request.
     */
    private void handleRequest(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();
        String matchedPattern = findMatchingRoute(requestPath);

        if (matchedPattern != null) {
            HttpMethod requestMethod = HttpMethod.valueOf(exchange.getRequestMethod());
            HttpHandler handler = contextHandlers.get(matchedPattern).get(requestMethod);

            if (handler != null) {
                handler.handle(exchange);
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
    }

    /**
     * Finds the first route that matches the request path.
     * @param requestPath The requested path.
     * @return The matching route pattern, or null if no match is found.
     */
    private String findMatchingRoute(String requestPath) {
        return contextHandlers.keySet().stream()
                .sorted(this::compareRoutePatterns)
                .filter(requestPath::matches)
                .findFirst()
                .orElse(null);
    }

    /**
     * Normalizes a path by converting dynamic segments into regular expressions.
     * @param path The path to normalize.
     * @return The normalized path with dynamic segments as regex patterns.
     */
    String normalizePath(String path) {
        return path.replaceAll("\\{[^/]+}", "([^/]+)").replaceAll("/+", "/");
    }

    /**
     * Compares two route patterns to determine which should take precedence.
     * @param route1 The first route pattern.
     * @param route2 The second route pattern.
     * @return A comparison result for sorting.
     */
    private int compareRoutePatterns(String route1, String route2) {
        boolean isDynamic1 = route1.contains("([^/]+)");
        boolean isDynamic2 = route2.contains("([^/]+)");

        if (isDynamic1 && !isDynamic2) return 1;
        if (!isDynamic1 && isDynamic2) return -1;
        return route1.compareTo(route2);
    }

    /**
     * Registers a handler for a specific HTTP method and route.
     * @param controllerClass The controller class containing the method.
     * @param method The controller method.
     * @param fullPath The full path for the route.
     * @param httpMethod The HTTP method for the route.
     * @param dynamicSegments The dynamic segments in the route.
     */
    private void addContextHandler(Class<?> controllerClass, Method method, String fullPath, HttpMethod httpMethod, List<String> dynamicSegments) {
        contextHandlers.computeIfAbsent(fullPath, k -> new HashMap<>()).put(httpMethod, exchange -> {
            try {
                Object controller = getControllerInstance(controllerClass);
                String requestPath = exchange.getRequestURI().getPath();
                Map<String, String> pathParams = extractPathParams(requestPath, fullPath, dynamicSegments);
                Map<String, String> queryParams = parseQueryParams(exchange);
                String requestBody = readRequestBody(exchange);

                Object[] methodParams = prepareMethodParameters(method, pathParams, queryParams, requestBody);
                Object result = method.invoke(controller, methodParams);

                handleResponse(exchange, result);
            } catch (Exception e) {
                handleException(exchange, e);
            }
        });
    }

    /**
     * Retrieves or creates an instance of a controller class.
     * @param controllerClass The controller class.
     * @return An instance of the controller.
     */
    Object getControllerInstance(Class<?> controllerClass) {
        return controllerInstances.computeIfAbsent(controllerClass, k -> createControllerInstance(controllerClass));
    }

    /**
     * Creates an instance of the specified controller class using reflection.
     * @param controllerClass The controller class to instantiate.
     * @return An instance of the controller class.
     */
    private Object createControllerInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create controller instance", e);
        }
    }

    /**
     * Reads the request body from an HTTP exchange.
     * @param exchange The HTTP exchange containing the request body.
     * @return The request body as a string.
     * @throws IOException If an I/O error occurs while reading the body.
     */
    String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Prepares the method parameters for invoking a controller method.
     * @param method The method to be invoked.
     * @param pathParams The extracted path parameters.
     * @param queryParams The extracted query parameters.
     * @param requestBody The request body.
     * @return An array of method parameters to pass when invoking the method.
     * @throws IOException If an error occurs while reading or parsing the request.
     */
    Object[] prepareMethodParameters(Method method, Map<String, String> pathParams,
                                     Map<String, String> queryParams, String requestBody) throws IOException {
        Object[] methodParams = new Object[method.getParameterCount()];
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            methodParams[i] = resolveParameter(param, pathParams, queryParams, requestBody);
        }
        return methodParams;
    }

    /**
     * Resolves the value of a method parameter, depending on annotations like @QueryParam, @PathParam, or @BeanParam.
     * @param param The method parameter.
     * @param pathParams The path parameters.
     * @param queryParams The query parameters.
     * @param requestBody The request body (used for BeanParam).
     * @return The resolved parameter value.
     * @throws IOException If an error occurs while parsing the request body for BeanParam.
     */
    private Object resolveParameter(Parameter param, Map<String, String> pathParams,
                                    Map<String, String> queryParams, String requestBody) throws IOException {
        if (param.isAnnotationPresent(QueryParam.class)) {
            return convertType(queryParams.get(param.getAnnotation(QueryParam.class).value()), param.getType());
        } else if (param.isAnnotationPresent(PathParam.class)) {
            return convertType(pathParams.get(param.getAnnotation(PathParam.class).value()), param.getType());
        } else if (param.isAnnotationPresent(BeanParam.class)) {
            return parseRequestBody(param.getType(), requestBody);
        }
        return null;
    }

    /**
     * Parses the request body into an object of the specified type.
     * @param paramType The type to which the request body should be converted.
     * @param requestBody The request body as a string.
     * @return The parsed object.
     * @throws IOException If an error occurs while parsing the request body.
     */
    private Object parseRequestBody(Class<?> paramType, String requestBody) throws IOException {
        if (requestBody.isBlank()) return null;
        if (paramType == String.class) {
            return requestBody;
        }
        return objectMapper.readValue(requestBody, paramType);
    }

    /**
     * Handles the response after processing a controller method's result.
     * @param exchange The HTTP exchange.
     * @param result The result object from the controller method.
     * @throws IOException If an error occurs while sending the response.
     */
    void handleResponse(HttpExchange exchange, Object result) throws IOException {
        if (result instanceof HttpResponse<?>) {
            HttpResponse<?> response = (HttpResponse<?>) result;
            String jsonResponse = convertResponseContentToJson(response.getContent());
            exchange.sendResponseHeaders(response.getCode(), jsonResponse.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    /**
     * Converts the response content to JSON format.
     * @param content The content to be converted.
     * @return The JSON representation of the content.
     * @throws IOException If an error occurs while converting to JSON.
     */
    private String convertResponseContentToJson(Object content) throws IOException {
        return content instanceof String ? (String) content : objectMapper.writeValueAsString(content);
    }

    /**
     * Handles exceptions that occur during request processing by sending an internal server error response.
     * @param exchange The HTTP exchange.
     * @param e The exception that was thrown.
     */
    private void handleException(HttpExchange exchange, Exception e) {
        try {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1); // Internal Server Error
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Extracts path parameters from the request path.
     * @param requestPath The request path.
     * @param routePattern The route pattern.
     * @param dynamicSegments The dynamic segments in the route.
     * @return A map of extracted path parameters.
     */
    Map<String, String> extractPathParams(String requestPath, String routePattern, List<String> dynamicSegments) {
        Pattern pattern = Pattern.compile(routePattern);
        Matcher matcher = pattern.matcher(requestPath);

        if (!matcher.matches()) return Collections.emptyMap();

        Map<String, String> params = new HashMap<>();
        for (int i = 1; i <= matcher.groupCount(); i++) {
            params.put(dynamicSegments.get(i - 1), matcher.group(i));
        }
        return params;
    }

    /**
     * Parses query parameters from the request URI.
     * @param exchange The HTTP exchange.
     * @return A map of query parameters.
     */
    private Map<String, String> parseQueryParams(HttpExchange exchange) {
        return Optional.ofNullable(exchange.getRequestURI().getQuery())
                .map(this::getQueryToMap)
                .orElse(Collections.emptyMap());
    }

    /**
     * Converts a query string to a map of key-value pairs.
     * @param query The query string to be converted.
     * @return A map of query parameters.
     */
    Map<String, String> getQueryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            result.put(entry[0], entry.length > 1 ? entry[1] : "");
        }
        return result;
    }

    /**
     * Converts a string value to the specified type.
     * @param value The value to convert.
     * @param type The target type to convert to.
     * @return The converted value.
     */
    Object convertType(String value, Class<?> type) {
        if (value == null) return null;
        if (type.equals(String.class)) return value;
        if (type.equals(Integer.class) || type.equals(int.class)) return Integer.parseInt(value);
        if (type.equals(Long.class) || type.equals(long.class)) return Long.parseLong(value);
        if (type.equals(UUID.class)) return UUID.fromString(value);
        if (type.equals(Double.class) || type.equals(double.class)) return Double.parseDouble(value);
        if (type.equals(Boolean.class) || type.equals(boolean.class)) return Boolean.parseBoolean(value);
        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }

    /**
     * Sends a response with the specified status code and message.
     * @param exchange The HTTP exchange.
     * @param statusCode The status code to send.
     * @param message The message to include in the response.
     */
    private void sendResponse(HttpExchange exchange, int statusCode, String message) {
        try {
            exchange.sendResponseHeaders(statusCode, message.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(message.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method to start the server.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new FlexibleRestServer("fr.unice.polytech.equipe.j", 5003).start();
    }
}
