package fr.unice.polytech.equipe.j.stepdefs.backend;

import fr.unice.polytech.equipe.j.restaurant.Menu;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;

public class Utils {
    public static Menu createMenuFromString(String menuItems) {
        Menu.MenuBuilder builder = new Menu.MenuBuilder();
        for (String item : menuItems.split("\", ")) {
            String[] parts = item.replace("\"", "").split(", ");
            builder.addMenuItem(new MenuItem(parts[0], Double.parseDouble(parts[1])));
        }
        return builder.build();
    }
}
