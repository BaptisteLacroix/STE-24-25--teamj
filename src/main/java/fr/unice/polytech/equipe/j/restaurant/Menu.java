package fr.unice.polytech.equipe.j.restaurant;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private List<MenuItem> menuItems;

    public Menu() {
        this.menuItems = new ArrayList<>();
    }

    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }


    // Trouver un élément du menu par nom
    public MenuItem findMenuItemByName(String name) {
        for (MenuItem item : menuItems) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    // Obtenir tous les éléments du menu
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }
}