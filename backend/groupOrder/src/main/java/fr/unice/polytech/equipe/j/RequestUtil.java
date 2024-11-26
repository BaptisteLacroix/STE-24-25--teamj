package java.fr.unice.polytech.equipe.j;

import fr.unice.polytech.equipe.j.HttpMethod;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class RequestUtil {
    public static java.net.http.HttpResponse<String> request(String uri, HttpMethod method, String jsonBody) {
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request;
            switch (method) {
                case HttpMethod.GET:
                    request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:5003/api/" + uri))
                            .header("Content-Type", "application/json")
                            .GET()
                            .build();
                    break;
                case HttpMethod.POST:
                    request = HttpRequest.newBuilder()
                            .uri(URI.create(uri))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();
                    break;
                case HttpMethod.PUT:
                    request = HttpRequest.newBuilder()
                            .uri(URI.create(uri))
                            .header("Content-Type", "application/json")
                            .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();
                    break;
                case HttpMethod.DELETE:
                    request = HttpRequest.newBuilder()
                            .uri(URI.create(uri))
                            .header("Content-Type", "application/json")
                            .DELETE()
                            .build();
                    break;
                default:
                    return null;
            }
            return client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return null;
        }
    }
}
