package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID orderUUID = UUID.randomUUID();
    private final RestaurantProxy restaurant;
    private final List<MenuItem> items;
    private final CampusUser user;
    private OrderStatus status;

    public Order(RestaurantProxy restaurant, CampusUser user) {
        this.restaurant = restaurant;
        items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.user = user;
    }

    public CampusUser getUser() {
        return user;
    }

    public RestaurantProxy getRestaurant() {
        return restaurant;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void addItem(MenuItem item) {
        if (getStatus().equals(OrderStatus.VALIDATED)) {
            throw new IllegalArgumentException("Cannot add items to an order that is already in preparation.");
        }
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        if (getStatus().equals(OrderStatus.VALIDATED)) {
            throw new IllegalArgumentException("Cannot remove items from an order that is already in preparation.");
        }
        items.remove(item);
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public UUID getOrderUUID() {
        return orderUUID;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderUUID=" + orderUUID +
                ", restaurantUUID=" + restaurant +
                ", items=" + items +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Order)) return false;
        Order order = (Order) obj;
        return orderUUID.equals(order.orderUUID);
    }

    @Override
    public int hashCode() {
        return orderUUID.hashCode();
    }
}
