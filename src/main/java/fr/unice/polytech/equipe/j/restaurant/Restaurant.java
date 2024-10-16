package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.payment.CheckoutObserver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Restaurant implements CheckoutObserver {
    private final UUID restaurantId = UUID.randomUUID();
    private final String restaurantName;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private Menu menu;
    private final List<Order> ordersHistory = new ArrayList<>();
    private final List<Order> pendingOrders = new ArrayList<>();
    private int capacity = 10; // TODO: Change later (EX2)

    public Restaurant(String name, LocalDateTime openingTime, LocalDateTime closingTime, Menu menu) {
        this.restaurantName = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.menu = menu;
    }

    public void changeMenu(Menu newMenu) {
        this.menu = newMenu;
    }

    public Menu getMenu() {
        return menu;
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
     *
     * @throws IllegalStateException if the restaurant is at full capacity
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
        // TODO: Improve when the issues restaurant managers are done
        return order.getItems().stream().allMatch(this::isItemAvailable);
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
}
