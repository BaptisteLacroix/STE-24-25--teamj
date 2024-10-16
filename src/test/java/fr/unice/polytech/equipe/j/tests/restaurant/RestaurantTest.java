package fr.unice.polytech.equipe.j.tests.restaurant;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.restaurant.Menu;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestaurantTest {

    private Restaurant restaurant;
    private MenuItem item1;
    private MenuItem item2;
    private Menu menu;

    @BeforeEach
    void setUp() {
        item1 = new MenuItem("Burger", 40,5.99);
        item2 = new MenuItem("Fries", 1,2.99);
        menu = new Menu.MenuBuilder().addMenuItems(List.of(item1, item2)).build();
        restaurant = new Restaurant("Test Restaurant", new ArrayList<>(), LocalDateTime.of(2024, 10, 1, 9, 0), LocalDateTime.of(2024, 10, 1, 21, 0), menu);
    }

    @Test
    void testCalculatePrice() {
        // Mock an Order
        Order order = mock(Order.class);
        when(order.getItems()).thenReturn(List.of(item1, item2));

        double totalPrice = restaurant.calculatePrice(order);
        assertEquals(8.98, totalPrice);
    }

    @Test
    void testChangeMenu() {
        Menu newMenu = new Menu.MenuBuilder().addMenuItem(new MenuItem("Salad", 10,4.99)).build();
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
}
