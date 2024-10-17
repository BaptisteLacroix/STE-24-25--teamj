package fr.unice.polytech.equipe.j.restaurant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuItemTest {

    @Test
    void testCreateMenuItem() {
        MenuItem item = new MenuItem("Burger", 20, 5.99);
        assertEquals("Burger", item.getName());
        assertEquals(5.99, item.getPrice());
    }

    @Test
    void testToString() {
        MenuItem item = new MenuItem("Burger", 20, 5.99);
        assertEquals("Burger - 5.99 EUR", item.toString());
    }
}
