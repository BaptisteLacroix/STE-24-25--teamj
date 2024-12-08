package fr.unice.polytech.equipe.j.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.JacksonConfig;
import fr.unice.polytech.equipe.j.RequestUtil;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.QueryParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.dto.IndividualOrderDTO;
import fr.unice.polytech.equipe.j.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.dto.OrderDTO;
import fr.unice.polytech.equipe.j.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.mapper.DTOMapper;
import fr.unice.polytech.equipe.j.menu.Menu;
import fr.unice.polytech.equipe.j.orderpricestrategy.OrderPriceStrategy;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static fr.unice.polytech.equipe.j.RequestUtil.createHttpResponse;
import static fr.unice.polytech.equipe.j.RequestUtil.request;

@Controller("/api/restaurants")
public class RestaurantController {

    @Route(value = "/all", method = HttpMethod.GET)
    public HttpResponse getAllRestaurants() {
        try {
            java.net.http.HttpResponse<String> response = request(RequestUtil.DATABASE_RESTAURANT_SERVICE_URI, "/all", HttpMethod.GET, null);
            return createHttpResponse(HttpCode.HTTP_200, response.body());
        } catch (Exception e) {
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "/{restaurantId}", method = HttpMethod.GET)
    public HttpResponse getRestaurant(@PathParam("restaurantId") UUID restaurantId) {
        try {
            java.net.http.HttpResponse<String> response = request(RequestUtil.DATABASE_RESTAURANT_SERVICE_URI, "/" + restaurantId, HttpMethod.GET, null);
            return createHttpResponse(HttpCode.HTTP_200, response.body());
        } catch (Exception e) {
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "/foodType/{foodType}", method = HttpMethod.GET)
    public HttpResponse searchByFoodType(@PathParam("foodType") String foodType) {
        try {
            // Fetch the list of restaurants from the database
            List<IRestaurant> restaurants = fetchRestaurantsFromDatabase();

            // Use the searchByTypeOfFood method from RestaurantServiceManager to filter restaurants by food type
            List<IRestaurant> matchingRestaurants = RestaurantServiceManager.getInstance()
                    .searchByTypeOfFood(restaurants, foodType);

            // Return 404 if no matching restaurants are found
            if (matchingRestaurants.isEmpty()) {
                return createHttpResponse(HttpCode.HTTP_404, "No restaurants found with the given food type");
            }

            // Return 200 with the list of matching restaurants in JSON format
            return createHttpResponse(HttpCode.HTTP_200, convertRestaurantsToJson(matchingRestaurants));

        } catch (Exception e) {
            // Return 500 in case of any errors
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    /**
     * Fetches the list of restaurants from the database.
     * This method makes an HTTP request to the database service to retrieve all restaurant data,
     * then converts the response body into a list of IRestaurant objects.
     *
     * @return A list of IRestaurant objects fetched from the database.
     * @throws IOException If there is an error during the HTTP request or JSON parsing.
     */
    private List<IRestaurant> fetchRestaurantsFromDatabase() throws IOException {
        java.net.http.HttpResponse<String> response = request(
                RequestUtil.DATABASE_RESTAURANT_SERVICE_URI,
                "/all",
                HttpMethod.GET,
                null
        );
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        List<RestaurantDTO> restaurantsDTO = objectMapper.readValue(response.body(), new TypeReference<List<RestaurantDTO>>() {
        });

        return restaurantsDTO.stream()
                .map(DTOMapper::toRestaurant)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of IRestaurant objects into a JSON string.
     * This method maps the list of IRestaurant objects to a list of RestaurantDTOs,
     * then converts the list of DTOs to JSON format using Jackson ObjectMapper.
     *
     * @param restaurants The list of IRestaurant objects to convert.
     * @return A JSON string representing the list of restaurants.
     * @throws JsonProcessingException If there is an error during JSON processing.
     */
    private String convertRestaurantsToJson(List<IRestaurant> restaurants) throws JsonProcessingException {
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        List<RestaurantDTO> matchingRestaurantsDTO = restaurants.stream()
                .map(DTOMapper::toRestaurantDTO)
                .collect(Collectors.toList());
        return objectMapper.writeValueAsString(matchingRestaurantsDTO);
    }


    @Route(value = "/deliveryTime/{hour}/{minutes}", method = HttpMethod.GET)
    public HttpResponse searchByDeliveryTime(
            @PathParam("hour") int hour,
            @PathParam("minutes") int minutes) {
        try {
            // Convert hours and minutes into a LocalDateTime object
            LocalDateTime deliveryTime = LocalDateTime.now().withHour(hour).withMinute(minutes).withSecond(0).withNano(0);

            java.net.http.HttpResponse<String> response = request(
                    RequestUtil.DATABASE_RESTAURANT_SERVICE_URI,
                    "/all",
                    HttpMethod.GET,
                    null
            );
            ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
            List<RestaurantDTO> restaurantDTOS = objectMapper.readValue(response.body(), new TypeReference<List<RestaurantDTO>>() {
            });
            List<IRestaurant> restaurants = restaurantDTOS.stream()
                    .map(DTOMapper::toRestaurant)
                    .collect(Collectors.toList());
            // Fetch the list of restaurants that can deliver at the specified time
            List<IRestaurant> matchingRestaurants = RestaurantServiceManager.getInstance()
                    .searchRestaurantByDeliveryTime(restaurants, Optional.of(deliveryTime));

            List<RestaurantDTO> matchingRestaurantsDTO = matchingRestaurants.stream()
                    .map(DTOMapper::toRestaurantDTO)
                    .collect(Collectors.toList());
            // Convert the list of restaurants to JSON response
            String jsonResponse = objectMapper.writeValueAsString(matchingRestaurantsDTO);
            return createHttpResponse(HttpCode.HTTP_200, jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "/{restaurantId}/orders/{orderId}/item/{menuItemId}", method = HttpMethod.POST)
    public HttpResponse addItemToOrder(
            @PathParam("restaurantId") UUID restaurantId,
            @PathParam("orderId") UUID orderId,
            @PathParam("menuItemId") UUID menuItemId,
            @QueryParam("deliveryTime") String deliveryTime) {

        try {
            // Fetch restaurant, order, and menu item details
            java.net.http.HttpResponse<String> menuItemResponse = request(
                    RequestUtil.DATABASE_RESTAURANT_SERVICE_URI,
                    "/" + restaurantId + "/menuItem/" + menuItemId,
                    HttpMethod.GET,
                    null);
            if (menuItemResponse == null || menuItemResponse.statusCode() != 200) {
                return createHttpResponse(HttpCode.HTTP_404, "Menu item not found");
            }

            IRestaurant restaurantProxy = createRestaurantProxy(restaurantId);
            OrderDTO orderDTO = fetchOrderByIdOrIndividual(orderId);
            if (orderDTO == null || restaurantProxy == null) {
                return createHttpResponse(HttpCode.HTTP_404, orderDTO == null ? "Order not found" : "Restaurant not found");
            }
            // Map responses to DTOs
            ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
            MenuItemDTO menuItem = objectMapper.readValue(menuItemResponse.body(), MenuItemDTO.class);

            // Use proxy to add item to the order
            LocalDateTime deliveryDateTime = null;
            if (deliveryTime != null) deliveryDateTime = LocalDateTime.parse(deliveryTime);
            java.net.http.HttpResponse<String> response = restaurantProxy.addItemToOrder(orderDTO, DTOMapper.toMenuItem(menuItem), deliveryDateTime);
            if (response.statusCode() != 201 && response.statusCode() != 200) {
                return createHttpResponse(HttpCode.HTTP_400, response.body());
            }
            return createHttpResponse(HttpCode.HTTP_200, "Item added to order successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "/{restaurantId}/orders/{orderId}/cancel", method = HttpMethod.POST)
    public HttpResponse cancelOrder(
            @PathParam("restaurantId") UUID restaurantId,
            @PathParam("orderId") UUID orderId,
            @BeanParam String deliveryTime) {
        try {
            LocalDateTime deliveryDateTime = LocalDateTime.parse(deliveryTime);
            OrderDTO orderDTO = fetchOrderByIdOrIndividual(orderId);
            IRestaurant restaurantProxy = createRestaurantProxy(restaurantId);

            if (orderDTO == null || restaurantProxy == null) {
                String errorMessage = orderDTO == null ? "Order not found" : "Restaurant not found";
                return createHttpResponse(HttpCode.HTTP_400, errorMessage);
            }

            java.net.http.HttpResponse<String> response = restaurantProxy.cancelOrder(orderDTO, deliveryDateTime);
            return createHttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "/{restaurantId}/manager/{managerId}/changeHours/{openingHour}/{closingHour}", method = HttpMethod.POST)
    public HttpResponse changeHours(
            @PathParam("restaurantId") UUID restaurantId,
            @PathParam("managerId") UUID managerId,
            @PathParam("openingHour") String openingHour,
            @PathParam("closingHour") String closingHour) {
        try {
            LocalDateTime openingHourDateTime = LocalDateTime.parse(openingHour);
            LocalDateTime closingHourDateTime = LocalDateTime.parse(closingHour);
            IRestaurant restaurantProxy = createRestaurantProxy(restaurantId);
            if (restaurantProxy == null) {
                return createHttpResponse(HttpCode.HTTP_400, "Restaurant not found");
            }
            java.net.http.HttpResponse<String> response = restaurantProxy.changeHours(managerId, openingHourDateTime, closingHourDateTime);
            return createHttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
        } catch (Exception e) {
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "/{restaurantId}/manager/{managerId}/slots/{slotId}/changeNumberOfEmployees/{numberOfEmployees}", method = HttpMethod.POST)
    public HttpResponse changeNumberOfEmployees(
            @PathParam("restaurantId") UUID restaurantId,
            @PathParam("managerId") UUID managerId,
            @PathParam("slotId") UUID slotId,
            @PathParam("numberOfEmployees") int numberOfEmployees) {
        try {
            IRestaurant restaurantProxy = createRestaurantProxy(restaurantId);
            if (restaurantProxy == null) {
                return createHttpResponse(HttpCode.HTTP_400, "Restaurant not found");
            }
            java.net.http.HttpResponse<String> response = restaurantProxy.changeNumberOfEmployees(managerId, slotId, numberOfEmployees);
            return createHttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
        } catch (Exception e) {
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "/{restaurantId}/menu", method = HttpMethod.GET)
    public HttpResponse getMenu(@PathParam("restaurantId") UUID restaurantId) {
        try {
            IRestaurant restaurantProxy = createRestaurantProxy(restaurantId);
            if (restaurantProxy == null) {
                return createHttpResponse(HttpCode.HTTP_400, "Restaurant not found");
            }
            Menu menu = restaurantProxy.getMenu();

            ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(menu);

            return createHttpResponse(HttpCode.HTTP_200, jsonResponse);
        } catch (Exception e) {
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "/{restaurantId}/orders/{orderId}/total-price", method = HttpMethod.GET)
    public HttpResponse getTotalPrice(
            @PathParam("restaurantId") UUID restaurantId,
            @PathParam("orderId") UUID orderId) {

        try {
            OrderDTO orderDTO = fetchOrderByIdOrIndividual(orderId);
            IRestaurant restaurantProxy = createRestaurantProxy(restaurantId);
            if (orderDTO == null || restaurantProxy == null) {
                return createHttpResponse(HttpCode.HTTP_400, orderDTO == null ? "Order not found" : "Restaurant not found");
            }
            double totalPrice = restaurantProxy.getTotalPrice(orderDTO);
            return createHttpResponse(HttpCode.HTTP_200, String.valueOf(totalPrice));
        } catch (Exception e) {
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "/{restaurantId}/slots/available", method = HttpMethod.GET)
    public HttpResponse isSlotCapacityAvailable(@PathParam("restaurantId") UUID restaurantId) {
        try {
            IRestaurant restaurantProxy = createRestaurantProxy(restaurantId);
            if (restaurantProxy == null) {
                return createHttpResponse(HttpCode.HTTP_400, "Restaurant not found");
            }
            boolean isAvailable = restaurantProxy.isSlotCapacityAvailable();

            return createHttpResponse(HttpCode.HTTP_200, String.valueOf(isAvailable));
        } catch (Exception e) {
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "/{restaurantId}/orders/{orderUUID}/paid", method = HttpMethod.POST)
    public HttpResponse onOrderPaid(
            @PathParam("restaurantId") UUID restaurantId,
            @PathParam("orderUUID") UUID orderId) {
        try {
            OrderDTO orderDTO = fetchOrderByIdOrIndividual(orderId);
            IRestaurant restaurantProxy = createRestaurantProxy(restaurantId);
            if (orderDTO == null || restaurantProxy == null) {
                return createHttpResponse(HttpCode.HTTP_400, orderDTO == null ? "Order not found" : "Restaurant not found");
            }
            java.net.http.HttpResponse<String> response = restaurantProxy.onOrderPaid(orderDTO);
            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return createHttpResponse(HttpCode.HTTP_200, "Order paid successfully");
            }
            return createHttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
        } catch (Exception e) {
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    // TODO Keep it ?
    @Route(value = "/{restaurantId}/order-price-strategy", method = HttpMethod.POST)
    public HttpResponse setOrderPriceStrategy(
            @PathParam("restaurantId") UUID restaurantId,
            @BeanParam OrderPriceStrategy orderPriceStrategy) {

        try {
            IRestaurant restaurantProxy = createRestaurantProxy(restaurantId);
            if (restaurantProxy == null) {
                return createHttpResponse(HttpCode.HTTP_400, "Restaurant not found");
            }
            restaurantProxy.setOrderPriceStrategy(orderPriceStrategy);

            return createHttpResponse(HttpCode.HTTP_200, "Order price strategy set successfully");
        } catch (Exception e) {
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "/{restaurantId}/can-prepare", method = HttpMethod.GET)
    public HttpResponse canPrepareItemForDeliveryTime(
            @PathParam("restaurantId") UUID restaurantId,
            @QueryParam("deliveryTime") String deliveryTime) {
        try {
            // Parse delivery time from query parameter
            LocalDateTime parsedDeliveryTime = LocalDateTime.parse(deliveryTime);

            // Fetch restaurant proxy
            IRestaurant restaurantProxy = createRestaurantProxy(restaurantId);
            if (restaurantProxy == null) {
                return createHttpResponse(HttpCode.HTTP_400, "Restaurant not found");
            }

            // Check if the restaurant can prepare an item for the given delivery time
            boolean canPrepare = restaurantProxy.canPrepareItemInTime(parsedDeliveryTime);

            // Return response
            if (canPrepare) {
                return createHttpResponse(HttpCode.HTTP_200, new JSONPObject("canPrepare", true).toString());
            } else {
                return createHttpResponse(HttpCode.HTTP_200, new JSONPObject("canPrepare", false).toString());
            }
        } catch (DateTimeParseException e) {
            // Handle invalid delivery time format
            return createHttpResponse(HttpCode.HTTP_400, "Invalid delivery time format: " + deliveryTime);
        } catch (Exception e) {
            // Handle other errors
            return createHttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    // Helper Methods
    private IRestaurant createRestaurantProxy(UUID restaurantId) {
        try {
            // Fetch restaurant data
            java.net.http.HttpResponse<String> restaurantResponse = request(
                    RequestUtil.DATABASE_RESTAURANT_SERVICE_URI,
                    "/" + restaurantId,
                    HttpMethod.GET,
                    null);
            // Parse response
            ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
            RestaurantDTO restaurantDTO = objectMapper.readValue(restaurantResponse.body(), RestaurantDTO.class);
            // Create proxy
            return new RestaurantProxy(DTOMapper.toRestaurant(restaurantDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private OrderDTO fetchOrderByIdOrIndividual(UUID orderId) {
        try {
            // Attempt to fetch regular order data
            java.net.http.HttpResponse<String> orderResponse = request(
                    RequestUtil.DATABASE_ORDER_SERVICE_URI,
                    "/" + orderId,
                    HttpMethod.GET,
                    null);

            // Parse response as OrderDTO
            ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
            return objectMapper.readValue(orderResponse.body(), OrderDTO.class);
        } catch (Exception e) {
            // If regular order fails, attempt to fetch individual order data
            try {
                java.net.http.HttpResponse<String> individualOrderResponse = request(
                        RequestUtil.DATABASE_ORDER_SERVICE_URI,
                        "/individual/" + orderId,
                        HttpMethod.GET,
                        null);

                // Parse response as IndividualOrderDTO
                return JacksonConfig.configureObjectMapper().readValue(individualOrderResponse.body(), IndividualOrderDTO.class);
            } catch (Exception innerException) {
                // Both fetching attempts failed
                return null;
            }
        }
    }
}
