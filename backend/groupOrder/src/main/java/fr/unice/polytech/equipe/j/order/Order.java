package java.fr.unice.polytech.equipe.j.order;

import java.fr.unice.polytech.equipe.j.restaurant.IRestaurant;
import java.fr.unice.polytech.equipe.j.restaurant.menu.MenuItem;
import java.fr.unice.polytech.equipe.j.user.CampusUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID orderUUID = UUID.randomUUID();
    private final IRestaurant restaurant;
    private final List<MenuItem> items;
    private final CampusUser user;
    private OrderStatus status;

    public Order(IRestaurant restaurant, CampusUser user) {
        this.restaurant = restaurant;
        items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.user = user;
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
        if (!(obj instanceof Order order)) return false;
        return orderUUID.equals(order.orderUUID);
    }

    @Override
    public int hashCode() {
        return orderUUID.hashCode();
    }
}
