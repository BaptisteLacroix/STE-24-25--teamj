package fr.unice.polytech.equipe.j;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.security.jgss.GSSUtil;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.QueryParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
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

        // Generate OpenAPI Documentation
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String fileName = classLoader.getResource(serverRoot.replace('.', '/') + "/").getPath();
            // split by "backend" string and get last element
            fileName = fileName.split("backend")[1].split("target")[0].replace("/", "");

            ObjectNode openApiSpec = generateOpenApiSpec(controllerClasses);
            File outputFile = new File("doc/openapi/" + fileName+ "OpenApi.json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, openApiSpec);
            System.out.println("OpenAPI specification generated: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate OpenAPI documentation", e);
        }
        server.createContext("/", this::handleRequest);
        server.setExecutor(null); // Use the default executor
        System.out.println("Server running on port " + port);
        server.start();
    }

    private ObjectNode generateOpenApiSpec(List<Class<?>> controllerClasses) throws Exception {
        ObjectNode openApi = objectMapper.createObjectNode();
        openApi.put("openapi", "3.0.1");
        openApi.set("info", createInfo());
        // Server url: localhost:port
        ArrayNode servers = objectMapper.createArrayNode();
        ObjectNode serverNode = objectMapper.createObjectNode();
        serverNode.put("url", "http://localhost:" + port);
        servers.add(serverNode);
        openApi.set("servers", servers);
        openApi.set("paths", createPaths(controllerClasses));
        openApi.set("components", createComponents(controllerClasses));
        return openApi;
    }

    private ObjectNode createInfo() {
        ObjectNode info = objectMapper.createObjectNode();
        info.put("title", "Generated API");
        info.put("version", "1.0.0");
        return info;
    }

    private ObjectNode createPaths(List<Class<?>> controllerClasses) {
        ObjectNode paths = objectMapper.createObjectNode();

        for (Class<?> controllerClass : controllerClasses) {
            if (controllerClass.isAnnotationPresent(Controller.class)) {
                Controller controller = controllerClass.getAnnotation(Controller.class);
                String basePath = controller.value();

                for (Method method : controllerClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Route.class)) {
                        Route route = method.getAnnotation(Route.class);
                        String fullPath = basePath + route.value();

                        ObjectNode methodNode = objectMapper.createObjectNode();
                        methodNode.put("summary", method.getName());
                        methodNode.put("description", "Generated from method " + method.getName());

                        // Generate parameters
                        ArrayNode parameters = objectMapper.createArrayNode();
                        for (Parameter param : method.getParameters()) {
                            parameters.add(generateParameter(param));
                        }
                        methodNode.set("parameters", parameters);

                        // Generate response
                        ObjectNode responses = objectMapper.createObjectNode();
                        responses.set("200", createResponse("Successful response", method.getReturnType()));
                        methodNode.set("responses", responses);

                        // Add to paths
                        ObjectNode pathNode = paths.has(fullPath)
                                ? (ObjectNode) paths.get(fullPath)
                                : objectMapper.createObjectNode();
                        pathNode.set(route.method().name().toLowerCase(), methodNode);
                        paths.set(fullPath, pathNode);
                    }
                }
            }
        }

        return paths;
    }

    private ObjectNode createResponse(String description, Class<?> responseType) {
        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("description", description);

        ObjectNode content = objectMapper.createObjectNode();
        ObjectNode mediaType = objectMapper.createObjectNode();

        if (responseType != null) {
            if (isPrimitiveOrWrapper(responseType)) {
                ObjectNode schema = objectMapper.createObjectNode();
                schema.put("type", mapJavaTypeToOpenApiType(responseType));
                mediaType.set("schema", schema);
            } else {
                mediaType.set("schema", createSchemaReference(responseType));
            }
        }

        content.set("application/json", mediaType);
        responseNode.set("content", content);

        return responseNode;
    }

    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() || type == Integer.class || type == Long.class ||
                type == Double.class || type == Boolean.class || type == String.class;
    }

    private ObjectNode generateParameter(Parameter param) {
        ObjectNode paramNode = objectMapper.createObjectNode();

        if (param.isAnnotationPresent(PathParam.class)) {
            PathParam pathParam = param.getAnnotation(PathParam.class);
            paramNode.put("name", pathParam.value());
            paramNode.put("in", "path");
            paramNode.put("required", true);
            paramNode.set("schema", generateSchemaForType(param.getType()));
        } else if (param.isAnnotationPresent(QueryParam.class)) {
            QueryParam queryParam = param.getAnnotation(QueryParam.class);
            paramNode.put("name", queryParam.value());
            paramNode.put("in", "query");
            paramNode.put("required", false);
            paramNode.set("schema", generateSchemaForType(param.getType()));
        } else if (param.isAnnotationPresent(BeanParam.class)) {
            // BeanParam: Inline schema or reference to the component
            paramNode.put("name", param.getType().getSimpleName());
            paramNode.put("in", "body");
            paramNode.set("schema", createSchemaReference(param.getType()));
        }

        return paramNode;
    }

    private ObjectNode createSchemaReference(Class<?> type) {
        ObjectNode schemaRef = objectMapper.createObjectNode();
        schemaRef.put("$ref", "#/components/schemas/" + type.getSimpleName());
        return schemaRef;
    }

    private ObjectNode createComponents(List<Class<?>> controllerClasses) {
        ObjectNode components = objectMapper.createObjectNode();
        ObjectNode schemas = objectMapper.createObjectNode();

        schemas.set("HttpResponse", generateHttpResponseSchema());

        // Scan for BeanParam types and DTOs
        for (Class<?> controllerClass : controllerClasses) {
            for (Method method : controllerClass.getDeclaredMethods()) {
                for (Parameter param : method.getParameters()) {
                    if (param.isAnnotationPresent(BeanParam.class)) {
                        schemas.set(param.getType().getSimpleName(), generateSchemaForType(param.getType()));
                    }
                }
            }
        }
        components.set("schemas", schemas);
        return components;
    }

    private ObjectNode generateHttpResponseSchema() {
        // Create a schema for HttpResponse
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");

        // Define the properties of HttpResponse, you can modify this based on your actual HttpResponse class
        ObjectNode properties = objectMapper.createObjectNode();

        ObjectNode statusCodeProperty = objectMapper.createObjectNode();
        statusCodeProperty.put("type", "integer");
        properties.set("statusCode", statusCodeProperty);

        ObjectNode bodyProperty = objectMapper.createObjectNode();
        bodyProperty.put("type", "string");  // Assuming HttpResponse has a body of type String
        properties.set("body", bodyProperty);

        schema.set("properties", properties);
        return schema;
    }

    private ObjectNode generateSchemaForType(Class<?> type) {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode properties = objectMapper.createObjectNode();

        for (Field field : type.getDeclaredFields()) {
            ObjectNode property = objectMapper.createObjectNode();
            property.put("type", mapJavaTypeToOpenApiType(field.getType()));
            properties.set(field.getName(), property);
        }

        schema.set("properties", properties);
        return schema;
    }

    private String mapJavaTypeToOpenApiType(Class<?> javaType) {
        if (javaType.equals(String.class)) {
            return "string";
        } else if (javaType.equals(Integer.class) || javaType.equals(int.class)) {
            return "integer";
        } else if (javaType.equals(Long.class) || javaType.equals(long.class)) {
            return "integer";
        } else if (javaType.equals(Double.class) || javaType.equals(double.class) ||
                javaType.equals(Float.class) || javaType.equals(float.class)) {
            return "number";
        } else if (javaType.equals(Boolean.class) || javaType.equals(boolean.class)) {
            return "boolean";
        } else if (javaType.equals(UUID.class)) {
            return "string";
        } else if (javaType.equals(java.time.LocalDateTime.class) || javaType.equals(java.util.Date.class)) {
            return "string";
        } else if (javaType.equals(java.time.LocalDate.class)) {
            return "string";
        } else if (javaType.equals(List.class)) {
            return "array"; // You'll need more details about the list's item type for a complete schema
        }
        return "object"; // Default for complex or unknown types
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
