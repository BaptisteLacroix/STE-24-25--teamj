package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.order.OrderStatus;
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

    private Restaurant restaurant;
    private MenuItem item1;
    private MenuItem item2;
    private Menu menu;
    private Clock clock;
    private Slot slot;
    private RestaurantManager manager;
    private OrderManager orderManager;
    private CampusUser campusUser;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2024-10-01T07:00:00Z"), ZoneId.of("Europe/Paris"));
        item1 = new MenuItem("Burger", "lorem ipsum", 40, 5.99);
        item2 = new MenuItem("Fries", "lorem ipsum", 1, 2.99);
        menu = new Menu.MenuBuilder().addMenuItems(List.of(item1, item2)).build();

        restaurant = new Restaurant("Test Restaurant",
                LocalDateTime.of(2024, 10, 1, 9, 0),
                LocalDateTime.of(2024, 10, 1, 21, 0), menu, clock);

        manager = new RestaurantManager(
                "email",
                "password",
                "Manager",
                restaurant);

        manager.updateNumberOfPersonnel(manager.getRestaurant().getSlots().getFirst(), 1);
        slot = restaurant.getSlots().getFirst();
        orderManager = new OrderManager(clock);
        campusUser = new CampusUser("user@example.com", "password123", orderManager);
    }

    @Test
    void testChangeMenu() {
        Menu newMenu = new Menu.MenuBuilder().addMenuItem(new MenuItem("Salad", "lorem ipsum", 10, 4.99)).build();
        restaurant.changeMenu(newMenu);
        assertEquals(newMenu, restaurant.getMenu());
    }

    @Test
    void testOpeningAndClosingTime() {
        assertEquals(LocalDateTime.of(2024, 10, 1, 9, 0), restaurant.getOpeningTime());
        assertEquals(LocalDateTime.of(2024, 10, 1, 21, 0), restaurant.getClosingTime());

        restaurant.setOpeningTime(LocalDateTime.of(2024, 10, 1, 8, 0));
        restaurant.setClosingTime(LocalDateTime.of(2024, 10, 1, 22, 0));

        assertEquals(LocalDateTime.of(2024, 10, 1, 8, 0), restaurant.getOpeningTime());
        assertEquals(LocalDateTime.of(2024, 10, 1, 22, 0), restaurant.getClosingTime());
    }

    @Test
    void testCapacityCheck() {
        assertTrue(restaurant.capacityCheck());

        assertEquals(slot.getMaxCapacity(), slot.getAvailableCapacity());

        // Add an order to reduce capacity
        Order order = new Order(restaurant, clock);
        order.addItem(item1); // 40 seconds
        restaurant.addOrder(order);

        // Check if the capacity has decreased
        assertEquals(slot.getMaxCapacity() - item1.getPrepTime(), slot.getAvailableCapacity());

        // Cancel the order and check capacity is restored
        restaurant.cancelOrder(order);
        assertEquals(slot.getMaxCapacity(), slot.getAvailableCapacity());
    }

    @Test
    void testAddAndCancelOrder() {
        Order order1 = new Order(restaurant, clock);
        order1.addItem(item1); // 40 seconds

        // Add an order and check that capacity decreases
        restaurant.addOrder(order1);
        assertEquals(slot.getCurrentCapacity(), 40);

        // Cancel the order and check that capacity is restored
        restaurant.cancelOrder(order1);
        assertEquals(slot.getCurrentCapacity(), 0);
    }

    @Test
    void testMaxCapacityReached() {
        Order order1 = new Order(restaurant, clock);
        order1.addItem(item1); // 40 seconds
        Order order2 = new Order(restaurant, clock);
        order2.addItem(item2); // 1 second

        restaurant.addOrder(order1);
        restaurant.addOrder(order2);

        // Capacity after adding both orders should be 41 (40 + 1)
        assertEquals(41, slot.getCurrentCapacity());
        assertTrue(slot.getAvailableCapacity() < slot.getMaxCapacity());

        // Trying to add an order that exceeds max capacity should fail
        Order largeOrder = new Order(restaurant, clock);
        largeOrder.addItem(new MenuItem("Pizza", "A large pizza", 2000, 10.99)); // 2000 seconds
        assertFalse(slot.UpdateSlotCapacity(largeOrder.getItems().get(0)));
    }

    @Test
    void testIsOrderValid() {
        // Order with valid items
        Order validOrder = new Order(restaurant, clock);
        validOrder.addItem(item1);
        validOrder.addItem(item2);
        assertTrue(restaurant.isOrderValid(validOrder));

        // Order with an invalid item (not in the menu)
        Order invalidOrder = new Order(restaurant, clock);
        invalidOrder.addItem(new MenuItem("Pizza", "lorem ipsum", 30, 9.99));
        assertFalse(restaurant.isOrderValid(invalidOrder));
    }

    @Test
    void testOrderPaid() {
        Order order = new Order(restaurant, clock);
        order.addItem(item1);
        restaurant.addOrder(order);

        // Before payment, order is in pending
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertTrue(restaurant.getPendingOrders().contains(order));

        // After payment, it should move to validated and history
        restaurant.orderPaid(order);
        assertEquals(OrderStatus.VALIDATED, order.getStatus());
        assertFalse(restaurant.getPendingOrders().contains(order));
        assertTrue(restaurant.getOrdersHistory().contains(order));
    }

    @Test
    void testAddOrderWhenSlotCapacityIsFull() {
        // Setting up a smaller capacity slot for easier testing
        MenuItem c_item1 = new MenuItem("Item1", "Test Item 1", 1700, 10.0); // Big item, will almost fill the slot
        MenuItem c_item2 = new MenuItem("Item2", "Test Item 2", 300, 5.0);   // Another item to fill the slot

        // Change restaurant menu for test items
        Menu testMenu = new Menu.MenuBuilder()
                .addMenuItem(c_item1)
                .addMenuItem(c_item2)
                .build();
        restaurant.changeMenu(testMenu);

        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, LocalDateTime.now(clock).plusHours(7).plusMinutes(29));

        // Start an individual order by the user
        campusUser.startIndividualOrder(restaurant, deliveryDetails);

        // Add items to fill the slot capacity
        campusUser.addItemToOrder(restaurant, c_item1); // 1700 seconds
        campusUser.addItemToOrder(restaurant, c_item2); // 300 seconds

        // Verify that the slot's capacity is full
        assertEquals(0, slot.getCurrentCapacity());

        // Now try to add another item that would exceed the slot capacity
        MenuItem exceedingItem = new MenuItem("Exceeding Item", "This item will exceed capacity", 100, 10.0);

        // Check if the order fails to be added due to full capacity
        assertThrows(IllegalArgumentException.class, () -> campusUser.addItemToOrder(restaurant, exceedingItem));

        // Ensure that no further items are added to the order
        assertEquals(0, slot.getCurrentCapacity());
    }

    @Test
    void testCancelNonExistentOrder() {
        // Create an order that hasn't been added to the system
        Order nonExistentOrder = new Order(restaurant, clock);
        nonExistentOrder.addItem(item1); // Adding a valid item

        // Attempt to cancel the non-existent order
        restaurant.cancelOrder(nonExistentOrder);

        // Ensure that the order has not been added to the system
        assertFalse(restaurant.getPendingOrders().contains(nonExistentOrder));

        // Ensure no changes have been made to the slot's capacity
        assertEquals(slot.getMaxCapacity(), slot.getAvailableCapacity());

        // Ensure not order history has been created
        assertFalse(restaurant.getOrdersHistory().contains(nonExistentOrder));

        // Ensure the order status has not been changed
        assertEquals(OrderStatus.PENDING, nonExistentOrder.getStatus());
    }
}
