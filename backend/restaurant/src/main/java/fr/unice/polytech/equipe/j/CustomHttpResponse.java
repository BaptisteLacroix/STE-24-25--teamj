package fr.unice.polytech.equipe.j;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.List;
import java.util.Optional;

public class CustomHttpResponse implements HttpResponse<String> {

    private final int statusCode;
    private final String body;
    private final HttpHeaders headers;

    // Constructor
    public CustomHttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = HttpHeaders.of(Map.of("Content-Type", List.of("text/plain")), (k, v) -> true);
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public String body() {
        return body;
    }

    /**
     * Returns an {@link Optional} containing the {@link SSLSession} in effect
     * for this response. Returns an empty {@code Optional} if this is not a
     * <i>HTTPS</i> response.
     *
     * @return an {@code Optional} containing the {@code SSLSession} associated
     * with the response
     */
    @Override
    public Optional<SSLSession> sslSession() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }

    @Override
    public URI uri() {
        return URI.create(""); // You can return a dummy URI or handle this differently
    }

    /**
     * Returns the HTTP protocol version that was used for this response.
     *
     * @return HTTP protocol version
     */
    @Override
    public HttpClient.Version version() {
        return null;
    }

    @Override
    public HttpRequest request() {
        return null;
    }

    /**
     * Returns an {@code Optional} containing the previous intermediate response
     * if one was received. An intermediate response is one that is received
     * as a result of redirection or authentication. If no previous response
     * was received then an empty {@code Optional} is returned.
     *
     * @return an Optional containing the HttpResponse, if any.
     */
    @Override
    public Optional<HttpResponse<String>> previousResponse() {
        return Optional.empty();
    }

    // Optional: You could implement the `toString` method for debugging
    @Override
    public String toString() {
        return "CustomHttpResponse{statusCode=" + statusCode + ", body='" + body + "', headers=" + headers + '}';
    }
}
