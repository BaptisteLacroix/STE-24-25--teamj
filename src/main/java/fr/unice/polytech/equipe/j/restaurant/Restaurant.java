package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.payment.CheckoutObserver;
import fr.unice.polytech.equipe.j.slot.Slot;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Restaurant implements CheckoutObserver {
    private final UUID restaurantId = UUID.randomUUID();
    private final String restaurantName;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private List<Slot> slots;
    private Menu menu;
    private final List<Order> ordersHistory = new ArrayList<>();
    private final List<Order> pendingOrders = new ArrayList<>();
    private int capacity = 10; // TODO: Change later (EX2)
    private int numberOfPersonnel;
    private Clock clock;

    public Restaurant(String name, List<Slot> slots, LocalDateTime openingTime, LocalDateTime closingTime, Menu menu, Clock clock) {
        this.restaurantName = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.menu = menu;
        this.slots = slots;
        this.clock = clock;
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

    public LocalDateTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalDateTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalDateTime closingTime) {
        this.closingTime = closingTime;
    }

    public List<Order> getOrdersHistory() {
        return ordersHistory;
    }

    public void addOrder(Order order) {
        pendingOrders.add(order);
        capacity--;
    }

    /**
     * Check if the restaurant is at full capacity
     * If the restaurant is at full capacity, an IllegalStateException is thrown
     * Otherwise, the capacity is decremented
     */
    public boolean capacityCheck() {
        return getCapacity() > 0;
    }

    /**
     * Cancel an order
     */
    public void cancelOrder(Order order) {
        getPendingOrders().remove(order);
        capacity++;
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

    public UUID getRestaurantId() {
        return restaurantId;
    }

    /**
     * Check if the order is valid
     *
     * @param order The order to check
     * @return true if the order is valid, false otherwise
     */
    public boolean isOrderValid(Order order) {
        // We need to check that the restaurant has all the items in the order, that he can prepare them in time
        // and that the order is not empty
        return getMenu().getItems().containsAll(order.getItems()) && order.getItems().size() > 0;
    }

    /**
     * When the user paid the order, the order is marked as VALIDATED.
     * and the order is add to the restaurant.
     *
     * @param order The UUID of the order
     */
    @Override
    public void orderPaid(Order order) {
        order.setStatus(OrderStatus.VALIDATED);
        getOrdersHistory().add(order);
        getPendingOrders().remove(order);
    }

    public List<Order> getPendingOrders() {
        return pendingOrders;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setNumberOfPersonnel(Slot slotToUpdate, int numberOfPersonnel) {
        if (slotToUpdate != null) {
            slotToUpdate.setNumberOfPersonnel(numberOfPersonnel);
        }
    }

    public int getNumberOfPersonnel() {
        return numberOfPersonnel;
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
        LocalDateTime closingTime = getClosingTime();

        // Check if the restaurant can prepare any item before the closing time or the delivery time
        return getMenu().getItems().stream()
                .anyMatch(item -> {
                    LocalDateTime preparationEndTime = now.plusSeconds(item.getPrepTime());
                    return preparationEndTime.isBefore(deliveryTime) && preparationEndTime.isBefore(closingTime);
                });
    }

}
