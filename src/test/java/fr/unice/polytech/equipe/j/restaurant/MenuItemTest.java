package fr.unice.polytech.equipe.j.restaurant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuItemTest {

    @Test
    void testCreateMenuItem() {
        MenuItem item = new MenuItem("Burger", "orem ipsum", 10,5.99);
        assertEquals("Burger", item.getName());
        assertEquals(5.99, item.getPrice());
    }

    @Test
    void testToString() {
        MenuItem item = new MenuItem("Burger", "lorem ipsum",8,5.99);
        assertEquals("Burger - 5.99 EUR", item.toString());
    }
}
