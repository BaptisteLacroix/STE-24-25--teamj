package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.DeliverableOrder;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderBuilder;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Restaurant {
    private final UUID restaurantId = UUID.randomUUID();
    private final String restaurantName;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private Menu menu;
    private OrderPriceStrategy orderPriceStrategy;
    private final List<Order> orders = new ArrayList<>();
    private List<Order> orderHistory = new ArrayList<>();

    public Restaurant(String name, LocalDateTime openingTime, LocalDateTime closingTime, Menu menu) {
        this.restaurantName = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.menu = menu;
        this.orderPriceStrategy = OrderPriceStrategyFactory.makeGiveItemForNItems(8);
    }

    public Restaurant(String name, LocalDateTime openingTime, LocalDateTime closingTime, Menu menu, OrderPriceStrategy orderPriceStrategy) {
        this.restaurantName = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.menu = menu;
        this.orderPriceStrategy = orderPriceStrategy;
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public OrderPriceStrategy getOrderPriceStrategy() {
        return orderPriceStrategy;
    }

    public void setOrderPriceStrategy(OrderPriceStrategy orderPriceStrategy) {
        this.orderPriceStrategy = orderPriceStrategy;
    }


// TODO : remove this method
    /**
     * Create and return an OrderBuilder for the restaurant
     *
     * @return The OrderBuilder instance
     */
    public OrderBuilder createOrderBuilder() {
        return new OrderBuilder().setRestaurant(this);
    }

    public double calculatePrice(Order order) {
        return order.getItems().stream()
                .mapToDouble(MenuItem::price)
                .sum();
    }

    public boolean isItemAvailable(MenuItem item) {
        return menu.getItems().contains(item);
    }

    public void addItemToOrder(OrderBuilder orderBuilder, MenuItem item) {
        if (orderBuilder.getRestaurant() != this) {
            throw new IllegalArgumentException("OrderBuilder is not for this restaurant");
        }
        if (isItemAvailable(item)) {
            orderBuilder.addMenuItem(item);
        } else {
            throw new IllegalArgumentException("MenuItem " + item.name() + " is not available.");
        }
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void addOrder(Order order) {
        orders.add(order);
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
