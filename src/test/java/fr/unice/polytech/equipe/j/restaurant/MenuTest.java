package fr.unice.polytech.equipe.j.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuTest {

    private Menu.MenuBuilder menuBuilder;
    private MenuItem item1;
    private MenuItem item2;

    @BeforeEach
    void setUp() {
        menuBuilder = new Menu.MenuBuilder();
        item1 = new MenuItem("Pizza", 12.99);
        item2 = new MenuItem("Pasta", 8.99);
    }

    @Test
    void testAddMenuItem() {
        Menu menu = menuBuilder.addMenuItem(item1).build();
        assertEquals(1, menu.getItems().size());
        assertTrue(menu.getItems().contains(item1));
    }

    @Test
    void testAddMenuItems() {
        Menu menu = menuBuilder.addMenuItems(List.of(item1, item2)).build();
        assertEquals(2, menu.getItems().size());
        assertTrue(menu.getItems().containsAll(List.of(item1, item2)));
    }

    @Test
    void testFindItemByName() {
        Menu menu = menuBuilder.addMenuItem(item1).build();
        MenuItem foundItem = menu.findItemByName("Pizza");
        assertEquals(item1, foundItem);
    }

    @Test
    void testFindItemByName_NotFound() {
        Menu menu = menuBuilder.addMenuItem(item1).build();
        MenuItem foundItem = menu.findItemByName("Sushi");
        assertNull(foundItem);  // "Sushi" is not in the menu
    }

    @Test
    void testUnmodifiableMenu() {
        Menu menu = menuBuilder.addMenuItem(item1).build();
        List<MenuItem> items = menu.getItems();
        assertThrows(UnsupportedOperationException.class, () -> items.add(item2));  // Ensure list is unmodifiable
    }
}
