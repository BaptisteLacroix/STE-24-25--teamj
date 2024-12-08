package fr.unice.polytech.equipe.j.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.payment.JacksonConfig;
import fr.unice.polytech.equipe.j.payment.RequestUtil;
import fr.unice.polytech.equipe.j.payment.dto.PaymentRequestDTO;
import fr.unice.polytech.equipe.j.payment.dto.PaymentResultDTO;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import static fr.unice.polytech.equipe.j.httpresponse.HttpCode.*;

@Controller("/api/payment")
public class PaymentController {

    private static final String PAYMENT_SERVICE_URI = "http://payment-service/api/payments";

    @Route(value = "/process", method = HttpMethod.POST)
    public HttpResponse processPayment(@BeanParam PaymentRequestDTO paymentRequestDTO) {
        try {
            // Validate essential payment request details
            if (paymentRequestDTO == null ||
                    paymentRequestDTO.getUserId() == null ||
                    paymentRequestDTO.getAmount() <= 0 ||
                    paymentRequestDTO.getPaymentMethod() == null) {
                return RequestUtil.createHttpResponse(
                        HttpCode.HTTP_400,
                        "Invalid payment request details"
                );
            }

            // Prepare ObjectMapper for JSON processing
            ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();

            // Convert PaymentRequestDTO to JSON
            String requestBody = objectMapper.writeValueAsString(paymentRequestDTO);

//            // Prepare HTTP client and request
//            HttpClient client = HttpClient.newHttpClient();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(java.net.URI.create(PAYMENT_SERVICE_URI + "/process"))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                    .build();
//
//            // Execute payment request
//            java.net.http.HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
//
//            // Process response
//            if (response.statusCode() == 200) {
//                // Parse successful payment result
//                PaymentResultDTO paymentResult = objectMapper.readValue(
//                        response.body(),
//                        PaymentResultDTO.class
//                );
//
//                return RequestUtil.createHttpResponse(
//                        HTTP_200,
//                        objectMapper.writeValueAsString(paymentResult)
//                );
//            } else {
//                return RequestUtil.createHttpResponse(
//                        HttpCode.HTTP_400,
//                        "Payment processing failed: " + response.body()
//                );
//            }

        } catch (Exception e) {
            e.printStackTrace();
            return RequestUtil.createHttpResponse(
                    HTTP_500,
                    "Internal server error during payment processing: " + e.getMessage()
            );
        }
    }

//    @Route(value = "/validate", method = HttpMethod.POST)
//    public HttpResponse validatePaymentRequest(@BeanParam PaymentRequestDTO paymentRequestDTO) {
//        try {
//            // Validate essential payment request details
//            if (paymentRequestDTO == null ||
//                    paymentRequestDTO.getUserId() == null ||
//                    paymentRequestDTO.getAmount() <= 0 ||
//                    paymentRequestDTO.getPaymentMethod() == null) {
//                return RequestUtil.createHttpResponse(
//                        HttpCode.HTTP_400,
//                        "Invalid payment request details"
//                );
//            }
//
//            // Prepare ObjectMapper for JSON processing
//            ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
//
//            // Convert PaymentRequestDTO to JSON
//            String requestBody = objectMapper.writeValueAsString(paymentRequestDTO);
//
//            // Prepare HTTP client and request
//            HttpClient client = HttpClient.newHttpClient();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(java.net.URI.create(PAYMENT_SERVICE_URI + "/validate"))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                    .build();
//
//            // Execute validation request
//            java.net.http.HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
//
//            // Return validation result
//            return RequestUtil.createHttpResponse(
//                    HttpCode.fromCode(response.statusCode()),
//                    response.body()
//            );
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return RequestUtil.createHttpResponse(
//                    HTTP_500,
//                    "Internal server error during payment validation: " + e.getMessage()
//            );
//        }
//    }
}