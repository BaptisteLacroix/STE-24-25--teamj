package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.payment.PaymentMethod;
import fr.unice.polytech.equipe.j.payment.PaymentProcessor;
import fr.unice.polytech.equipe.j.payment.PaymentProcessorFactory;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.time.LocalDateTime;
import java.util.Optional;


public class OrderManager {
    private final RestaurantProxy restaurantProxy;
    private GroupOrderProxy groupOrderProxy;

    public OrderManager(RestaurantProxy restaurantProxy) {
        this.restaurantProxy = restaurantProxy;
    }

    public OrderManager(RestaurantProxy restaurantProxy, GroupOrderProxy groupOrderProxy) {
        this(restaurantProxy);
        this.groupOrderProxy = groupOrderProxy;
    }

    public void cancelOrder(Restaurant restaurant, Order order) {
        restaurant.cancelOrder(order);
        order.setStatus(OrderStatus.CANCELLED);
    }

    public void addItemToOrder(Order order, MenuItem menuItem, LocalDateTime deliveryTime) throws IllegalArgumentException {
        if (order instanceof IndividualOrder && deliveryTime != null) getRestaurantProxy().addItemToOrder(order, menuItem, deliveryTime);
        else if (order instanceof IndividualOrder) {
            throw new IllegalArgumentException("Cannot add item to individual order without delivery time.");
        }
        else {
            if (groupOrderProxy.getStatus() != OrderStatus.PENDING) {
                throw new IllegalArgumentException("Cannot add item to group order that is not pending.");
            }
            getRestaurantProxy().addItemToOrder(order, menuItem, groupOrderProxy.getDeliveryDetails().getDeliveryTime().orElse(null));
            if (!groupOrderProxy.getOrders().contains(order)) {
                groupOrderProxy.addOrder(order);
            }
        }
    }

    // TODO: See later Transaction return
    public Transaction validateOrder(Order order) {
        getRestaurantProxy().checkOrderCanBeValidated(order);
        PaymentMethod method = order.getUser().getDefaultPaymentMethod();
        PaymentProcessor processor = PaymentProcessorFactory.createPaymentProcessor(method);
        double totalPrice = getRestaurantProxy().getTotalPrice(order);
        processor.processPayment(totalPrice);
        getRestaurantProxy().onOrderPaid(order);
        order.getUser().addOrderToHistory(order);
        Transaction transaction = new Transaction(totalPrice, method.name(), LocalDateTime.now(), order);
        order.getUser().addTransactionToHistory(transaction);
        return transaction;
    }

    public void validateGroupOrder(CampusUser campusUser) {
        groupOrderProxy.validateOrder(campusUser);
    }

    public void validateGroupOrder(CampusUser campusUser, LocalDateTime deliveryTime) {
        groupOrderProxy.setDeliveryTime(deliveryTime);
        this.validateGroupOrder(campusUser);
    }

    public RestaurantProxy getRestaurantProxy() {
        return restaurantProxy;
    }
}
