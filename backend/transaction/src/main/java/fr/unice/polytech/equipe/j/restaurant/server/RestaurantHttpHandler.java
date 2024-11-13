package fr.unice.polytech.equipe.j.restaurant.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.unice.polytech.equipe.j.restaurant.backend.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.backend.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.restaurant.backend.menu.MenuItem;
import fr.unice.polytech.equipe.j.serverutils.http.HttpUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code RestaurantHttpHandler} is an implementation of the {@link HttpHandler} interface,
 * responsible for handling HTTP requests related to restaurant data and operations.
 * <p>
 * This handler processes requests made to the /restaurants route and interacts with
 * the restaurant management service to provide restaurant-related data. The handler
 * can handle various types of HTTP requests such as GET, POST, etc., depending on
 * the implementation.
 * </p>
 */
public class RestaurantHttpHandler implements HttpHandler {
    // User Routes
    private static final String RESTAURANT_ID_REGEX = "/restaurants/([^/]+)";
    private static final String MENU_ITEM_REGEX = "/restaurants/([^/]+)/menu";
    private static final String FOOD_TYPE_REGEX = "/restaurants/foodType/([^/]+)";
    private static final String DELIVERY_TIME_REGEX = "/restaurants/deliveryTime/(\\d+)/(\\d+)";

    // Management routes
    private static final String MANAGEMENT_ITEM_REGEX = "/restaurants/manage/menu/item/([^/]+)";
    private static final String DELETE_ITEM_REGEX = "/restaurants/manage/menu/item/([^/]+)";
    private static final String UPDATE_PREP_TIME_REGEX = "/restaurants/manage/menu/item/prepTime";
    private static final String UPDATE_PRICE_REGEX = "/restaurants/manage/menu/item/price";
    private static final String UPDATE_OPENING_HOUR_REGEX = "/restaurants/manage/openingHour";
    private static final String UPDATE_CLOSING_HOUR_REGEX = "/restaurants/manage/closingHour";
    private static final String UPDATE_EMPLOYEES_REGEX = "/restaurants/manage/employee";
    private static final String UPDATE_STRATEGY_REGEX = "/restaurants/manage/strategy";

    /**
     * Handles HTTP requests and generates an appropriate response based on the requested path and method.
     * The method checks the request method and URI to determine which route to handle, including
     * routes for retrieving restaurant information and managing restaurant operations.
     *
     * <p>
     * The following routes are supported:
     * <ul>
     *   <li><b>GET /restaurants</b>: Retrieve a list of all restaurants.</li>
     *   <li><b>GET /restaurants/{restaurantId}</b>: Retrieve details for a specific restaurant by its ID.</li>
     *   <li><b>GET /restaurants/{restaurantId}/menu</b>: Retrieve the menu for a specific restaurant.</li>
     *   <li><b>GET /restaurants/foodType/{foodType}</b>: Retrieve a list of restaurants offering a specific food type.</li>
     *   <li><b>GET /restaurants/deliveryTime/{hour}/{minute}</b>: Retrieve restaurants based on a specific delivery time.</li>
     *   <li><b>POST /restaurants/manage/menu/item</b>: Add a new menu item to a restaurant's menu.</li>
     *   <li><b>DELETE /restaurants/manage/menu/item</b>: Remove a menu item from a restaurant's menu.</li>
     *   <li><b>POST /restaurants/manage/menu/item/prepTime</b>: Update the preparation time for a menu item.</li>
     *   <li><b>POST /restaurants/manage/menu/item/price</b>: Update the price of a menu item.</li>
     *   <li><b>POST /restaurants/manage/openingHour</b>: Update the opening hour for a restaurant.</li>
     *   <li><b>POST /restaurants/manage/closingHour</b>: Update the closing hour for a restaurant.</li>
     *   <li><b>POST /restaurants/manage/employee</b>: Update the number of employees for a restaurant.</li>
     *   <li><b>POST /restaurants/manage/strategy</b>: Update the operational strategy for a restaurant.</li>
     * </ul>
     * </p>
     *
     * @param exchange the HTTP exchange containing the request and response
     * @throws NullPointerException if the exchange is {@code null}
     * @throws IOException          if an I/O error occurs while handling the request
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // CORS
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Accept, X-Requested-With, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization");

        String method = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();

        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("Received " + method + " request for " + requestPath);

        // Handling GET /restaurants
        if ("GET".equals(method) && "/restaurants".equals(requestPath)) {
            System.out.println("Handling GET /restaurants");
            handleGetRestaurants(exchange, objectMapper);
        }
        // Handling GET /restaurants/{restaurantId}
        else if ("GET".equals(method) && requestPath.matches(RESTAURANT_ID_REGEX)) {
            handleGetRestaurantById(exchange, requestPath, objectMapper);
        }
        // Handling GET /restaurants/{restaurantId}/menu
        else if ("GET".equals(method) && requestPath.matches(MENU_ITEM_REGEX)) {
            handleGetRestaurantMenu(exchange, requestPath, objectMapper);
        }
        // Handling GET /restaurants/foodType/{foodType}
        else if ("GET".equals(method) && requestPath.matches(FOOD_TYPE_REGEX)) {
            handleGetRestaurantsByFoodType(exchange, requestPath, objectMapper);
        }
        // Handling GET /restaurants/deliveryTime/{hour}/{minute}
        else if ("GET".equals(method) && requestPath.matches(DELIVERY_TIME_REGEX)) {
            handleGetRestaurantsByDeliveryTime(exchange, requestPath, objectMapper);
        }
        // POST /restaurants/manage/menu/item/
        else if ("POST".equals(method) && requestPath.matches(MANAGEMENT_ITEM_REGEX)) {
            handleAddMenuItem(exchange);
        }
        // DELETE /restaurants/manage//menu/item/}
        else if ("DELETE".equals(method) && requestPath.matches(DELETE_ITEM_REGEX)) {
            handleDeleteMenuItem(exchange);
        }
        // POST /restaurants/manage/menu/item/prepTime
        else if ("POST".equals(method) && requestPath.equals(UPDATE_PREP_TIME_REGEX)) {
            handleUpdatePrepTime(exchange);
        }
        // POST /restaurants/manage/menu/item/price
        else if ("POST".equals(method) && requestPath.equals(UPDATE_PRICE_REGEX)) {
            handleUpdatePrice(exchange);
        }
        // POST /restaurants/manage/openingHour
        else if ("POST".equals(method) && requestPath.equals(UPDATE_OPENING_HOUR_REGEX)) {
            handleUpdateOpeningHour(exchange);
        }
        // POST /restaurants/manage/closingHour
        else if ("POST".equals(method) && requestPath.equals(UPDATE_CLOSING_HOUR_REGEX)) {
            handleUpdateClosingHour(exchange);
        }
        // POST /restaurants/manage/employee
        else if ("POST".equals(method) && requestPath.equals(UPDATE_EMPLOYEES_REGEX)) {
            handleUpdateEmployees(exchange);
        }
        // POST /restaurants/manage/strategy
        else if ("POST".equals(method) && requestPath.equals(UPDATE_STRATEGY_REGEX)) {
            handleUpdateStrategy(exchange);
        } else {
            exchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, -1);
        }
    }

    /**
     * Handles the request to update a restaurant's operational strategy.
     * The request body should contain the following parameters:
     *
     * <ul>
     *   <li>userUuid: The UUID of the user attempting the management action.</li>
     *   <li>restaurantUuid: The UUID of the restaurant on which the action is performed.</li>
     *   <li>strategyName: The name of the strategy to apply to the restaurant.</li>
     * </ul>
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Validates the provided UUIDs (for the user and restaurant).</li>
     *   <li>Checks if the restaurant exists.</li>
     *   <li>Updates the restaurant's strategy (currently a placeholder implementation).</li>
     * </ul>
     *
     * <p>If the restaurant is found, the strategy is updated, and a success message is sent.
     * If not, a "Not Found" response is returned.</p>
     *
     * @param exchange the HTTP exchange containing the request and response data
     * @throws IOException if an I/O error occurs while handling the request
     */
    private void handleUpdateStrategy(HttpExchange exchange) throws IOException {
        JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        UUID userUuid = UUID.fromString(requestBody.getString("userUuid"));
        UUID restaurantUuid = UUID.fromString(requestBody.getString("restaurantUuid"));
        String strategy = requestBody.getString("strategy");

        // TODO: Check UUID is Manager

        IRestaurant restaurant = RestaurantServiceManager.getInstance().searchByUUID(restaurantUuid.toString());
        if (restaurant != null) {
            // TODO: Enum for strategies name and switch
            // TODO : restaurant.setOrderPriceStrategy(strategy);
            sendResponse(exchange, HttpUtils.OK_CODE, new JSONObject().put("message", "Strategy updated (not implemented)"));
        } else {
            sendResponse(exchange, HttpUtils.NOT_FOUND_CODE, new JSONObject().put("error", "Restaurant not found"));
        }
    }

    /**
     * Handles the request to update the number of employees for a specific restaurant.
     * The request body should contain the following parameters:
     *
     * <ul>
     *   <li>userUuid: The UUID of the user attempting the management action.</li>
     *   <li>restaurantUuid: The UUID of the restaurant on which the action is performed.</li>
     *   <li>numberOfEmployees: The new number of employees to set for the restaurant.</li>
     * </ul>
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Validates the provided UUIDs (for the user and restaurant).</li>
     *   <li>Checks if the restaurant exists.</li>
     *   <li>Updates the restaurant's employee count based on the provided number.</li>
     * </ul>
     *
     * <p>If the restaurant is found, the number of employees is updated and a success message is sent.
     * If not, a "Not Found" response is returned.</p>
     *
     * @param exchange the HTTP exchange containing the request and response data
     * @throws IOException if an I/O error occurs while handling the request
     */
    private void handleUpdateEmployees(HttpExchange exchange) throws IOException {
        JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        UUID userUuid = UUID.fromString(requestBody.getString("userUuid"));
        UUID restaurantUuid = UUID.fromString(requestBody.getString("restaurantUuid"));
        int numberOfEmployees = requestBody.getInt("numberOfEmployees");

        // TODO: Check UUID is Manager

        IRestaurant restaurant = RestaurantServiceManager.getInstance().searchByUUID(restaurantUuid.toString());
        if (restaurant != null) {
            restaurant.setNumberOfPersonnel(restaurant.getSlots().get(0), numberOfEmployees);
            sendResponse(exchange, HttpUtils.OK_CODE, new JSONObject().put("message", "Number of employees updated"));
        } else {
            sendResponse(exchange, HttpUtils.NOT_FOUND_CODE, new JSONObject().put("error", "Restaurant not found"));
        }
    }

    /**
     * Handles the request to update the closing hour for a specific restaurant.
     * The request body should contain the following parameters:
     *
     * <ul>
     *   <li>userUuid: The UUID of the user attempting the management action.</li>
     *   <li>restaurantUuid: The UUID of the restaurant on which the action is performed.</li>
     *   <li>hour: The new closing hour (24-hour format).</li>
     *   <li>minute: The new closing minute (0-59).</li>
     * </ul>
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Validates the provided UUIDs (for the user and restaurant).</li>
     *   <li>Checks if the restaurant exists.</li>
     *   <li>Updates the restaurant's closing hour based on the provided hour and minute.</li>
     * </ul>
     *
     * <p>If the restaurant is found, the closing time is updated and a success message is sent.
     * If not, a "Not Found" response is returned.</p>
     *
     * @param exchange the HTTP exchange containing the request and response data
     * @throws IOException if an I/O error occurs while handling the request
     */
    private void handleUpdateClosingHour(HttpExchange exchange) throws IOException {
        JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        UUID userUuid = UUID.fromString(requestBody.getString("userUuid"));
        UUID restaurantUuid = UUID.fromString(requestBody.getString("restaurantUuid"));
        int hour = requestBody.getInt("hour");
        int minute = requestBody.getInt("minute");

        // TODO: Check UUID is Manager

        IRestaurant restaurant = RestaurantServiceManager.getInstance().searchByUUID(restaurantUuid.toString());
        if (restaurant != null) {
            restaurant.setClosingTime(LocalDateTime.now().withHour(hour).withMinute(minute));
            sendResponse(exchange, HttpUtils.OK_CODE, new JSONObject().put("message", "Closing hour updated"));
        } else {
            sendResponse(exchange, HttpUtils.NOT_FOUND_CODE, new JSONObject().put("error", "Restaurant not found"));
        }
    }

    /**
     * Handles the request to update the opening hour for a specific restaurant.
     * The request body should contain the following parameters:
     *
     * <ul>
     *   <li>userUuid: The UUID of the user attempting the management action.</li>
     *   <li>restaurantUuid: The UUID of the restaurant on which the action is performed.</li>
     *   <li>hour: The new opening hour (24-hour format).</li>
     *   <li>minute: The new opening minute (0-59).</li>
     * </ul>
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Validates the provided UUIDs (for the user and restaurant).</li>
     *   <li>Checks if the restaurant exists.</li>
     *   <li>Updates the restaurant's opening hour based on the provided hour and minute.</li>
     * </ul>
     *
     * <p>If the restaurant is found, the opening time is updated and a success message is sent.
     * If not, a "Not Found" response is returned.</p>
     *
     * @param exchange the HTTP exchange containing the request and response data
     * @throws IOException if an I/O error occurs while handling the request
     */
    private void handleUpdateOpeningHour(HttpExchange exchange) throws IOException {
        JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        UUID userUuid = UUID.fromString(requestBody.getString("userUuid"));
        UUID restaurantUuid = UUID.fromString(requestBody.getString("restaurantUuid"));
        int hour = requestBody.getInt("hour");
        int minute = requestBody.getInt("minute");

        // TODO: Check UUID is Manager

        IRestaurant restaurant = RestaurantServiceManager.getInstance().searchByUUID(restaurantUuid.toString());
        if (restaurant != null) {
            restaurant.setOpeningTime(LocalDateTime.now().withHour(hour).withMinute(minute));
            sendResponse(exchange, HttpUtils.OK_CODE, new JSONObject().put("message", "Opening hour updated"));
        } else {
            sendResponse(exchange, HttpUtils.NOT_FOUND_CODE, new JSONObject().put("error", "Restaurant not found"));
        }
    }

    /**
     * Handles the request to add a new menu item to a restaurant's menu.
     * The request body should contain the following parameters:
     *
     * <ul>
     *   <li>userUuid: The UUID of the user attempting the management action.</li>
     *   <li>restaurantUuid: The UUID of the restaurant on which the action is performed.</li>
     *   <li>item: The details of the menu item to add, which should be a JSON object containing:
     *       <ul>
     *         <li>name: The name of the menu item.</li>
     *         <li>prepTime: The preparation time for the item in minutes.</li>
     *         <li>price: The price of the menu item.</li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Validates the provided UUIDs (for the user and restaurant).</li>
     *   <li>Checks if the restaurant exists.</li>
     *   <li>Creates a new `MenuItem` and adds it to the restaurant's menu.</li>
     * </ul>
     *
     * <p>If the restaurant is found, the item is added successfully, and a success message is sent.
     * If not, a "Not Found" response is returned.</p>
     *
     * @param exchange the HTTP exchange containing the request and response data
     * @throws IOException if an I/O error occurs while handling the request
     */
    private void handleAddMenuItem(HttpExchange exchange) throws IOException {
        JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        UUID userUuid = UUID.fromString(requestBody.getString("userUuid"));
        UUID restaurantUuid = UUID.fromString(requestBody.getString("restaurantUuid"));
        JSONObject itemDetails = requestBody.getJSONObject("item"); // Item details (name, prepTime, price, etc.)
        String name = itemDetails.getString("name");
        int prepTime = itemDetails.getInt("prepTime");
        double price = itemDetails.getDouble("price");

        // TODO: Check UUID is Manager

        MenuItem newItem = new MenuItem(name, prepTime, price);

        IRestaurant restaurant = RestaurantServiceManager.getInstance().searchByUUID(restaurantUuid.toString());
        if (restaurant != null) {
            restaurant.getMenu().getItems().add(newItem);
            sendResponse(exchange, HttpUtils.OK_CODE, new JSONObject().put("message", "Item added successfully"));
        } else {
            sendResponse(exchange, HttpUtils.NOT_FOUND_CODE, new JSONObject().put("error", "Restaurant not found"));
        }
    }

    /**
     * Handles the request to delete a menu item from a restaurant's menu.
     * The request body should contain the following parameters:
     *
     * <ul>
     *   <li>userUuid: The UUID of the user attempting the management action.</li>
     *   <li>restaurantUuid: The UUID of the restaurant on which the action is performed.</li>
     *   <li>itemName: The name of the item to remove from the menu.</li>
     * </ul>
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Validates the provided UUIDs (for the user and restaurant).</li>
     *   <li>Checks if the restaurant exists.</li>
     *   <li>Removes the menu item with the specified name from the restaurant's menu.</li>
     * </ul>
     *
     * <p>If the item is successfully removed, a success message is sent. If the item is not found,
     * a "Not Found" response is returned. If the restaurant is not found, a "Not Found" response is sent.</p>
     *
     * @param exchange the HTTP exchange containing the request and response data
     * @throws IOException if an I/O error occurs while handling the request
     */
    private void handleDeleteMenuItem(HttpExchange exchange) throws IOException {
        JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        UUID userUuid = UUID.fromString(requestBody.getString("userUuid"));
        UUID restaurantUuid = UUID.fromString(requestBody.getString("restaurantUuid"));
        String itemName = requestBody.getString("itemName");

        // TODO: Check UUID is Manager

        IRestaurant restaurant = RestaurantServiceManager.getInstance().searchByUUID(restaurantUuid.toString());
        if (restaurant != null) {
            boolean removed = restaurant.getMenu().getItems().removeIf(item -> item.getName().equals(itemName));
            if (removed) {
                sendResponse(exchange, HttpUtils.OK_CODE, new JSONObject().put("message", "Item removed successfully"));
            } else {
                sendResponse(exchange, HttpUtils.NOT_FOUND_CODE, new JSONObject().put("error", "Item not found"));
            }
        }
    }

    /**
     * Handles the request to update the preparation time for a specific menu item.
     * The request body should contain the following parameters:
     *
     * <ul>
     *   <li>userUuid: The UUID of the user attempting the management action.</li>
     *   <li>restaurantUuid: The UUID of the restaurant on which the action is performed.</li>
     *   <li>itemName: The name of the menu item whose preparation time is being updated.</li>
     *   <li>prepTime: The new preparation time for the item in seconds.</li>
     * </ul>
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Validates the provided UUIDs (for the user and restaurant).</li>
     *   <li>Checks if the restaurant exists.</li>
     *   <li>Finds the menu item by name and updates its preparation time.</li>
     * </ul>
     *
     * <p>If the item is found, the preparation time is updated, and a success message is returned.
     * If the item is not found, a "Not Found" response is returned.</p>
     *
     * @param exchange the HTTP exchange containing the request and response data
     * @throws IOException if an I/O error occurs while handling the request
     */
    private void handleUpdatePrepTime(HttpExchange exchange) throws IOException {
        JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        UUID userUuid = UUID.fromString(requestBody.getString("userUuid"));
        UUID restaurantUuid = UUID.fromString(requestBody.getString("restaurantUuid"));
        String itemName = requestBody.getString("itemName");
        int prepTime = requestBody.getInt("prepTime");

        // TODO: Check UUID is Manager

        IRestaurant restaurant = RestaurantServiceManager.getInstance().searchByUUID(restaurantUuid.toString());
        if (restaurant != null) {
            MenuItem updated = restaurant.getMenu().findItemByName(itemName);
            if (updated != null) {
                updated.setPrepTime(prepTime);
                sendResponse(exchange, HttpUtils.OK_CODE, new JSONObject().put("message", "Prep time updated"));
            } else {
                sendResponse(exchange, HttpUtils.NOT_FOUND_CODE, new JSONObject().put("error", "Item not found"));
            }
        }
    }

    /**
     * Handles the request to update the price of a specific menu item.
     * The request body should contain the following parameters:
     *
     * <ul>
     *   <li>userUuid: The UUID of the user attempting the management action.</li>
     *   <li>restaurantUuid: The UUID of the restaurant on which the action is performed.</li>
     *   <li>itemName: The name of the menu item whose price is being updated.</li>
     *   <li>price: The new price for the item in euros.</li>
     * </ul>
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Validates the provided UUIDs (for the user and restaurant).</li>
     *   <li>Checks if the restaurant exists.</li>
     *   <li>Finds the menu item by name and updates its price.</li>
     * </ul>
     *
     * <p>If the item is found, the price is updated, and a success message is returned.
     * If the item is not found, a "Not Found" response is returned.</p>
     *
     * @param exchange the HTTP exchange containing the request and response data
     * @throws IOException if an I/O error occurs while handling the request
     */
    private void handleUpdatePrice(HttpExchange exchange) throws IOException {
        JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        UUID userUuid = UUID.fromString(requestBody.getString("userUuid"));
        UUID restaurantUuid = UUID.fromString(requestBody.getString("restaurantUuid"));
        String itemName = requestBody.getString("itemName");
        int price = requestBody.getInt("price");

        // TODO: Check UUID is Manager

        IRestaurant restaurant = RestaurantServiceManager.getInstance().searchByUUID(restaurantUuid.toString());
        if (restaurant != null) {
            MenuItem updated = restaurant.getMenu().findItemByName(itemName);
            if (updated != null) {
                updated.setPrice(price);
                sendResponse(exchange, HttpUtils.OK_CODE, new JSONObject().put("message", "Price updated"));
            } else {
                sendResponse(exchange, HttpUtils.NOT_FOUND_CODE, new JSONObject().put("error", "Item not found"));
            }
        }
    }

    /**
     * Handles the request to retrieve a list of all restaurants.
     * The response will be a JSON array containing the details of all restaurants.
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Retrieves the list of all restaurants.</li>
     *   <li>Serializes the list of restaurants into a JSON format using ObjectMapper.</li>
     * </ul>
     *
     * @param exchange     the HTTP exchange containing the request and response data
     * @param objectMapper the object mapper to serialize the response
     * @throws IOException if an I/O error occurs while writing the response
     */
    private void handleGetRestaurants(HttpExchange exchange, ObjectMapper objectMapper) throws IOException {
        JSONArray jsonArray = new JSONArray();
        System.out.println("Getting all restaurants");
        List<IRestaurant> restaurants = RestaurantServiceManager.getInstance().getRestaurants();
        System.out.println("Found " + restaurants.size() + " restaurants");
        for (IRestaurant restaurant : restaurants) {
            System.out.println("Restaurant: " + restaurant.getRestaurantName());
            jsonArray.put(new JSONObject(objectMapper.writeValueAsString(restaurant)));
        }
        System.out.println("Sending response");
        sendResponse(exchange, HttpUtils.OK_CODE, new JSONObject().put("restaurants", jsonArray));
        System.out.println("Response sent");
    }

    /**
     * Handles the request to retrieve the details of a specific restaurant by its ID.
     * The request path must contain the restaurant ID as a parameter.
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Extracts the restaurant ID from the request path.</li>
     *   <li>Retrieves the restaurant by its ID.</li>
     *   <li>Serializes the restaurant details into a JSON format using ObjectMapper.</li>
     * </ul>
     *
     * @param exchange     the HTTP exchange containing the request and response data
     * @param requestPath  the request path containing the restaurant ID
     * @param objectMapper the object mapper to serialize the response
     * @throws IOException if an I/O error occurs while writing the response
     */
    private void handleGetRestaurantById(HttpExchange exchange, String requestPath, ObjectMapper objectMapper) throws IOException {
        Matcher matcher = Pattern.compile(RESTAURANT_ID_REGEX).matcher(requestPath);
        if (matcher.find()) {
            String restaurantId = matcher.group(1);
            IRestaurant restaurant = RestaurantServiceManager.getInstance().searchByUUID(restaurantId);
            if (restaurant == null) {
                sendResponse(exchange, HttpUtils.NOT_FOUND_CODE, new JSONObject());
            } else {
                sendResponse(exchange, HttpUtils.OK_CODE, new JSONObject(objectMapper.writeValueAsString(restaurant)));
            }
        }
    }

    /**
     * Handles the request to retrieve the menu for a specific restaurant.
     * The request path must contain the restaurant ID as a parameter.
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Extracts the restaurant ID from the request path.</li>
     *   <li>Retrieves the restaurant by its ID and fetches its menu.</li>
     *   <li>Serializes the menu into a JSON format using ObjectMapper.</li>
     * </ul>
     *
     * @param exchange     the HTTP exchange containing the request and response data
     * @param requestPath  the request path containing the restaurant ID
     * @param objectMapper the object mapper to serialize the response
     * @throws IOException if an I/O error occurs while writing the response
     */
    private void handleGetRestaurantMenu(HttpExchange exchange, String requestPath, ObjectMapper objectMapper) throws IOException {
        Matcher matcher = Pattern.compile(MENU_ITEM_REGEX).matcher(requestPath);
        if (matcher.find()) {
            String restaurantId = matcher.group(1);
            IRestaurant restaurant = RestaurantServiceManager.getInstance().searchByUUID(restaurantId);
            if (restaurant == null) {
                sendResponse(exchange, HttpUtils.NOT_FOUND_CODE, new JSONObject());
            } else {
                sendResponse(exchange, HttpUtils.OK_CODE, new JSONObject(objectMapper.writeValueAsString(restaurant.getMenu())));
            }
        }
    }

    /**
     * Handles the request to retrieve a list of restaurants that serve a specific type of food.
     * The request path must contain the food type as a parameter.
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Extracts the food type from the request path.</li>
     *   <li>Retrieves a list of restaurants that serve the given food type.</li>
     *   <li>Serializes the list of restaurants into a JSON format using ObjectMapper.</li>
     * </ul>
     *
     * @param exchange     the HTTP exchange containing the request and response data
     * @param requestPath  the request path containing the food type
     * @param objectMapper the object mapper to serialize the response
     * @throws IOException if an I/O error occurs while writing the response
     */
    private void handleGetRestaurantsByFoodType(HttpExchange exchange, String requestPath, ObjectMapper objectMapper) throws IOException {
        Matcher matcher = Pattern.compile(FOOD_TYPE_REGEX).matcher(requestPath);
        if (matcher.find()) {
            String foodType = matcher.group(1);
            sendResponse(exchange, HttpUtils.OK_CODE, new JSONObject(objectMapper.writeValueAsString(RestaurantServiceManager.getInstance().searchByTypeOfFood(foodType))));
        }
    }

    /**
     * Handles the request to retrieve a list of restaurants that can deliver within the specified time.
     * The request path must contain the delivery time (hour and minute) as parameters.
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Extracts the hour and minute from the request path.</li>
     *   <li>Constructs a LocalDateTime object based on the provided delivery time.</li>
     *   <li>Retrieves a list of restaurants that can deliver at the given time.</li>
     *   <li>Serializes the list of restaurants into a JSON format using ObjectMapper.</li>
     * </ul>
     *
     * @param exchange     the HTTP exchange containing the request and response data
     * @param requestPath  the request path containing the hour and minute of the delivery time
     * @param objectMapper the object mapper to serialize the response
     * @throws IOException if an I/O error occurs while writing the response
     */
    private void handleGetRestaurantsByDeliveryTime(HttpExchange exchange, String requestPath, ObjectMapper objectMapper) throws IOException {
        Matcher matcher = Pattern.compile(DELIVERY_TIME_REGEX).matcher(requestPath);
        if (matcher.find()) {
            int hour = Integer.parseInt(matcher.group(1));
            int minute = Integer.parseInt(matcher.group(2));

            LocalDateTime deliveryTime = LocalDateTime.now().withHour(hour).withMinute(minute);

            sendResponse(exchange, HttpUtils.OK_CODE,
                    new JSONObject(objectMapper.writeValueAsString(RestaurantServiceManager.getInstance().searchRestaurantByDeliveryTime(Optional.of(deliveryTime)))));
        } else {
            sendResponse(exchange, HttpUtils.BAD_REQUEST_CODE, new JSONObject().put("error", "Invalid delivery time format"));
        }
    }

    /**
     * Sends an HTTP response to the client with a specified status code and JSON payload.
     * <p>
     * This method sets the response headers to indicate the content type is JSON and then
     * sends the provided response body as a JSON string to the client.
     * </p>
     *
     * @param exchange     The HTTP exchange object representing the current request-response cycle.
     *                     It contains the request and response data, including headers and body.
     * @param statusCode   The HTTP status code to be sent as part of the response (e.g., 200 for OK, 404 for Not Found).
     * @param responseText The JSON object that will be serialized and sent as the response body.
     *                     It contains the data that will be returned to the client.
     * @throws IOException If an I/O error occurs while writing the response to the output stream.
     */
    private void sendResponse(HttpExchange exchange, int statusCode, JSONObject responseText) throws IOException {
        exchange.getResponseHeaders().set(HttpUtils.CONTENT_TYPE, HttpUtils.APPLICATION_JSON);
        exchange.sendResponseHeaders(statusCode, responseText.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseText.toString().getBytes());
        }
    }
}
