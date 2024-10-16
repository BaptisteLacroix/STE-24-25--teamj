package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;


public class OrderManager {

    public GroupOrder startGroupOrder(DeliveryDetails deliveryDetails) {
        return new GroupOrder(deliveryDetails);
    }

    public Order startSingleOrder(Restaurant restaurant, DeliveryDetails deliveryDetails) {
        if (restaurant.capacityCheck()) {
            IndividualOrder order = new IndividualOrder(restaurant, deliveryDetails);
            restaurant.addOrder(order);
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
        order.addItem(menuItem);
    }

    public void addOrderToGroup(GroupOrder groupOrder, Order order) {
        if (groupOrder.getStatus() == OrderStatus.PENDING) {
            groupOrder.addOrder(order);
        }
    }

    public void validateIndividualOrder(Transaction transaction, Order order, Restaurant restaurant) throws IllegalArgumentException {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot validate order that is not pending.");
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot validate order with no items.");
        }
        if (!restaurant.isOrderValid(order)) {
            throw new IllegalArgumentException("Order is not valid.");
        }
        transaction.addObserver(restaurant);
        transaction.proceedCheckout(order, getTotalPrice(order));
        transaction.removeObserver(restaurant);
    }

    public void validateGroupOrder(GroupOrder groupOrder) throws IllegalArgumentException {
        // TODO: Check for validation Order group
        if (groupOrder.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot validate group order that is not pending.");
        }
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
