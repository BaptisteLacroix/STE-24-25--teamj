package fr.unice.polytech.equipe.j.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.payment.RequestUtil;
import fr.unice.polytech.equipe.j.payment.dto.PaymentRequestDTO;
import fr.unice.polytech.equipe.j.payment.dto.PaymentResultDTO;
import fr.unice.polytech.equipe.j.payment.PaymentProcessor;
import fr.unice.polytech.equipe.j.payment.PaymentProcessorFactory;

import static fr.unice.polytech.equipe.j.FlexibleRestServer.objectMapper;
import static fr.unice.polytech.equipe.j.httpresponse.HttpCode.*;

@Controller("/api/payment")
public class PaymentController {

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

            // Process payment
            PaymentProcessor processor = PaymentProcessorFactory.createPaymentProcessor(paymentRequestDTO.getPaymentMethod());

            // just a place holder
            boolean processingResult = true;
            processor.processPayment(paymentRequestDTO.getAmount());

            // Create payment result
            PaymentResultDTO paymentResult = new PaymentResultDTO();
            paymentResult.setUserId(paymentRequestDTO.getUserId());
            paymentResult.setAmount(paymentRequestDTO.getAmount());
            paymentResult.setPaymentMethod(paymentRequestDTO.getPaymentMethod());
            paymentResult.setTransactionId(generateTransactionId());
            paymentResult.setSuccess(processingResult);

            // Return successful response
            return RequestUtil.createHttpResponse(
                    HTTP_200,
                    objectMapper.writeValueAsString(paymentResult)
            );

        } catch (Exception e) {
            e.printStackTrace();
            return RequestUtil.createHttpResponse(
                    HTTP_500,
                    "Internal server error during payment processing: " + e.getMessage()
            );
        }
    }

    // Helper method to generate a unique transaction ID
    private String generateTransactionId() {
        return "TRX-" + System.currentTimeMillis() + "-" + Math.round(Math.random() * 1000);
    }
}