package java.fr.unice.polytech.equipe.j.restaurant;

import java.fr.unice.polytech.equipe.j.order.Order;
import java.fr.unice.polytech.equipe.j.order.OrderStatus;
import java.fr.unice.polytech.equipe.j.order.grouporder.backend.IGroupOrder;
import java.fr.unice.polytech.equipe.j.restaurant.menu.Menu;
import java.fr.unice.polytech.equipe.j.restaurant.menu.MenuItem;
import java.fr.unice.polytech.equipe.j.restaurant.orderpricestrategy.OrderPrice;
import java.fr.unice.polytech.equipe.j.restaurant.orderpricestrategy.OrderPriceStrategy;
import java.fr.unice.polytech.equipe.j.restaurant.slot.Slot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class RestaurantProxy implements IRestaurant {
    private final IRestaurant restaurant;

    public RestaurantProxy(IRestaurant restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * Add an item to an order, either individual or group.
     *
     * @param order        the order to which the item is added
     * @param menuItem     the menu item being added
     * @param deliveryTime the delivery time for the order (optional)
     */
    // @Override
    public void addItemToOrder(Order order, MenuItem menuItem, LocalDateTime deliveryTime) {
        ((Restaurant)restaurant).canAddItemToOrder(order, menuItem, deliveryTime);
        restaurant.addItemToOrder(order, menuItem, deliveryTime);
    }

    @Override
    public void cancelOrder(Order order, LocalDateTime deliveryTime) {
        // If the delivery time is not half an hour before the order, the order is cancelled
        if (deliveryTime.isBefore(LocalDateTime.now().minusMinutes(30))) {
            restaurant.cancelOrder(order, deliveryTime);
            order.setStatus(OrderStatus.CANCELLED);
        }
    }

    @Override
    public boolean isOrderValid(Order order) {
        return getRestaurant().isOrderValid(order);
    }

    @Override
    public boolean canAccommodateDeliveryTime(List<MenuItem> items, LocalDateTime deliveryTime) {
        return getRestaurant().canAccommodateDeliveryTime(items, deliveryTime);
    }

    @Override
    public double getTotalPrice(Order order) {
        return getRestaurant().getTotalPrice(order);
    }

    @Override
    public Menu getMenu() {
        return getRestaurant().getMenu();
    }

    @Override
    public Optional<LocalDateTime> getOpeningTime() {
        return getRestaurant().getOpeningTime();
    }

    @Override
    public Optional<LocalDateTime> getClosingTime() {
        return getRestaurant().getClosingTime();
    }

    @Override
    public boolean isSlotCapacityAvailable() {
        return getRestaurant().isSlotCapacityAvailable();
    }

    @Override
    public void onOrderPaid(Order order) {
        getRestaurant().onOrderPaid(order);
    }

    // for Mock testing
    public IRestaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public boolean canPrepareItemForGroupOrderDeliveryTime(IGroupOrder groupOrderProxy) {
        return restaurant.canPrepareItemForGroupOrderDeliveryTime(groupOrderProxy);
    }

    @Override
    public UUID getRestaurantUUID() {
        return getRestaurant().getRestaurantUUID();
    }

    @Override
    public void setOrderPriceStrategy(OrderPriceStrategy orderPriceStrategy) {
        restaurant.setOrderPriceStrategy(orderPriceStrategy);
    }

    @Override
    public OrderPrice processOrderPrice(Order order) {
        return restaurant.processOrderPrice(order);
    }

    @Override
    public String getRestaurantName() {
        return getRestaurant().getRestaurantName();
    }

    @Override
    public List<Slot> getSlots() {
        return getRestaurant().getSlots();
    }

    @Override
    public void setOpeningTime(LocalDateTime openingHour) {
        getRestaurant().setOpeningTime(openingHour);
    }

    @Override
    public void setClosingTime(LocalDateTime closingHour) {
        getRestaurant().setClosingTime(closingHour);
    }

    @Override
    public boolean setNumberOfPersonnel(Slot slotToUpdate, int numberOfPersonnel) {
        return getRestaurant().setNumberOfPersonnel(slotToUpdate, numberOfPersonnel);
    }

    @Override
    public void addOrderToHistory(Order order) {
        getRestaurant().addOrderToHistory(order);
    }

    @Override
    public boolean addMenuItemToSlot(Slot slot, MenuItem selectedItem) {
        return getRestaurant().addMenuItemToSlot(slot, selectedItem);
    }

    @Override
    public List<Order> getOrdersHistory() {
        return getRestaurant().getOrdersHistory();
    }

    @Override
    public Map<Slot, Set<Order>> getPendingOrders() {
        return getRestaurant().getPendingOrders();
    }

    @Override
    public void changeMenu(Menu testMenu) {
        getRestaurant().changeMenu(testMenu);
    }
}
