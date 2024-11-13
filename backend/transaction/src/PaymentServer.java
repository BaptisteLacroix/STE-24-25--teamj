
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import fr.unice.polytech.equipe.j.payment.strategy.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class PaymentServer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/payment", new PaymentHandler());
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        System.out.println("Server started on port " + port);
    }

    static class PaymentHandler implements HttpHandler {
        private static final double MINIMUM_AMOUNT = 0.01;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                if ("POST".equals(exchange.getRequestMethod())) {
                    handlePaymentRequest(exchange);
                } else {
                    sendResponse(exchange, 405, createErrorResponse("Method not allowed"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, createErrorResponse("Internal server error: " + e.getMessage()));
            }
        }

        private void handlePaymentRequest(HttpExchange exchange) throws IOException {
            // Read request body
            PaymentRequest request = readRequest(exchange.getRequestBody());

            // Validate request
            String validationError = validateRequest(request);
            if (validationError != null) {
                sendResponse(exchange, 400, createErrorResponse(validationError));
                return;
            }

            try {
                // Process payment
                PaymentProcessor processor = PaymentProcessorFactory.createPaymentProcessor(request.getPaymentMethod());

                // just a place holder
                boolean processingResult = true ;
                processor.processPayment(request.getAmount());

                if (processingResult) {
                    PaymentResult result = new PaymentResult();
                    result.setSuccess(true);
                    result.setUserId(request.getUserId());
                    result.setPaymentMethod(request.getPaymentMethod());
                    result.setAmount(request.getAmount());
                    result.setMessage("Payment processed successfully");
                    result.setTransactionId(generateTransactionId());

                    // Send success response
                    String jsonResponse = objectMapper.writeValueAsString(result);
                    sendResponse(exchange, 200, jsonResponse);
                } else {
                    sendResponse(exchange, 400, createErrorResponse("Payment processing failed"));
                }
            } catch (IllegalArgumentException e) {
                sendResponse(exchange, 400, createErrorResponse("Invalid payment method: " + e.getMessage()));
            } catch (Exception e) {
                sendResponse(exchange, 500, createErrorResponse("Payment processing failed: " + e.getMessage()));
            }
        }

        private String validateRequest(PaymentRequest request) {
            if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
                return "UserId is required";
            }
            if (request.getPaymentMethod() == null) {
                return "Payment method is required";
            }
            if (request.getAmount() == null) {
                return "Amount is required";
            }
            if (request.getAmount() < MINIMUM_AMOUNT) {
                return "Amount must be greater than " + MINIMUM_AMOUNT;
            }
            return null;
        }

        private PaymentRequest readRequest(InputStream requestBody) throws IOException {
            return objectMapper.readValue(requestBody, PaymentRequest.class);
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            byte[] responseBytes = response.getBytes();
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }

        private String createErrorResponse(String message) throws IOException {
            ErrorResponse error = new ErrorResponse(message);
            return objectMapper.writeValueAsString(error);
        }

        private String generateTransactionId() {
            return "TRX-" + System.currentTimeMillis() + "-" + Math.round(Math.random() * 1000);
        }
    }
}

class PaymentRequest {
    private String userId;
    private PaymentMethod paymentMethod;
    private Double amount;

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

class PaymentResult {
    private boolean success;
    private String userId;
    private PaymentMethod paymentMethod;
    private Double amount;
    private String transactionId;
    private String message;

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}