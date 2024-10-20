package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.payment.PaymentMethod;
import fr.unice.polytech.equipe.j.payment.PaymentProcessor;
import fr.unice.polytech.equipe.j.payment.PaymentProcessorFactory;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;


public class OrderManager {
    private final Clock clock;

    private PaymentMethod preferedPaymenMethod = PaymentMethod.CREDIT_CARD;

    public Clock getClock() {
        return clock;
    }

    public OrderManager(Clock clock) {
        this.clock = clock;
    }

    public GroupOrder startGroupOrder(DeliveryDetails deliveryDetails) {
        return new GroupOrder(deliveryDetails, getClock());
    }

    public IndividualOrder startSingleOrder(Restaurant restaurant, DeliveryDetails deliveryDetails) {
        if (restaurant.capacityCheck()) {
            IndividualOrder order = new IndividualOrder(restaurant, deliveryDetails, getClock());
            restaurant.addOrder(order);
            return order;
        }
        return null;
    }

    public Order startSubGroupOrder(Restaurant restaurant, GroupOrder groupOrder) {
        if (restaurant.capacityCheck() && restaurant.canPrepareItemForGroupOrderDeliveryTime(groupOrder)) {
            Order order = new Order(restaurant, getClock());
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

    /**
     * Add an item to an order, either individual or group.
     *
     * @param order        the order to which the item is added
     * @param restaurant   the restaurant preparing the order
     * @param menuItem     the menu item being added
     * @param deliveryTime the delivery time for the order (optional)
     * @throws IllegalArgumentException if the item is not available or cannot be prepared in time
     */
    public void addItemToOrder(Order order, Restaurant restaurant, MenuItem menuItem, Optional<LocalDateTime> deliveryTime) throws IllegalArgumentException {
        // Validate item availability
        if (!restaurant.isItemAvailable(menuItem)) {
            throw new IllegalArgumentException("Item is not available.");
        }

        // Check if the item can be prepared in time for the delivery
        if (deliveryTime.isPresent() && isItemTooLate(menuItem, deliveryTime.get())) {
            throw new IllegalArgumentException("Cannot add item to order, it will not be ready in time.");
        }

        if (deliveryTime.isPresent() && !restaurant.slotAvailable(menuItem, deliveryTime.get())) {
            throw new IllegalArgumentException("Cannot add item to order, no slot available.");
        }

        // Add the item to the order
        order.addItem(menuItem);
    }

    /**
     * Helper method to check if the item's preparation time exceeds the delivery time.
     */
    private boolean isItemTooLate(MenuItem menuItem, LocalDateTime deliveryTime) {
        LocalDateTime estimatedReadyTime = LocalDateTime.now(getClock()).plusSeconds(menuItem.getPrepTime());
        return estimatedReadyTime.isAfter(deliveryTime);
    }

    /**
     * Overloaded method to add an item to an individual order.
     */
    public void addItemToOrder(IndividualOrder order, Restaurant restaurant, MenuItem menuItem) throws IllegalArgumentException {
        addItemToOrder(order, restaurant, menuItem, order.getDeliveryDetails().getDeliveryTime());
    }

    /**
     * Add an item to a group order.
     */
    public void addItemToOrder(GroupOrder groupOrder, Order order, Restaurant restaurant, MenuItem menuItem) throws IllegalArgumentException {
        addItemToOrder(order, restaurant, menuItem, groupOrder.getDeliveryDetails().getDeliveryTime());
    }

    public PaymentMethod getPreferedPaymenMethod() {
        return preferedPaymenMethod;
    }

    public void setPreferedPaymenMethod(PaymentMethod preferedPaymenMethod) {
        this.preferedPaymenMethod = preferedPaymenMethod;
    }


    public Transaction validateOrder( Order order) throws IllegalArgumentException {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot validate order that is not pending.");
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot validate order with no items.");
        }
        if (!order.getRestaurant().isOrderValid(order)) {
            throw new IllegalArgumentException("Order is not valid.");
        }
        order.getRestaurant().orderPaid(order);
        order.setStatus(OrderStatus.VALIDATED);

        return makePayment(getTotalPrice(order),this.preferedPaymenMethod,order);


    }

    public Transaction makePayment(double amount, PaymentMethod method,Order order) {
        PaymentProcessor processor = PaymentProcessorFactory.createPaymentProcessor(method);
        boolean success = processor.processPayment(amount);
        if (success) {
            String paymentMethod = method.name();
            return  new Transaction(amount, paymentMethod, LocalDateTime.now(),order);
        } else {
            return null;
        }
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

    public void joinGroupOrder(GroupOrder groupOrder, CampusUser campusUser) {
        if (groupOrder.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot join group order that is not pending.");
        }
        campusUser.setGroupOrder(groupOrder);
    }
}
