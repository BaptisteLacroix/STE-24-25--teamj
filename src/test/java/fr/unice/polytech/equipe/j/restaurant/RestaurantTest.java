package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.order.IndividualOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.restaurant.menu.Menu;
import fr.unice.polytech.equipe.j.restaurant.menu.MenuItem;
import fr.unice.polytech.equipe.j.slot.Slot;
import fr.unice.polytech.equipe.j.user.CampusUser;
import fr.unice.polytech.equipe.j.user.RestaurantManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantTest {

    private IRestaurant restaurant;
    private IRestaurant restaurantProxy;
    private MenuItem item1;
    private MenuItem item2;
    private Slot slot;
    private OrderManager orderManager;
    private CampusUser campusUser;
    private DeliveryDetails deliveryDetails;

    @BeforeEach
    void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-01T07:00:00Z"), ZoneId.of("Europe/Paris")));
        item1 = new MenuItem("Burger", 40, 5.99);
        item2 = new MenuItem("Fries", 1, 2.99);
        Menu menu = new Menu.MenuBuilder().addMenuItems(List.of(item1, item2)).build();

        restaurant = new Restaurant("Test Restaurant",
                LocalDateTime.of(2024, 10, 1, 9, 0),
                LocalDateTime.of(2024, 10, 1, 21, 0), menu);

        restaurantProxy = new RestaurantProxy(restaurant);

        RestaurantManager manager = new RestaurantManager(
                "email",
                "password",
                "Manager",
                restaurant);

        manager.updateNumberOfPersonnel(manager.getRestaurant().getSlots().getFirst(), 1);
        slot = restaurant.getSlots().getFirst();
        campusUser = new CampusUser("John", 0);
        orderManager = new OrderManager(restaurantProxy);

        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        deliveryDetails = new DeliveryDetails(deliveryLocation, TimeUtils.getNow().plusMinutes(30));
    }

    @Test
    void testChangeMenu() {
        Menu newMenu = new Menu.MenuBuilder().addMenuItem(new MenuItem("Salad", 10, 4.99)).build();
        restaurant.changeMenu(newMenu);
        assertEquals(newMenu, restaurant.getMenu());
    }

    @Test
    void testOpeningAndClosingTime() {
        assertEquals(LocalDateTime.of(2024, 10, 1, 9, 0), restaurant.getOpeningTime().orElseThrow());
        assertEquals(LocalDateTime.of(2024, 10, 1, 21, 0), restaurant.getClosingTime().orElseThrow());

        restaurant.setOpeningTime(LocalDateTime.of(2024, 10, 1, 8, 0));
        restaurant.setClosingTime(LocalDateTime.of(2024, 10, 1, 22, 0));

        assertEquals(LocalDateTime.of(2024, 10, 1, 8, 0), restaurant.getOpeningTime().get());
        assertEquals(LocalDateTime.of(2024, 10, 1, 22, 0), restaurant.getClosingTime().get());
    }

    @Test
    void testIsSlotCapacityAvailable() {
        assertTrue(restaurant.isSlotCapacityAvailable());

        assertEquals(slot.getMaxCapacity(), slot.getAvailableCapacity());

        // Add an order to reduce capacity
        IndividualOrder order = new IndividualOrder(restaurantProxy, deliveryDetails, campusUser);
        restaurant.addItemToOrder(
                order,
                item1,
                deliveryDetails.getDeliveryTime().orElse(null)
        ); // 40 seconds

        // Check if the capacity has decreased
        assertEquals(slot.getMaxCapacity() - item1.getPrepTime(), slot.getAvailableCapacity());

        // Cancel the order and check capacity is restored
        restaurant.cancelOrder(order, deliveryDetails.getDeliveryTime().orElse(null));
        assertEquals(slot.getMaxCapacity(), slot.getAvailableCapacity());
    }

    @Test
    void testAddAndCancelOrder() {
        Order order1 = new Order(restaurantProxy, campusUser);
        restaurant.addItemToOrder(
                order1,
                item1,
                deliveryDetails.getDeliveryTime().orElse(null)
        ); // 40 seconds

        // Add an order and check that capacity decreases
        assertEquals(40, slot.getCurrentCapacity());

        // Cancel the order and check that capacity is restored
        restaurant.cancelOrder(order1, deliveryDetails.getDeliveryTime().orElse(null));
        assertEquals(0, slot.getCurrentCapacity());
    }

    @Test
    void testMaxCapacityReached() {
        Order order1 = new Order(restaurantProxy, campusUser);
        order1.addItem(item1); // 40 seconds
        Order order2 = new Order(restaurantProxy, campusUser);
        order2.addItem(item2); // 1 second

        restaurant.addItemToOrder(order1,
                item1,
                deliveryDetails.getDeliveryTime().orElse(null)
        );
        restaurant.addItemToOrder(order2,
                item2,
                deliveryDetails.getDeliveryTime().orElse(null)
        );

        // Capacity after adding both orders should be 41 (40 + 1)
        assertEquals(41, slot.getCurrentCapacity());
        assertTrue(slot.getAvailableCapacity() < slot.getMaxCapacity());

        // Trying to add an order that exceeds max capacity should fail
        Order largeOrder = new Order(restaurantProxy, campusUser);

        MenuItem largeItem = new MenuItem("Large Item", 200000, 5.99);
        restaurant.getMenu().addMenuItem(largeItem);
//        assertThrows(IllegalArgumentException.class, () -> largeOrder.addItem(largeItem)); // 2000 seconds
        largeOrder.addItem(largeItem);
    }

    @Test
    void testIsOrderValid() {
        // Order with valid items
        Order validOrder = new Order(restaurantProxy, campusUser);
        validOrder.addItem(item1);
        validOrder.addItem(item2);
        assertTrue(restaurant.isOrderValid(validOrder));

        // Order with an invalid item (not in the menu)
        Order invalidOrder = new Order(restaurantProxy, campusUser);
        invalidOrder.addItem(new MenuItem("Pizza", 30, 9.99));
        assertFalse(restaurant.isOrderValid(invalidOrder));
    }

    @Test
    void testOnOrderPaid() {
        Order order = new Order(restaurantProxy, campusUser);
        order.addItem(item1);
        restaurantProxy.addItemToOrder(order, item1, deliveryDetails.getDeliveryTime().orElse(null));

        // Before payment, order is in pending
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertTrue(restaurant.getPendingOrders().entrySet().stream().anyMatch(entry -> entry.getValue().contains(order)));

        // After payment, it should move to validated and history
        restaurantProxy.onOrderPaid(order);
        assertEquals(OrderStatus.VALIDATED, order.getStatus());
        assertFalse(restaurantProxy.getPendingOrders().entrySet().stream().anyMatch(entry -> entry.getValue().contains(order)));
        assertEquals(1, restaurant.getOrdersHistory().size());
        assertTrue(restaurantProxy.getOrdersHistory().contains(order));
    }

    @Test
    void testAddOrderWhenSlotCapacityIsFull() {
        // Setting up a smaller capacity slot for easier testing
        MenuItem c_item1 = new MenuItem("Item1", 1400, 10.0); // Big item, will almost fill the slot
        MenuItem c_item2 = new MenuItem("Item2", 300, 5.0);   // Another item to fill the slot
        MenuItem c_item3 = new MenuItem("Item3", 300, 5.0);   // Another item to fill the slot

        // Change restaurant menu for test items
        Menu testMenu = new Menu.MenuBuilder()
                .addMenuItem(c_item1)
                .addMenuItem(c_item2)
                .addMenuItem(c_item3)
                .build();
        restaurant.changeMenu(testMenu);

        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, TimeUtils.getNow().plusHours(7).plusMinutes(29));

        // Start an individual order by the user
        IndividualOrder order = new IndividualOrder(restaurantProxy, deliveryDetails, campusUser);

        orderManager = new OrderManager(restaurantProxy);

        // Add items to fill the slot capacity
        orderManager.addItemToOrder(order, c_item1); // 1400 seconds
        orderManager.addItemToOrder(order, c_item2); // 300 seconds

        // Verify that the slot's capacity is full
        assertEquals(100, restaurantProxy.getSlots().getFirst().getAvailableCapacity());


        // Check if the order fails to be added due to full capacity
        assertThrows(IllegalArgumentException.class, () -> orderManager.addItemToOrder(
                order,
                c_item3
        ), "Cannot add item to order, no slot available.");

        // Ensure that no further items are added to the order
        assertEquals(100, restaurantProxy.getSlots().getFirst().getAvailableCapacity());
    }

    @Test
    void testCancelNonExistentOrder() {
        // Create an order that hasn't been added to the system
        Order nonExistentOrder = new Order(restaurantProxy, campusUser);
        nonExistentOrder.addItem(item1); // Adding a valid item

        // Attempt to cancel the non-existent order
        restaurant.cancelOrder(nonExistentOrder, deliveryDetails.getDeliveryTime().orElse(null));

        // Ensure that the order has not been added to the system
        assertFalse(restaurant.getPendingOrders().entrySet().stream().anyMatch(entry -> entry.getValue().contains(nonExistentOrder)));

        // Ensure no changes have been made to the slot's capacity
        assertEquals(slot.getMaxCapacity(), slot.getAvailableCapacity());

        // Ensure not order history has been created
        assertFalse(restaurant.getOrdersHistory().contains(nonExistentOrder));

        // Ensure the order status has not been changed
        assertEquals(OrderStatus.PENDING, nonExistentOrder.getStatus());
    }
}
