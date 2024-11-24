package fr.unice.polytech.equipe.j;

import com.sun.net.httpserver.HttpExchange;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FlexibleRestServerTest {
    private FlexibleRestServer server;

    @BeforeEach
    void setUp() {
        server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 8080);
    }

    @Test
    void testNormalizePath() {
        String path = "/api/{id}/details";
        String expected = "/api/([^/]+)/details";
        assertEquals(expected, server.normalizePath(path));
    }

    @Test
    void testReadRequestBody() throws IOException {
        HttpExchange exchange = mock(HttpExchange.class);
        InputStream inputStream = new ByteArrayInputStream("test-body".getBytes());
        when(exchange.getRequestBody()).thenReturn(inputStream);

        String body = server.readRequestBody(exchange);
        assertEquals("test-body", body);
    }

    @Test
    void testHandleResponse_withHttpResponse() throws IOException {
        HttpExchange exchange = mock(HttpExchange.class);
        OutputStream outputStream = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(outputStream);

        HttpResponse<String> response = new HttpResponse<>(HttpCode.HTTP_200, "Hello World");
        server.handleResponse(exchange, response);

        verify(exchange).sendResponseHeaders(200, "Hello World".getBytes().length);
        assertEquals("Hello World", outputStream.toString());
    }

    @Test
    void testConvertType() {
        assertEquals(123, server.convertType("123", Integer.class));
        assertEquals(true, server.convertType("true", Boolean.class));
        assertEquals("test", server.convertType("test", String.class));
        assertThrows(IllegalArgumentException.class, () -> server.convertType("invalid", Void.class));
    }

    @Test
    void testGetQueryToMap() {
        String query = "key1=value1&key2=value2";
        Map<String, String> result = server.getQueryToMap(query);

        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    void testFullRequestFlow() throws IOException {
        // Start server in a separate thread
        new Thread(() -> server.start()).start();

        // Simulate HTTP Request
        URL url = new URL("http://localhost:8080/api/test");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(404, responseCode); // Expecting 404 as no routes defined for "/api/test"
    }

    @Test
    void testExtractPathParams() {
        // Test with a matching path and route pattern
        String requestPath = "/api/123/details";
        String routePattern = "/api/([^/]+)/details";
        List<String> annotatedPathParams = List.of("id");
        Map<String, String> params = server.extractPathParams(requestPath, routePattern, annotatedPathParams);

        assertNotNull(params);
        assertEquals(1, params.size());
        assertEquals("123", params.get("id"));

        // Test with a non-matching path
        requestPath = "/api/123";
        params = server.extractPathParams(requestPath, routePattern, annotatedPathParams);

        assertTrue(params.isEmpty());
    }

    @Test
    void testExtractPathParams2() {
        // Test with a matching path and route pattern
        String requestPath = "/api/database/restaurants/00873fca-cb38-4f44-9987-649e7b629c87";
        String routePattern = "/api/database/restaurants/([^/]+)";
        List<String> annotatedPathParams = List.of("id");
        Map<String, String> params = server.extractPathParams(requestPath, routePattern, annotatedPathParams);

        assertNotNull(params);
        assertEquals(1, params.size());
        assertEquals("00873fca-cb38-4f44-9987-649e7b629c87", params.get("id"));

        // Test with a non-matching path
        requestPath = "/api/00873fca-cb38-4f44-9987-649e7b629c87";
        params = server.extractPathParams(requestPath, routePattern, annotatedPathParams);

        assertTrue(params.isEmpty());
    }

    @Test
    void testPrepareMethodParameters_withMissingParams() throws Exception {
        Method mockMethod = MockController.class.getDeclaredMethod("mockControllerMethod", String.class, Integer.class, String.class);
        Map<String, String> pathParams = Map.of();
        Map<String, String> queryParams = Map.of();
        String requestBody = "";

        Object[] methodParams = server.prepareMethodParameters(mockMethod, pathParams, queryParams, requestBody);

        // Check that missing parameters result in null
        assertNull(methodParams[0]);  // Path param
        assertNull(methodParams[1]);  // Query param
        assertNull(methodParams[2]);  // BeanParam
    }

    @Test
    void testGetControllerInstance() {
        // Test for controller instantiation
        Object controller = server.getControllerInstance(MockController.class);

        assertNotNull(controller);
        assertTrue(controller instanceof MockController);

        // Test caching behavior
        Object sameController = server.getControllerInstance(MockController.class);
        assertSame(controller, sameController);
    }

    // Mock controller class for testing purposes
    static class MockController {
        public void mockControllerMethod(String pathParam, Integer queryParam, String beanParam) {
            // Mocked method
        }
    }
}
