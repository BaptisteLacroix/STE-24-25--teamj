package fr.unice.polytech.equipe.j.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.dto.IndividualOrderDTO;
import fr.unice.polytech.equipe.j.dto.OrderDTO;
import fr.unice.polytech.equipe.j.dto.OrderStatus;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.utils.JacksonConfig;
import fr.unice.polytech.equipe.j.utils.RequestUtil;

import java.time.LocalDateTime;
import java.util.UUID;

import static fr.unice.polytech.equipe.j.utils.RequestUtil.request;

@Controller("/api/gateway")
public class GatewayController {
    private ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
    private java.net.http.HttpResponse<String> response;

    @Route(value = "/restaurants/types", method = HttpMethod.GET)
    public HttpResponse getAllFoodTypes() {
        response = request(
                RequestUtil.RESTAURANT_SERVICE_URI,
                "/types",
                HttpMethod.GET,
                null
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/restaurants/all", method = HttpMethod.GET)
    public HttpResponse getAllRestaurants() {
        response = request(
                RequestUtil.RESTAURANT_SERVICE_URI,
                "/all",
                HttpMethod.GET,
                null
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/restaurants/{id}", method = HttpMethod.GET)
    public HttpResponse getRestaurantById(@PathParam("id") String id) {
        response = request(
                RequestUtil.RESTAURANT_SERVICE_URI,
                "/" + id,
                HttpMethod.GET,
                null
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/restaurants/foodType/{foodType}", method = HttpMethod.GET)
    public HttpResponse getRestaurantsByFoodType(@PathParam("foodType") String foodType) {
        response = request(
                RequestUtil.RESTAURANT_SERVICE_URI,
                "/foodType/" + foodType,
                HttpMethod.GET,
                null
        );
        System.out.println(response.body());
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/restaurants/name/{name}", method = HttpMethod.GET)
    public HttpResponse getRestaurantsByName(@PathParam("name") String name) {
        response = request(
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
        // Step 1: Validate user and get deliveryTime
        HttpResponse userValidationResponse = validateUser(userId);
        if (userValidationResponse != null) return userValidationResponse;

        LocalDateTime deliveryTime = getDeliveryTime(groupOrderId, deliveryDetailsDTO);
        if (deliveryTime == null)
            return new HttpResponse(HttpCode.HTTP_404, "Delivery details are required for an individual order");

        // Step 2: Validate and handle the order based on groupOrderId
        HttpResponse orderResponse = processOrder(userId, orderId, restaurantId, dishId, deliveryTime, groupOrderId, deliveryDetailsDTO);
        if (orderResponse != null) return orderResponse;

        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    // Validate User
    private HttpResponse validateUser(String userId) {
        if (getUserById(userId).statusCode() != 200) {
            return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
        }
        return null;
    }

    // Get Delivery Time (either from groupOrder or individual order)
    private LocalDateTime getDeliveryTime(String groupOrderId, DeliveryDetailsDTO deliveryDetailsDTO) {
        if (groupOrderId != null) {
            return getDeliveryTimeFromGroupOrder(groupOrderId);
        } else if (deliveryDetailsDTO != null) {
            return deliveryDetailsDTO.getDeliveryTime();
        }
        return null;
    }

    // Extract delivery time from the group order response
    private LocalDateTime getDeliveryTimeFromGroupOrder(String groupOrderId) {
        response = getGroupOrderById(groupOrderId);
        if (response.statusCode() != 200) {
            return null;
        }
        try {
            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode deliveryDetailsNode = rootNode.path("deliveryDetails");

            if (!deliveryDetailsNode.isMissingNode()) {
                DeliveryDetailsDTO deliveryDetails = objectMapper.treeToValue(deliveryDetailsNode, DeliveryDetailsDTO.class);
                return deliveryDetails.getDeliveryTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Process Order and Item Addition Logic
    private HttpResponse processOrder(
            String userId, String orderId, String restaurantId, String dishId, LocalDateTime deliveryTime,
            String groupOrderId, DeliveryDetailsDTO deliveryDetailsDTO
    ) {
        int statusCodeOrder = getOrderById(orderId).statusCode();
        int statusCodeIndividual = getIndividualOrderById(orderId).statusCode();

        if (groupOrderId != null) {
            return handleGroupOrder(userId, orderId, restaurantId, dishId, deliveryTime, statusCodeOrder, statusCodeIndividual);
        } else if (deliveryDetailsDTO != null) {
            return handleIndividualOrder(userId, orderId, restaurantId, dishId, deliveryTime, statusCodeIndividual, deliveryDetailsDTO);
        }
        return null;
    }

    // Handle Group Order Logic
    private HttpResponse handleGroupOrder(String userId, String orderId, String restaurantId, String dishId, LocalDateTime deliveryTime, int statusCodeOrder, int statusCodeIndividual) {
        if (statusCodeOrder != 200 && statusCodeIndividual != 200) {
            return createNewOrderAndAddItem(userId, orderId, restaurantId, dishId, deliveryTime);
        } else if (statusCodeOrder == 200 && statusCodeIndividual != 200) {
            return addItemToRestaurantOrder(restaurantId, orderId, dishId, deliveryTime);
        } else {
            return new HttpResponse(HttpCode.HTTP_404, "Individual order can't be used as order within a group order");
        }
    }

    // Handle Individual Order Logic
    private HttpResponse handleIndividualOrder(String userId, String orderId, String restaurantId, String dishId, LocalDateTime deliveryTime, int statusCodeIndividual, DeliveryDetailsDTO deliveryDetailsDTO) {
        if (statusCodeIndividual != 200) {
            return createNewIndividualOrderAndAddItem(userId, orderId, restaurantId, dishId, deliveryDetailsDTO);
        } else {
            return addItemToRestaurantOrder(restaurantId, orderId, dishId, deliveryTime);
        }
    }

    // Create and add item to a new order if order does not exist
    private HttpResponse createNewOrderAndAddItem(String userId, String orderId, String restaurantId, String dishId, LocalDateTime deliveryTime) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(UUID.fromString(orderId));
        orderDTO.setUserId(UUID.fromString(userId));
        orderDTO.setStatus(OrderStatus.PENDING.toString());

        if (updateOrder(orderDTO).statusCode() != 201) {
            return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
        }
        return addItemToRestaurantOrder(restaurantId, orderId, dishId, deliveryTime);
    }

    // Create and add item to a new individual order
    private HttpResponse createNewIndividualOrderAndAddItem(String userId, String orderId, String restaurantId, String dishId, DeliveryDetailsDTO deliveryDetailsDTO) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(UUID.fromString(orderId));
        orderDTO.setUserId(UUID.fromString(userId));
        orderDTO.setStatus(OrderStatus.PENDING.toString());

        IndividualOrderDTO individualOrderDTO = new IndividualOrderDTO(orderDTO, deliveryDetailsDTO);

        if (updateIndividualOrder(individualOrderDTO).statusCode() != 201) {
            return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
        }
        return addItemToRestaurantOrder(restaurantId, orderId, dishId, deliveryDetailsDTO.getDeliveryTime());
    }

    // Add item to the restaurant order
    private HttpResponse addItemToRestaurantOrder(String restaurantId, String orderId, String dishId, LocalDateTime deliveryTime) {
        response = request(
                RequestUtil.RESTAURANT_SERVICE_URI,
                "/" + restaurantId + "/orders/" + orderId + "/item/" + dishId + "?deliveryTime=" + deliveryTime,
                HttpMethod.POST,
                null
        );
        if (response.statusCode() != 200) {
            return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
        }
        return null;
    }

    @Route(value = "/{userId}/orders/{orderId}/cancel", method = HttpMethod.PUT)
    public HttpResponse cancelOrder(@PathParam("userId") String userId, @PathParam("orderId") String orderId) {
        if (getUserById(userId).statusCode() != 200) {
            return new HttpResponse(HttpCode.HTTP_404, "User not found");
        }

        if (getOrderById(orderId).statusCode() != 200) {
            return new HttpResponse(HttpCode.HTTP_404, "Order not found");
        }

        OrderDTO orderDTO = objectMapper.convertValue(response.body(), OrderDTO.class);
        if (!orderDTO.getStatus().equals(OrderStatus.PENDING.toString())) {
            return new HttpResponse(HttpCode.HTTP_400, "Order can't be cancelled");
        }
        orderDTO.setStatus(OrderStatus.CANCELLED.toString());
        response = updateOrder(orderDTO);
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/{userId}/orders/{orderId}", method = HttpMethod.GET)
    public HttpResponse getOrder(@PathParam("userId") String userId, @PathParam("orderId") String orderId) {
        if (getUserById(userId).statusCode() != 200) {
            return new HttpResponse(HttpCode.HTTP_404, "User not found");
        }

        if (getOrderById(orderId).statusCode() != 200) {
            return new HttpResponse(HttpCode.HTTP_404, "Order not found");
        }
        OrderDTO orderDTO = objectMapper.convertValue(response.body(), OrderDTO.class);
        if (orderDTO.getUserId().equals(UUID.fromString(userId))) {
            return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
        }
        return new HttpResponse(HttpCode.HTTP_403, "Order not found for user: " + userId);
    }

    @Route(value = "/{userId}/restaurants/{restaurantId}/orders/{orderId}/total-price", method = HttpMethod.GET)
    public HttpResponse getTotalPrice(@PathParam("userId") String userId, @PathParam("restaurantId") String restaurantId, @PathParam("orderId") String orderId) {
        if (getUserById(userId).statusCode() != 200) {
            return new HttpResponse(HttpCode.HTTP_404, "User not found");
        }

        if (getOrderById(orderId).statusCode() != 200) {
            return new HttpResponse(HttpCode.HTTP_404, "Order not found");
        }

        response = request(
                RequestUtil.RESTAURANT_SERVICE_URI,
                "/" + restaurantId + "/orders/" + orderId + "/total-price",
                HttpMethod.GET,
                null
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/{userId}/delivery-location", method = HttpMethod.GET)
    public HttpResponse getDeliveryLocation(@PathParam("userId") String userId) {
        if (getUserById(userId).statusCode() != 200) {
            return new HttpResponse(HttpCode.HTTP_404, "User not found");
        }
        response = request(
                RequestUtil.DATABASE_DELIVERY_LOCATION_SERVICE_URI,
                "",
                HttpMethod.GET,
                null
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/{userId}/delivery-location/{deliveryLocationId}", method = HttpMethod.GET)
    public HttpResponse getDeliveryLocationById(@PathParam("userId") String userId, @PathParam("deliveryLocationId") String deliveryLocationId) {
        if (getUserById(userId).statusCode() != 200) {
            return new HttpResponse(HttpCode.HTTP_404, "User not found");
        }
        response = request(
                RequestUtil.DATABASE_DELIVERY_LOCATION_SERVICE_URI,
                "/" + deliveryLocationId,
                HttpMethod.GET,
                null
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/{userId}/group-order", method = HttpMethod.POST)
    public HttpResponse createGroupOrder(@PathParam("userId") String userId, @BeanParam DeliveryDetailsDTO deliveryDetailsDTO) {
        if (getUserById(userId).statusCode() != 200) {
            return new HttpResponse(HttpCode.HTTP_404, "User not found");
        }

        response = request(
                RequestUtil.GROUP_ORDER_SERVICE_URI,
                "/" + userId + "/create",
                HttpMethod.POST,
                objectMapper.valueToTree(deliveryDetailsDTO).toString()
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    @Route(value = "/{userId}/group-order/{groupOrderId}/join", method = HttpMethod.PUT)
    public HttpResponse joinGroupOrder(@PathParam("userId") String userId, @PathParam("groupOrderId") String groupOrderId) {
        if (getUserById(userId).statusCode() != 200) {
            return new HttpResponse(HttpCode.HTTP_404, "User not found");
        }

        response = request(
                RequestUtil.GROUP_ORDER_SERVICE_URI,
                "/" + groupOrderId + "/join/" + userId,
                HttpMethod.PUT,
                null
        );
        return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
    }

    private java.net.http.HttpResponse<String> getUserById(String userId) {
        response = request(
                RequestUtil.DATABASE_CAMPUS_USER_SERVICE_URI,
                "/" + userId,
                HttpMethod.GET,
                null
        );
        return response;
    }

    private java.net.http.HttpResponse<String> getGroupOrderById(String groupOrderId) {
        response = request(
                RequestUtil.DATABASE_ORDER_SERVICE_URI,
                "/" + groupOrderId,
                HttpMethod.GET,
                null
        );
        return response;
    }

    private java.net.http.HttpResponse<String> getOrderById(String orderId) {
        response = request(
                RequestUtil.DATABASE_ORDER_SERVICE_URI,
                "/" + orderId,
                HttpMethod.GET,
                null
        );
        return response;
    }

    private java.net.http.HttpResponse<String> getIndividualOrderById(String orderId) {
        response = request(
                RequestUtil.DATABASE_ORDER_SERVICE_URI,
                "/individual/" + orderId,
                HttpMethod.GET,
                null
        );
        return response;
    }

    private java.net.http.HttpResponse<String> updateOrder(OrderDTO orderDTO) {
        response = request(
                RequestUtil.DATABASE_ORDER_SERVICE_URI,
                "/update",
                HttpMethod.PUT,
                objectMapper.valueToTree(orderDTO).toString()
        );
        return response;
    }

    private java.net.http.HttpResponse<String> updateIndividualOrder(IndividualOrderDTO orderDTO) {
        response = request(
                RequestUtil.DATABASE_ORDER_SERVICE_URI,
                "/individual/update",
                HttpMethod.PUT,
                objectMapper.valueToTree(orderDTO).toString()
        );
        return response;
    }
}
