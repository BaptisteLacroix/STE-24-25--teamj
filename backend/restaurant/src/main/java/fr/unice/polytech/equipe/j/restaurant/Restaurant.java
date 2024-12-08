package fr.unice.polytech.equipe.j.restaurant;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.CustomHttpResponse;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.JacksonConfig;
import fr.unice.polytech.equipe.j.RequestUtil;
import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.dto.IndividualOrderDTO;
import fr.unice.polytech.equipe.j.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.dto.OrderDTO;
import fr.unice.polytech.equipe.j.dto.OrderStatus;
import fr.unice.polytech.equipe.j.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.mapper.DTOMapper;
import fr.unice.polytech.equipe.j.menu.Menu;
import fr.unice.polytech.equipe.j.menu.MenuItem;
import fr.unice.polytech.equipe.j.orderpricestrategy.FreeItemFotNItemsOrderPriceStrategy;
import fr.unice.polytech.equipe.j.orderpricestrategy.OrderPrice;
import fr.unice.polytech.equipe.j.orderpricestrategy.OrderPriceStrategy;
import fr.unice.polytech.equipe.j.slot.Slot;
import lombok.Setter;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static fr.unice.polytech.equipe.j.RequestUtil.request;

public class Restaurant implements IRestaurant {
    private final UUID restaurantId;
    private final String restaurantName;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private List<Slot> slots;
    @Setter
    private Menu menu;
    private OrderPriceStrategy orderPriceStrategy;
    private final List<OrderDTO> ordersHistory = new ArrayList<>();
    private Map<Slot, Set<OrderDTO>> pendingOrders = new LinkedHashMap<>();


    public Restaurant(UUID restaurantId, String name, LocalDateTime openingTime, LocalDateTime closingTime, Menu menu, List<Slot> slots, Map<Slot, Set<OrderDTO>> pendingOrders) {
        this(restaurantId, name, openingTime, closingTime, menu, new FreeItemFotNItemsOrderPriceStrategy(8), slots, pendingOrders);
    }

    private Restaurant(UUID restaurantId, String name, LocalDateTime openingTime, LocalDateTime closingTime, Menu menu, OrderPriceStrategy strategy, List<Slot> slots, Map<Slot, Set<OrderDTO>> pendingOrders) {
        this.restaurantId = restaurantId;
        this.restaurantName = name;
        if (closingTime != null && closingTime.isBefore(openingTime)) {
            throw new IllegalArgumentException("Closing time cannot be before opening time.");
        }
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.menu = menu;
        this.orderPriceStrategy = strategy;
        if (slots == null) {
            generateSlots();
        } else {
            this.slots = slots;
            // Initialize the pending orders for each slot with its corresponding set of orders
            Map<Slot, Set<OrderDTO>> newPendingOrders = new LinkedHashMap<>();
            for (Slot slot : slots) {
                newPendingOrders.put(slot, new LinkedHashSet<>(pendingOrders.getOrDefault(slot, new HashSet<>())));
            }
            this.pendingOrders = newPendingOrders;
        }
    }

    /**
     * For every 30 minutes, generate a slot with a duration of 30 minutes. The number of slots is calculated based on the opening and closing time.
     */
    private void generateSlots() {
        if (openingTime == null || closingTime == null) {
            return;
        }
        slots = new ArrayList<>();
        LocalDateTime currentTime = openingTime;
        while (currentTime.isBefore(closingTime)) {
            Slot slot = new Slot(UUID.randomUUID(), currentTime, 0);
            slots.add(slot);
            pendingOrders.put(slot, new LinkedHashSet<>());
            currentTime = currentTime.plusMinutes(30);
        }
    }

    /**
     * Calculate the average preparation time for the restaurant's menu items.
     *
     * @return the average preparation time in seconds.
     */
    public int calculateAveragePreparationTime() {
        int totalPrepTime = 0;
        List<MenuItem> items = menu.getItems();

        for (MenuItem item : items) {
            totalPrepTime += item.getPrepTime();
        }

        return totalPrepTime / items.size();
    }

    /**
     * Return the maximum capacity of the restaurant based on the average preparation time of a command.
     *
     * @return the maximum capacity of the restaurant.
     */
    public int getMaxOrdersForSlot(Slot slot) {
        int averagePrepTime = calculateAveragePreparationTime();
        int slotDurationInSeconds = (int) slot.getDurationTime().getSeconds() * slot.getNumberOfPersonnel();
        double maxOrders = (double) slotDurationInSeconds / averagePrepTime;

        return (int) Math.floor(maxOrders + 0.5);
    }

    /**
     * Check if the restaurant has enough capacity to accept a new order.
     *
     * @return true if there is capacity, false otherwise.
     */
    @Override
    public boolean isSlotCapacityAvailable() {
        // Check if there is a slot available with enough capacity to prepare the order
        // and the number of orders is less than the maximum (EX2)
        return slots.stream().anyMatch(slot -> slot.getAvailableCapacity() > 0 && this.pendingOrders.get(slot).size() < this.getMaxOrdersForSlot(slot));
    }

    /**
     * Add an item to an order, either individual or group.
     *
     * @param orderDTO     the order to which the item is added
     * @param menuItem     the menu item being added
     * @param deliveryTime the delivery time for the order (optional)
     * @throws IllegalArgumentException if the item is not available or cannot be prepared in time
     */
    @Override
    public HttpResponse<String> addItemToOrder(OrderDTO orderDTO, MenuItem menuItem, LocalDateTime deliveryTime) {
        try {
            Slot availableSlot = slots.stream()
                    .filter(slot -> slot.updateSlotCapacity(menuItem))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Cannot add item to order, no slot available."));
            this.pendingOrders.get(availableSlot).add(orderDTO);
            orderDTO.setSlot(DTOMapper.toSlotDTO(availableSlot));
            // add the new item to the list of items in the order
            List<MenuItemDTO> newItems = new ArrayList<>(orderDTO.getItems());
            newItems.add(DTOMapper.toMenuItemDTO(menuItem));
            orderDTO.setItems(newItems);
            ObjectMapper mapper = JacksonConfig.configureObjectMapper();

            // Update the restaurant in the database using the update route
            HttpResponse<String> response = request(
                    RequestUtil.DATABASE_RESTAURANT_SERVICE_URI,
                    "/update",
                    HttpMethod.PUT,
                    mapper.writeValueAsString(DTOMapper.toRestaurantDTO(this))
            );
            if (response.statusCode() != HttpCode.HTTP_201.getCode() && response.statusCode() != HttpCode.HTTP_200.getCode()) {
                return response;
            }
            if (orderDTO instanceof IndividualOrderDTO) {
                response = request(
                        RequestUtil.DATABASE_ORDER_SERVICE_URI,
                        "/individual/update",
                        HttpMethod.PUT,
                        mapper.writeValueAsString(orderDTO)
                );
            } else {
                response = request(
                        RequestUtil.DATABASE_ORDER_SERVICE_URI,
                        "/update",
                        HttpMethod.PUT,
                        mapper.writeValueAsString(orderDTO)
                );
            }
            // If the response is not successful, abort the restaurant update operation
            if (response.statusCode() != HttpCode.HTTP_201.getCode() && response.statusCode() != HttpCode.HTTP_200.getCode()) {
                this.pendingOrders.get(availableSlot).remove(orderDTO);
                availableSlot.addCapacity(-menuItem.getPrepTime());
                request(
                        RequestUtil.DATABASE_RESTAURANT_SERVICE_URI,
                        "/update",
                        HttpMethod.PUT,
                        mapper.writeValueAsString(DTOMapper.toRestaurantDTO(this))
                );
                return response;
            }
            return new CustomHttpResponse(HttpCode.HTTP_200.getCode(), "Item added to order.");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Cancel an order and free up capacity.
     *
     * @param orderDTO the order to cancel
     */
    @Override
    public HttpResponse<String> cancelOrder(OrderDTO orderDTO, LocalDateTime deliveryTime) {
        try {
            // Check if the order exists and matches the UUID
            if (pendingOrders.values().stream()
                    .map(Set::stream)
                    .noneMatch(stream -> stream.anyMatch(o -> o.getId().equals(orderDTO.getId())))) {
                return new CustomHttpResponse(HttpCode.HTTP_404.getCode(), "Order not found.");
            }

            // Find all slots that have the order inside
            Set<Slot> slots = pendingOrders.entrySet().stream()
                    .filter(entry -> entry.getValue().stream().anyMatch(o -> o.getId().equals(orderDTO.getId())))
                    .map(Map.Entry::getKey)
                    .collect(HashSet::new, HashSet::add, HashSet::addAll);

            // Remove the order from each slot and adjust the capacity
            for (Slot slot : slots) {
                for (MenuItemDTO item : orderDTO.getItems()) {
                    slot.addCapacity(-item.getPrepTime()); // Adjust capacity if needed
                }
                pendingOrders.get(slot).removeIf(o -> o.getId().equals(orderDTO.getId()));
            }
            // Update the restaurant in the database using the update route
            ObjectMapper mapper = JacksonConfig.configureObjectMapper();
            HttpResponse<String> restaurantUpdateResponse = request(
                    RequestUtil.DATABASE_RESTAURANT_SERVICE_URI,
                    "/update",
                    HttpMethod.PUT,
                    mapper.writeValueAsString(DTOMapper.toRestaurantDTO(this))
            );

            if (restaurantUpdateResponse.statusCode() != HttpCode.HTTP_201.getCode()) {
                System.out.println("Error while updating the restaurant.");
                return restaurantUpdateResponse;
            }
            orderDTO.setStatus(OrderStatus.CANCELLED);
            orderDTO.setSlot(null);
            HttpResponse<String> orderUpdateResponse;
            if (orderDTO instanceof IndividualOrderDTO) {
                orderUpdateResponse = request(
                        RequestUtil.DATABASE_ORDER_SERVICE_URI,
                        "/individual/update",
                        HttpMethod.PUT,
                        mapper.writeValueAsString(orderDTO)
                );
            } else {
                orderUpdateResponse = request(
                        RequestUtil.DATABASE_ORDER_SERVICE_URI,
                        "/update",
                        HttpMethod.PUT,
                        mapper.writeValueAsString(orderDTO)
                );
            }

            if (orderUpdateResponse.statusCode() != HttpCode.HTTP_201.getCode()) {
                System.out.println("Error while updating the order status.");
                return orderUpdateResponse;
            }
            return new CustomHttpResponse(HttpCode.HTTP_200.getCode(), "Order canceled successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomHttpResponse(HttpCode.HTTP_500.getCode(), "An error occurred while canceling the order.");
        }
    }


    /**
     * Check if an item is available
     *
     * @param item The item to check
     * @return true if the item is available, false otherwise
     */
    public boolean isItemAvailable(MenuItem item) {
        return getMenu().getItems().contains(item);
    }

    /**
     * Validates whether an item can be added to an existing order.
     *
     * @param orderDTO     the order to which the item is being added
     * @param menuItem     the menu item that is intended to be added
     * @param deliveryTime the requested delivery time for the order (can be null)
     * @throws IllegalArgumentException if the order is not pending,
     *                                  if the item is unavailable,
     *                                  if the item cannot be prepared in time,
     *                                  or if there is no available slot for the item.
     */
    HttpResponse<String> canAddItemToOrder(OrderDTO orderDTO, MenuItem menuItem, LocalDateTime deliveryTime) {
        if (deliveryTime != null && deliveryTime.isAfter(this.getClosingTime().get())) {
            return new CustomHttpResponse(HttpCode.HTTP_400.getCode(), "Cannot add item to order, the restaurant will be closed.");
        }
        if (orderDTO.getStatus() != OrderStatus.PENDING) {
            return new CustomHttpResponse(HttpCode.HTTP_400.getCode(), "Order is not pending.");
        }
        if (!isItemAvailable(menuItem)) {
            return new CustomHttpResponse(HttpCode.HTTP_400.getCode(), "Item is not available.");
        }
        if (deliveryTime != null && isItemTooLate(menuItem, deliveryTime)) {
            return new CustomHttpResponse(HttpCode.HTTP_400.getCode(), "Cannot add item to order, restaurant cannot prepare it in time.");
        }
        if (deliveryTime != null && !slotAvailable(menuItem, deliveryTime)) {
            return new CustomHttpResponse(HttpCode.HTTP_400.getCode(), "Cannot add item to order, no slot available.");
        }
        if (deliveryTime == null && isItemTooLate(menuItem, getClosingTime().orElseThrow())) {
            return new CustomHttpResponse(HttpCode.HTTP_400.getCode(), "Cannot add item to order, restaurant cannot prepare it in time, the restaurant will be closed.");
        }
        return new CustomHttpResponse(HttpCode.HTTP_200.getCode(), "Item can be added to order.");
    }

    /**
     * Helper method to check if the item's preparation time exceeds the delivery time.
     */
    private boolean isItemTooLate(MenuItem menuItem, LocalDateTime deliveryTime) {
        LocalDateTime estimatedReadyTime = TimeUtils.getNow().plusSeconds(menuItem.getPrepTime());
        return estimatedReadyTime.isAfter(deliveryTime);
    }

    /**
     * Check if the order is valid.
     *
     * @param orderDTO The order to check
     * @return true if the order is valid, false otherwise
     */
    @Override
    public boolean isOrderValid(OrderDTO orderDTO) {
        if (!orderDTO.getStatus().equals(OrderStatus.PENDING)) {
            return false;
        }
        // Check that the restaurant has all the items in the order, and the order is not empty
        return new HashSet<>(getMenu().getItems()).containsAll(orderDTO.getItems()) && !orderDTO.getItems().isEmpty();
    }

    /**
     * Checks if there is a slot available for a given menu item before the specified delivery time.
     *
     * @param menuItem     The menu item to check.
     * @param deliveryTime The desired delivery time.
     * @return true if there is a slot available that can prepare the item before the delivery time, false otherwise.
     */
    public boolean slotAvailable(MenuItem menuItem, LocalDateTime deliveryTime) {
        LocalDateTime now = TimeUtils.getNow();
        for (Slot slot : slots) {
            // Check if the slot's opening time is before the delivery time and within operating hours
            boolean isSlotBeforeDeliveryTime = slot.getOpeningDate().isBefore(deliveryTime);
            // Check if the slot has enough capacity to prepare the item, and the number of orders is less than the maximum (EX2)
            boolean isSlotCapacityAvailable = slot.getAvailableCapacity() >= menuItem.getPrepTime() && pendingOrders.get(slot).size() < this.getMaxOrdersForSlot(slot);
            // Check that the slot is between the current time and the delivery time
            if (isSlotBeforeDeliveryTime && isSlotCapacityAvailable) {
                LocalDateTime preparationEndTime = now.plusSeconds(menuItem.getPrepTime());
                if (preparationEndTime.isBefore(deliveryTime)) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isOpenAt(LocalDateTime time) {
        return openingTime != null && closingTime != null &&
                !time.isBefore(openingTime) && !time.isAfter(closingTime);
    }

    @Override
    public boolean canAccommodateDeliveryTime(List<MenuItem> items, LocalDateTime deliveryTime) {
        LocalDateTime preparationEndTime = TimeUtils.getNow().plusSeconds(getPreparationTime(items));
        return deliveryTime.isAfter(preparationEndTime) && isOpenAt(deliveryTime);
    }


    /**
     * Mark the order as VALIDATED after payment and move it from pending orders to the order history.
     *
     * @param orderDTO The UUID of the order
     */
    @Override
    public HttpResponse<String> onOrderPaid(OrderDTO orderDTO) {
        try {
            ObjectMapper mapper = JacksonConfig.configureObjectMapper();
            orderDTO.setStatus(OrderStatus.VALIDATED);
            if (orderDTO instanceof IndividualOrderDTO individualOrderDTO) {
                return request(
                        RequestUtil.DATABASE_ORDER_SERVICE_URI,
                        "/individual/update",
                        HttpMethod.PUT,
                        mapper.writeValueAsString(individualOrderDTO)
                );
            }
            return request(
                    RequestUtil.DATABASE_ORDER_SERVICE_URI,
                    "/update",
                    HttpMethod.PUT,
                    mapper.writeValueAsString(orderDTO)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomHttpResponse(HttpCode.HTTP_500.getCode(), "An error occurred while updating the order status.");
        }
    }

    /**
     * Get the preparation time for a list of items
     *
     * @param items The list of items
     * @return The preparation time
     */
    public int getPreparationTime(List<MenuItem> items) {
        int preparationTime = 0;
        for (MenuItem item : items) {
            preparationTime += item.getPrepTime();
        }
        return preparationTime;
    }

    /**
     * Check if the restaurant can prepare any item in time for the delivery
     *
     * @param deliveryTime The delivery time
     * @return true if the restaurant can prepare any item in time, false otherwise
     */
    @Override
    public boolean canPrepareItemInTime(LocalDateTime deliveryTime) {
        // Check that the delivery time is not empty
        LocalDateTime now = TimeUtils.getNow();
        LocalDateTime closingTime = getClosingTime().orElseThrow();

        // Check if the restaurant can prepare any item before the closing time or the delivery time
        return getMenu().getItems().stream()
                .anyMatch(item -> {
                    LocalDateTime preparationEndTime = now.plusSeconds(item.getPrepTime());
                    return preparationEndTime.isBefore(deliveryTime) && preparationEndTime.isBefore(closingTime);
                });
    }

    @Override
    public boolean setNumberOfPersonnel(Slot slotToUpdate, int numberOfPersonnel) {
        if (slotToUpdate != null) {
            slotToUpdate.setNumberOfPersonnel(numberOfPersonnel);
            return true;
        }
        return false;
    }

    @Override
    public OrderPrice processOrderPrice(OrderDTO orderDTO) {
        // TODO
        return this.orderPriceStrategy.processOrderPrice(orderDTO, this);
    }

    @Override
    public boolean addMenuItemToSlot(Slot slot, MenuItem item) {
        if (!slot.updateSlotCapacity(item)) {
            int index = slots.indexOf(slot);
            if (index != -1 && index < slots.size() - 1) {
                Slot nextSlot = slots.get(index + 1);
                return addMenuItemToSlot(nextSlot, item);
            } else {
                return false;  // Plus de slots disponibles
            }
        }
        return true;  // Slot disponible
    }

    @Override
    public void changeMenu(Menu newMenu) {
        this.menu = newMenu;
    }

    @Override
    public Menu getMenu() {
        return menu;
    }

    @Override
    public String getRestaurantName() {
        return restaurantName;
    }

    @Override
    public Optional<LocalDateTime> getOpeningTime() {
        return Optional.ofNullable(openingTime);
    }

    @Override
    public Optional<LocalDateTime> getClosingTime() {
        return Optional.ofNullable(closingTime);
    }

    public OrderPriceStrategy getOrderPriceStrategy() {
        return orderPriceStrategy;
    }

    @Override
    public void setOrderPriceStrategy(OrderPriceStrategy orderPriceStrategy) {
        this.orderPriceStrategy = orderPriceStrategy;
    }

    @Override
    public UUID getRestaurantUUID() {
        return restaurantId;
    }

    @Override
    public Map<Slot, Set<OrderDTO>> getPendingOrders() {
        return pendingOrders;
    }

    @Override
    public List<Slot> getSlots() {
        return slots;
    }


    @Override
    public void addOrderToHistory(OrderDTO orderDTO) {
        this.ordersHistory.add(orderDTO);
    }

    @Override
    public List<OrderDTO> getOrdersHistory() {
        // TODO: To The Request
        HttpResponse<String> response = request(
                RequestUtil.DATABASE_RESTAURANT_SERVICE_URI,
                "/" + getRestaurantUUID() + "/history",
                HttpMethod.GET,
                null
        );
        if (response.statusCode() != 200) {
            return new ArrayList<>();
        }
        ObjectMapper mapper = JacksonConfig.configureObjectMapper();
        try {
            return mapper.readValue(response.body(), new TypeReference<List<OrderDTO>>() {
            });
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public double getTotalPrice(OrderDTO orderDTO) {
        return orderDTO.getItems().stream()
                .mapToDouble(MenuItemDTO::getPrice)
                .sum();
    }

    @Override
    public HttpResponse<String> changeHours(UUID managerId, LocalDateTime openingHour, LocalDateTime closingHour) {
        try {
            if (openingHour == null && closingHour != null) {
                return new CustomHttpResponse(HttpCode.HTTP_400.getCode(), "Opening time must be set before closing time.");
            }
            // Check that the opening time is before the closing time
            if (openingHour != null && openingHour.isAfter(closingHour)) {
                return new CustomHttpResponse(HttpCode.HTTP_400.getCode(), "Closing time cannot be before opening time.");
            }
            if (closingHour != null && closingHour.isBefore(openingHour)) {
                return new CustomHttpResponse(HttpCode.HTTP_400.getCode(), "Closing time cannot be before opening time.");
            }

            // Update the opening and closing time
            this.openingTime = openingHour;
            this.closingTime = closingHour;

            // if closing time is set, generate slots
            if (openingHour != null && closingHour != null) {
                generateSlots();
            } else {
                // if closing time is not set, remove all slots
                slots = new ArrayList<>();
            }
            // Call the database to update the restaurant
            ObjectMapper mapper = JacksonConfig.configureObjectMapper();
            RestaurantDTO restaurantDTO = DTOMapper.toRestaurantDTO(this);
            return request(
                    RequestUtil.DATABASE_RESTAURANT_SERVICE_URI,
                    "/update",
                    HttpMethod.PUT,
                    mapper.writeValueAsString(restaurantDTO)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomHttpResponse(HttpCode.HTTP_500.getCode(), "An error occurred while updating the restaurant.");
        }
    }

    public HttpResponse<String> changeNumberOfEmployees(UUID managerId, UUID slotId, int numberOfEmployees) {
        // Update the number of employees for the slot
        Slot slotToUpdate = slots.stream()
                .filter(slot -> slot.getUUID().equals(slotId))
                .findFirst()
                .orElse(null);
        if (slotToUpdate == null) {
            return new CustomHttpResponse(HttpCode.HTTP_404.getCode(), "Slot not found.");
        }
        slotToUpdate.setNumberOfPersonnel(numberOfEmployees);
        // Call the database to update the restaurant
        try {
            ObjectMapper mapper = JacksonConfig.configureObjectMapper();
            RestaurantDTO restaurantDTO = DTOMapper.toRestaurantDTO(this);
            return request(
                    RequestUtil.DATABASE_RESTAURANT_SERVICE_URI,
                    "/update",
                    HttpMethod.PUT,
                    mapper.writeValueAsString(restaurantDTO)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomHttpResponse(HttpCode.HTTP_500.getCode(), "An error occurred while updating the restaurant.");
        }
    }
}
