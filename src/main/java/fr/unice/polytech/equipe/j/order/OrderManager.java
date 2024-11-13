package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.grouporder.backend.IGroupOrder;
import fr.unice.polytech.equipe.j.payment.strategy.PaymentMethod;
import fr.unice.polytech.equipe.j.payment.strategy.PaymentProcessor;
import fr.unice.polytech.equipe.j.payment.strategy.PaymentProcessorFactory;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.backend.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.backend.menu.MenuItem;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.time.LocalDateTime;


public class OrderManager {
    private final IRestaurant restaurantProxy;
    private IGroupOrder groupOrderProxy;

    public OrderManager(IRestaurant restaurantProxy) {
        this.restaurantProxy = restaurantProxy;
    }

    public OrderManager(IRestaurant restaurantProxy, IGroupOrder groupOrderProxy) {
        this(restaurantProxy);
        this.groupOrderProxy = groupOrderProxy;
    }

    public void cancelOrder(Order order, LocalDateTime deliveryTime) {
        restaurantProxy.cancelOrder(order, deliveryTime);
    }

    public void addItemToOrder(Order order, MenuItem menuItem) throws IllegalArgumentException {
        if (order instanceof IndividualOrder individualOrder && individualOrder.getDeliveryDetails() != null) getRestaurantProxy().addItemToOrder(order, menuItem, individualOrder.getDeliveryDetails().getDeliveryTime().orElseThrow());
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
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot validate order that is not pending.");
        }
        if (!getRestaurantProxy().isOrderValid(order)) {
            throw new IllegalArgumentException("Order is not valid.");
        }
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
        groupOrderProxy.validate(campusUser);
    }

    public void validateGroupOrder(CampusUser campusUser, LocalDateTime deliveryTime) {
        groupOrderProxy.setDeliveryTime(deliveryTime);
        this.validateGroupOrder(campusUser);
    }

    public IRestaurant getRestaurantProxy() {
        return restaurantProxy;
    }
}
