package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.DeliverableOrder;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.payment.CheckoutObserver;
import fr.unice.polytech.equipe.j.slot.Slot;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class Restaurant implements CheckoutObserver {
    private final UUID restaurantId = UUID.randomUUID();
    private final String restaurantName;
    private Optional<LocalDateTime> openingTime;
    private Optional<LocalDateTime> closingTime;
    private List<Slot> slots;
    private Menu menu;
    private OrderPriceStrategy orderPriceStrategy;
    private final List<Order> orders = new ArrayList<>();
    private final List<Order> ordersHistory = new ArrayList<>();
    private final List<Order> pendingOrders = new ArrayList<>();
    private int numberOfPersonnel;
    private List<Order> orderHistory = new ArrayList<>();
    private Clock clock;

    public Restaurant(String name, LocalDateTime openingTime, LocalDateTime closingTime, Menu menu, Clock clock) {
        this(name, openingTime, closingTime, menu, clock, OrderPriceStrategyFactory.makeGiveItemForNItems(8));
    }
    private Restaurant(String name, LocalDateTime openingTime, LocalDateTime closingTime, Menu menu, Clock clock, OrderPriceStrategy strategy) {
        this.restaurantName = name;
        this.openingTime = openingTime == null ? Optional.empty() : Optional.of(openingTime);
        if (closingTime != null && closingTime.isBefore(openingTime)) {
            throw new IllegalArgumentException("Closing time cannot be before opening time.");
        }
        this.closingTime = closingTime == null ? Optional.empty() : Optional.of(closingTime);
        if (this.openingTime.isPresent() && this.closingTime.isEmpty()) {
            throw new IllegalArgumentException("Closing time is required if the restaurant is open.");
        }
        this.menu = menu;
        this.slots = slots;
        this.orderPriceStrategy = strategy;
        this.clock = clock;
        generateSlots();
    }

    /**
     * For every 30 minutes, generate a slot with a duration of 30 minutes. The number of slots is calculated based on the opening and closing time.
     */
    private void generateSlots() {
        if (openingTime.isEmpty() || closingTime.isEmpty()) {
            return;
        }
        slots = new ArrayList<>();
        LocalDateTime currentTime = openingTime.get();
        while (currentTime.isBefore(closingTime.get())) {
            slots.add(new Slot(currentTime, 0));
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
    public int getMaxCapacity() {
        int averagePrepTime = calculateAveragePreparationTime();
        int totalCapacity = 0;

        for (Slot slot : slots) {
            int slotDurationInSeconds = (int) slot.getDurationTime().getSeconds();
            int slotCapacity = slotDurationInSeconds / averagePrepTime;

            slot.setMaxCapacity(slotCapacity);
            totalCapacity += slotCapacity;
        }
        return totalCapacity;
    }

    /**
     * Add an order to the restaurant and adjust the capacity of the relevant slot.
     * Throws an exception if the restaurant has reached full capacity.
     *
     * @param order the order to add
     */
    public void addOrder(Order order) {
        if (capacityCheck()) {
            pendingOrders.add(order);

            // Adjust the capacity for each slot based on the order
            for (Slot slot : slots) {
                for (MenuItem item : order.getItems()) {
                    slot.UpdateSlotCapacity(item);
                }
            }
        } else {
            throw new IllegalStateException("The restaurant has reached full capacity.");
        }
    }

    /**
     * Check if the restaurant has enough capacity to accept a new order.
     *
     * @return true if there is capacity, false otherwise.
     */
    public boolean capacityCheck() {
        return slots.stream().anyMatch(slot -> slot.getAvailableCapacity() > 0);
    }

    /**
     * Cancel an order and free up capacity.
     *
     * @param order the order to cancel
     */
    public void cancelOrder(Order order) {
        if (pendingOrders.remove(order)) {
            // Adjust the capacity for each slot when an order is canceled
            for (Slot slot : slots) {
                for (MenuItem item : order.getItems()) {
                    slot.addCapacity(-item.getPrepTime());
                }
            }
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
     * Check if the order is valid.
     *
     * @param order The order to check
     * @return true if the order is valid, false otherwise
     */
    public boolean isOrderValid(Order order) {
        // Check that the restaurant has all the items in the order, and the order is not empty
        return getMenu().getItems().containsAll(order.getItems()) && order.getItems().size() > 0;
    }

    /**
     * Mark the order as VALIDATED after payment and move it from pending orders to the order history.
     *
     * @param order The UUID of the order
     */
    @Override
    public void orderPaid(Order order) {
        order.setStatus(OrderStatus.VALIDATED);
        ordersHistory.add(order);
        pendingOrders.remove(order);
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
     * @param groupOrder The group order
     * @return true if the restaurant can prepare any item in time, false otherwise
     */
    public boolean canPrepareItemForGroupOrderDeliveryTime(GroupOrder groupOrder) {
        // Check that the delivery time is not empty
        if (groupOrder.getDeliveryDetails().getDeliveryTime().isEmpty()) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now(getClock());
        LocalDateTime deliveryTime = groupOrder.getDeliveryDetails().getDeliveryTime().get();
        LocalDateTime closingTime = getClosingTime().get();

        // Check if the restaurant can prepare any item before the closing time or the delivery time
        return getMenu().getItems().stream()
                .anyMatch(item -> {
                    LocalDateTime preparationEndTime = now.plusSeconds(item.getPrepTime());
                    return preparationEndTime.isBefore(deliveryTime) && preparationEndTime.isBefore(closingTime);
                });
    }

    /**
     * Checks if there is a slot available for a given menu item before the specified delivery time.
     *
     * @param menuItem     The menu item to check.
     * @param deliveryTime The desired delivery time.
     * @return true if there is a slot available that can prepare the item before the delivery time, false otherwise.
     */
    public boolean slotAvailable(MenuItem menuItem, LocalDateTime deliveryTime) {
        LocalDateTime now = LocalDateTime.now(clock);

        for (Slot slot : slots) {
            // Check if the slot's opening time is before the delivery time and within operating hours
            // Check if the item can be prepared before the delivery time
            boolean isBefore = slot.getOpeningDate().isBefore(deliveryTime);
            boolean isAfter = slot.getOpeningDate().isAfter(now);
            boolean isCapacityAvailable = slot.getAvailableCapacity() >= menuItem.getPrepTime();
            if (isBefore && !isAfter && isCapacityAvailable) {
                LocalDateTime preparationEndTime = now.plusSeconds(menuItem.getPrepTime());
                if (preparationEndTime.isBefore(deliveryTime)) {
                    return true;
                }
            }

        }
        return false;
    }

    public void setNumberOfPersonnel(Slot slotToUpdate, int numberOfPersonnel) {
        if (slotToUpdate != null) {
            slotToUpdate.setNumberOfPersonnel(numberOfPersonnel);
        }
    }

    public Clock getClock() {
        return clock;
    }


    public void changeMenu(Menu newMenu) {
        this.menu = newMenu;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public Optional<LocalDateTime> getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalDateTime openingTime) {
        this.openingTime = openingTime == null ? Optional.empty() : Optional.of(openingTime);
        // if opening time is set, generate slots
        if (this.openingTime.isPresent() && this.closingTime.isPresent()) {
            generateSlots();
        }
        // if the opening time is empty, clear the slots and set the closing time to empty
        if (this.openingTime.isEmpty()) {
            slots.clear();
            this.closingTime = Optional.empty();
        }
    }

    public Optional<LocalDateTime> getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalDateTime closingTime) {
        if (closingTime != null && closingTime.isBefore(openingTime.orElseThrow())) {
            throw new IllegalArgumentException("Closing time cannot be before opening time.");
        }
        this.closingTime = closingTime == null ? Optional.empty() : Optional.of(closingTime);
        // if closing time is set, generate slots
        if (this.openingTime.isPresent() && this.closingTime.isPresent()) {
            generateSlots();
        }
        // if the closing time is empty, clear the slots
        if (this.closingTime.isEmpty()) {
            slots.clear();
        }
    }

    public List<Order> getOrders() {
        return orders;
    }

    public OrderPriceStrategy getOrderPriceStrategy() {
        return orderPriceStrategy;
    }

    public void setOrderPriceStrategy(OrderPriceStrategy orderPriceStrategy) {
        this.orderPriceStrategy = orderPriceStrategy;
    }

    public double calculatePrice(Order order) {
        return order.getItems().stream()
                .mapToDouble(MenuItem::getPrice)
                .sum();
    }

    public List<Order> getOrdersHistory() {
        return ordersHistory;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public List<Order> getPendingOrders() {
        return pendingOrders;
    }

    public int getCapacity() {
        return getMaxCapacity(); // Dynamically return the current max capacity based on the slots
    }

    public boolean addMenuItemToSlot(Slot slot, MenuItem item) {
        if (!slot.UpdateSlotCapacity(item)) {
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

    public int getNumberOfPersonnel() {
        return numberOfPersonnel;
    }

    public List<Slot> getSlots() {
        return slots;
    }


    public void addOrderToHistory(Order order) {
        this.orderHistory.add(order);
    }

    public OrderPrice processOrderPrice(Order order) {
        assert order.getRestaurant().equals(this);
        return this.orderPriceStrategy.processOrderPrice(order, this);
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }
}
