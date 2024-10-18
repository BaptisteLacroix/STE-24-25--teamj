package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;

import java.time.LocalDateTime;


public class OrderManager {

    public GroupOrder startGroupOrder(DeliveryDetails deliveryDetails) {
        return new GroupOrder(deliveryDetails);
    }

    public IndividualOrder startSingleOrder(Restaurant restaurant, DeliveryDetails deliveryDetails) {
        if (restaurant.capacityCheck()) {
            IndividualOrder order = new IndividualOrder(restaurant, deliveryDetails);
            restaurant.addOrder(order);
            return order;
        }
        return null;
    }

    public Order startSubGroupOrder(Restaurant restaurant, GroupOrder groupOrder) {
        if (restaurant.capacityCheck()) {
            Order order = new Order(restaurant);
            restaurant.addOrder(order);
            groupOrder.addOrder(order);
            return order;
        }
        return null;
    }

    public void cancelOrder(Restaurant restaurant, Order order) {
        restaurant.cancelOrder(order);
        order.setStatus(OrderStatus.CANCELLED);
    }

    public void addItemToOrder(Order order, Restaurant restaurant, MenuItem menuItem) throws IllegalArgumentException {
        if (!restaurant.isItemAvailable(menuItem)) {
            throw new IllegalArgumentException("Item is not available.");
        }
        IndividualOrder individualOrder = (IndividualOrder) order;
        // Check that the restauratn an prepare the item before teh delivery time
        if (!restaurant.canPrepareItem(menuItem, individualOrder.getDeliveryDetails().getDeliveryTime())) {
            throw new IllegalArgumentException("Restaurant cannot prepare the item before the delivery time.");
        }
        order.addItem(menuItem);
    }

    public void addItemToOrder(GroupOrder groupOrder, Order order, Restaurant restaurant, MenuItem menuItem) throws IllegalArgumentException {
        if (!restaurant.isItemAvailable(menuItem)) {
            throw new IllegalArgumentException("Item is not available.");
        }
        // if the order is from a groupOrder, we need to check that the restaurant can reparare the food before the delivery time

        order.addItem(menuItem);
    }

    public void addOrderToGroup(GroupOrder groupOrder, Order order) {
        if (groupOrder.getStatus() == OrderStatus.PENDING) {
            groupOrder.addOrder(order);
        }
    }

    public void validateOrder(Transaction transaction, Order order) throws IllegalArgumentException {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot validate order that is not pending.");
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot validate order with no items.");
        }
        if (!order.getRestaurant().isOrderValid(order)) {
            throw new IllegalArgumentException("Order is not valid.");
        }
        transaction.addObserver(order.getRestaurant());
        transaction.proceedCheckout(order, getTotalPrice(order));
        transaction.removeObserver(order.getRestaurant());
    }

    public void validateGroupOrder(GroupOrder groupOrder) throws IllegalArgumentException {
        if (groupOrder.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot validate group order that is not pending.");
        }
        if (groupOrder.getDeliveryDetails().getDeliveryTime().isEmpty()) {
            throw new IllegalArgumentException("Cannot validate group order with no delivery time.");
        }
        groupOrder.setStatus(OrderStatus.VALIDATED);
    }

    public void validateGroupOrder(GroupOrder groupOrder, LocalDateTime deliveryTime) throws IllegalArgumentException {
        if (groupOrder.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot validate group order that is not pending.");
        }
        if (groupOrder.getDeliveryDetails().getDeliveryTime().isPresent()) {
            throw new IllegalArgumentException("You cannot change the delivery time of a group order that is already set.");
        }
        groupOrder.setDeliveryTime(deliveryTime);
        groupOrder.setStatus(OrderStatus.VALIDATED);
    }

    public double getTotalPrice(Order order) {
        double totalPrice = 0;
        for (MenuItem item : order.getItems()) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }
}
