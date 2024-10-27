package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.grouporder.IGroupOrder;
import fr.unice.polytech.equipe.j.restaurant.menu.Menu;
import fr.unice.polytech.equipe.j.restaurant.menu.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.strategies.order.OrderPrice;
import fr.unice.polytech.equipe.j.restaurant.strategies.order.OrderPriceStrategy;
import fr.unice.polytech.equipe.j.slot.Slot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IRestaurant {

    /**
     * Add an item to an order, either individual or group.
     *
     * @param order        the order to which the item is added
     * @param menuItem     the menu item being added
     * @param deliveryTime the delivery time for the order (optional)
     * @throws IllegalArgumentException if the item is not available or cannot be prepared in time
     */
    void addItemToOrder(Order order, MenuItem menuItem, LocalDateTime deliveryTime);

    void onOrderPaid(Order order);

    double getTotalPrice(Order order);

    Optional<LocalDateTime> getOpeningTime();

    Optional<LocalDateTime> getClosingTime();

    boolean isSlotCapacityAvailable();

    boolean canPrepareItemForGroupOrderDeliveryTime(IGroupOrder groupOrderProxy);

    UUID getRestaurantUUID();

    void setOrderPriceStrategy(OrderPriceStrategy orderPriceStrategy);

    OrderPrice processOrderPrice(Order order);

    void cancelOrder(Order order, LocalDateTime deliveryTime);

    boolean isOrderValid(Order order);

    boolean canAccommodateDeliveryTime(List<MenuItem> items, LocalDateTime deliveryTime);

    Menu getMenu();

    String getRestaurantName();

    // TODO: See later if we keep this
    List<Slot> getSlots();

    void setOpeningTime(LocalDateTime openingHour);

    void setClosingTime(LocalDateTime closingHour);

    void setNumberOfPersonnel(Slot slotToUpdate, int numberOfPersonnel);

    void addOrderToHistory(Order order);

    boolean addMenuItemToSlot(Slot slot, MenuItem selectedItem);

    List<Order> getOrdersHistory();

    Map<Slot, Set<Order>> getPendingOrders();

    void changeMenu(Menu testMenu);
}
