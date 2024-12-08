package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.dto.OrderDTO;
import fr.unice.polytech.equipe.j.menu.Menu;
import fr.unice.polytech.equipe.j.menu.MenuItem;
import fr.unice.polytech.equipe.j.orderpricestrategy.OrderPrice;
import fr.unice.polytech.equipe.j.orderpricestrategy.OrderPriceStrategy;
import fr.unice.polytech.equipe.j.slot.Slot;

import java.net.http.HttpResponse;
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
     * @param orderDTO        the order to which the item is added
     * @param menuItem     the menu item being added
     * @param deliveryTime the delivery time for the order (optional)
     * @throws IllegalArgumentException if the item is not available or cannot be prepared in time
     */
    HttpResponse<String> addItemToOrder(OrderDTO orderDTO, MenuItem menuItem, LocalDateTime deliveryTime);

    HttpResponse<String> onOrderPaid(OrderDTO orderDTO);

    double getTotalPrice(OrderDTO orderDTO);

    Optional<LocalDateTime> getOpeningTime();

    Optional<LocalDateTime> getClosingTime();

    boolean isSlotCapacityAvailable();

    boolean canPrepareItemInTime(LocalDateTime deliveryTime);

    UUID getRestaurantUUID();

    void setOrderPriceStrategy(OrderPriceStrategy orderPriceStrategy);

    OrderPrice processOrderPrice(OrderDTO orderDTO);

    HttpResponse<String> cancelOrder(OrderDTO orderDTO, LocalDateTime deliveryTime);

    boolean isOrderValid(OrderDTO orderDTO);

    boolean canAccommodateDeliveryTime(List<MenuItem> items, LocalDateTime deliveryTime);

    Menu getMenu();

    String getRestaurantName();

    // TODO: See later if we keep this
    List<Slot> getSlots();

    boolean setNumberOfPersonnel(Slot slotToUpdate, int numberOfPersonnel);

    void addOrderToHistory(OrderDTO orderDTO);

    boolean addMenuItemToSlot(Slot slot, MenuItem selectedItem);

    List<OrderDTO> getOrdersHistory();

    Map<Slot, Set<OrderDTO>> getPendingOrders();

    void changeMenu(Menu testMenu);

    HttpResponse<String> changeHours(UUID managerId, LocalDateTime openingHour, LocalDateTime closingHour);

    HttpResponse<String> changeNumberOfEmployees(UUID managerId, UUID slotId, int numberOfEmployees);
}
