package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.Menu;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.user.ConnectedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class ConnectedUserTest {

    @Mock
    private ConnectedUser user;
    private MenuItem item1;
    private MenuItem item2;
    private Transaction transaction;
    private RestaurantProxy noMockRestaurantProxy;
    private ConnectedUser noMockUser;
    private Restaurant noMockRestaurant;

    @BeforeEach
    void setUp() {
        user = spy(new ConnectedUser("user@test.com", "password", 100.0));
        item1 = new MenuItem("Burger", 5.99);
        item2 = new MenuItem("Fries", 2.99);
        transaction = mock(Transaction.class);
        noMockUser = new ConnectedUser("user@email.com", "password", 100.0);
        noMockRestaurant = new Restaurant("Restaurant", LocalDateTime.now(), LocalDateTime.now(), null);
        noMockRestaurant.changeMenu(new Menu(List.of(item1, item2)));
        noMockRestaurantProxy = new RestaurantProxy();
    }

    @Test
    void testStartOrder() {
        noMockUser.startIndividualOrder(noMockRestaurantProxy, noMockRestaurant.getRestaurantId());
        noMockUser.addItemToOrder(noMockRestaurantProxy, noMockRestaurant.getRestaurantId(), item1);
        noMockUser.addItemToOrder(noMockRestaurantProxy, noMockRestaurant.getRestaurantId(), item2);

        // Assuming OrderBuilder works correctly, no exception should be thrown
        assertDoesNotThrow(() -> noMockUser.addItemToOrder(noMockRestaurantProxy, noMockRestaurant.getRestaurantId(), item1));
    }

    @Test
    void testProceedCheckout() {
        // Spy on the ConnectedUser instance to track method calls
        noMockUser.startIndividualOrder(noMockRestaurantProxy, noMockRestaurant.getRestaurantId());

        noMockUser.addItemToOrder(noMockRestaurantProxy, noMockRestaurant.getRestaurantId(), item1);

        // Simulate the transaction checkout
        doNothing().when(transaction).proceedCheckout(any());

        noMockUser.proceedIndividualOrderCheckout();

        // Verify that the status of the order change
        assertEquals(OrderStatus.VALIDATED, noMockUser.getOrdersHistory().getLast().getStatus());
    }


    @Test
    void testProceedCheckout_WithoutOrder_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> user.proceedIndividualOrderCheckout());
        assertEquals("Order with id: null not found.", exception.getMessage());
    }

    @Test
    void testNotifyCheckoutSuccess() {
        Order order = mock(Order.class);
        user.notifyCheckoutSuccess(order);

        // Ensure order history is updated and the current order is cleared
        assertTrue(user.getOrdersHistory().contains(order));
    }

    @Test
    void testGetOrdersHistory() {
        Order order = mock(Order.class);
        user.notifyCheckoutSuccess(order);
        List<Order> history = user.getOrdersHistory();

        assertEquals(1, history.size());
        assertTrue(history.contains(order));
    }

    @Test
    void testToString() {
        when(user.getOrdersHistory()).thenReturn(List.of());
        when(user.getEmail()).thenReturn("user@test.com");
        when(user.getAccountBalance()).thenReturn(100.0);
        assertEquals("user@test.com - 100.0€ - 0 orders", user.toString());

        // After adding an order to history
        Order order = mock(Order.class);
        user.notifyCheckoutSuccess(order);
        when(user.getOrdersHistory()).thenReturn(List.of(order));
        assertEquals("user@test.com - 100.0€ - 1 orders", user.toString());
    }
}
