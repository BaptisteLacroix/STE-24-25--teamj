package fr.unice.polytech.equipe.j.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantTest {

    private Restaurant restaurant;
    private MenuItem item1;
    private MenuItem item2;
    private Menu menu;

    @BeforeEach
    void setUp() {
        item1 = new MenuItem("Burger", "lorem ipsum",40,5.99, 8);
        item2 = new MenuItem("Fries", "lorem ipsum",1,2.99, 8);
        menu = new Menu.MenuBuilder().addMenuItems(List.of(item1, item2)).build();
        restaurant = new Restaurant("Test Restaurant", new ArrayList<>(), LocalDateTime.of(2024, 10, 1, 9, 0), LocalDateTime.of(2024, 10, 1, 21, 0), menu);
    }
    
    @Test
    void testChangeMenu() {
        Menu newMenu = new Menu.MenuBuilder().addMenuItem(new MenuItem("Salad", "lorem ipsum",10,4.99, 8)).build();
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
