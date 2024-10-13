package fr.unice.polytech.equipe.j.tests.restaurant;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuItemTest {

    @Test
    void testCreateMenuItem() {
        MenuItem item = new MenuItem("Burger", 5.99);
        assertEquals("Burger", item.name());
        assertEquals(5.99, item.price());
    }

    @Test
    void testToString() {
        MenuItem item = new MenuItem("Burger", 5.99);
        assertEquals("Burger - 5.99 EUR", item.toString());
    }
}
