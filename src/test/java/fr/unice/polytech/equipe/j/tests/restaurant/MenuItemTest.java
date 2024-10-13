package fr.unice.polytech.equipe.j.tests.restaurant;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuItemTest {

    @Test
    void testCreateMenuItem() {
        MenuItem item = new MenuItem("Burger", 10,5.99,60);
        assertEquals("Burger", item.getName());
        assertEquals(5.99, item.getPrice());
    }

    @Test
    void testToString() {
        MenuItem item = new MenuItem("Burger", 8,5.99,6);
        assertEquals("Burger - 5.99 EUR", item.toString());
    }
}
