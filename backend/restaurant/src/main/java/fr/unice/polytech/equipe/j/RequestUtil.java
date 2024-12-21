package fr.unice.polytech.equipe.j;

import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

public class RequestUtil {
    public static final String DATABASE_RESTAURANT_SERVICE_URI = "http://localhost:5002/api/database/restaurants";
    public static final String DATABASE_CAMPUS_USER_SERVICE_URI = "http://localhost:5002/api/database/campusUsers";
    public static final String DATABASE_MANAGER_SERVICE_URI = "http://localhost:5002/api/database/managers";
    public static final String DATABASE_ORDER_SERVICE_URI = "http://localhost:5002/api/database/orders";
    public static final String DATABASE_GROUPORDER_SERVICE_URI = "http://localhost:5002/api/database/groupOrders";

    public static java.net.http.HttpResponse<String> request(
            String basePath, String uri, String queryPath, HttpMethod method, String jsonBody) {
        try {
            // If ? inside uri, it will be encoded as %3F, so we need to encode it again
            String encodedUri = URLEncoder.encode(uri, StandardCharsets.UTF_8);
            String fullUri = basePath + encodedUri + (queryPath != null ? queryPath : "");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request;

            switch (method) {
                case HttpMethod.GET:
                    request = HttpRequest.newBuilder()
                            .uri(URI.create(fullUri))
                            .header("Content-Type", "application/json")
                            .GET()
                            .build();
                    break;
                case HttpMethod.POST:
                    request = HttpRequest.newBuilder()
                            .uri(URI.create(fullUri))
                            .header("Content-Type", "application/json")
                            .POST(jsonBody == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();
                    break;
                case HttpMethod.PUT:
                    request = HttpRequest.newBuilder()
                            .uri(URI.create(fullUri))
                            .header("Content-Type", "application/json")
                            .PUT(jsonBody == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();
                    break;
                case HttpMethod.DELETE:
                    request = HttpRequest.newBuilder()
                            .uri(URI.create(fullUri))
                            .header("Content-Type", "application/json")
                            .DELETE()
                            .build();
                    break;
                default:
                    return null;
            }

            return client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace(); // For better debugging
            return null;
        }
    }

    /**
     * Creates a standardized HttpResponse with the given HTTP status code and message.
     *
     * @param statusCode The HTTP status code to include in the response.
     * @param message The message to include in the response body.
     * @return An HttpResponse with the specified status code and message.
     */
    public static HttpResponse createHttpResponse(HttpCode statusCode, String message) {
        return new HttpResponse(statusCode, message);
    }
}
