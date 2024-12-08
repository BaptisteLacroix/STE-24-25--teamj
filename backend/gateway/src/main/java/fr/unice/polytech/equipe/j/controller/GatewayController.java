package fr.unice.polytech.equipe.j.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.utils.JacksonConfig;
import fr.unice.polytech.equipe.j.utils.RequestUtil;

import java.time.LocalDateTime;

import static fr.unice.polytech.equipe.j.utils.RequestUtil.request;

@Controller("/api/gateway")
public class GatewayController {
    private ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();

    @Route(value = "/restaurants/all", method = HttpMethod.GET)
    public HttpResponse getAllRestaurants() {
        java.net.http.HttpResponse<String> response = request(
                RequestUtil.RESTAURANT_SERVICE_URI,
                "/all",
                HttpMethod.GET,
                null
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/restaurants/{id}", method = HttpMethod.GET)
    public HttpResponse getRestaurantById(@PathParam("id") String id) {
        java.net.http.HttpResponse<String> response = request(
                RequestUtil.RESTAURANT_SERVICE_URI,
                "/" + id,
                HttpMethod.GET,
                null
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/restaurants/foodType/{foodType}", method = HttpMethod.GET)
    public HttpResponse getRestaurantsByFoodType(@PathParam("foodType") String foodType) {
        java.net.http.HttpResponse<String> response = request(
                RequestUtil.RESTAURANT_SERVICE_URI,
                "/foodType/" + foodType,
                HttpMethod.GET,
                null
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/restaurants/name/{name}", method = HttpMethod.GET)
    public HttpResponse getRestaurantsByName(@PathParam("name") String name) {
        java.net.http.HttpResponse<String> response = request(
                RequestUtil.RESTAURANT_SERVICE_URI,
                "/name/" + name,
                HttpMethod.GET,
                null
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/${userId}/restaurants/${restaurantId}/orders/${orderId}/item/${dishId}", method = HttpMethod.POST)
    public HttpResponse addItemToOrder(
            @PathParam("userId") String userId,
            @PathParam("restaurantId") String restaurantId,
            @PathParam("orderId") String orderId,
            @PathParam("dishId") String dishId,
            @BeanParam DeliveryDetailsDTO deliveryDetailsDTO,
            @BeanParam String groupOrderId
    ) {
        // Get user from database
        java.net.http.HttpResponse<String> response = request(
                RequestUtil.DATABASE_CAMPUS_USER_SERVICE_URI,
                "/" + userId,
                HttpMethod.GET,
                null
        );
        if (response.statusCode() != 200) {
            return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
        }
        LocalDateTime deliveryTime = null;
        if (deliveryDetailsDTO != null) {
            deliveryTime = deliveryDetailsDTO.getDeliveryTime();
        }
        if (groupOrderId != null) {
            // Get group order from database
            response = request(
                    RequestUtil.DATABASE_ORDER_SERVICE_URI,
                    "/" + groupOrderId,
                    HttpMethod.GET,
                    null
            );
            if (response.statusCode() != 200) {
                return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
            }
            try {
                // Parse the JSON string into a JsonNode
                JsonNode rootNode = objectMapper.readTree(response.body());

                // Extract the "deliveryDetails" node from the root of the JSON
                JsonNode deliveryDetailsNode = rootNode.path("deliveryDetails");

                // Deserialize the "deliveryDetails" node into your DeliveryDetailsDTO class
                if (!deliveryDetailsNode.isMissingNode()) {
                    DeliveryDetailsDTO deliveryDetails = objectMapper.treeToValue(deliveryDetailsNode, DeliveryDetailsDTO.class);
                    deliveryTime = deliveryDetails.getDeliveryTime();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new HttpResponse(HttpCode.HTTP_500, "An error occurred while processing the request.");
            }
        }

        //TODO: If deliveryTime is null check that the order is not an individual order, if it is return an error

        response = request(
                RequestUtil.RESTAURANT_SERVICE_URI,
                "/" + restaurantId + "/orders/" + orderId + "/item/" + dishId + "?deliveryTime=" + deliveryTime,
                HttpMethod.POST,
                null
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }
}
