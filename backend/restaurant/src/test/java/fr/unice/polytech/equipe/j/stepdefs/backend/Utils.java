package fr.unice.polytech.equipe.j.stepdefs.backend;

import fr.unice.polytech.equipe.j.restaurant.backend.menu.Menu;
import fr.unice.polytech.equipe.j.restaurant.backend.menu.MenuItem;

public class Utils {
    public static Menu createMenuFromString(String menuItems) {
        Menu.MenuBuilder builder = new Menu.MenuBuilder();
        for (String item : menuItems.split("\", ")) {
            String[] parts = item.replace("\"", "").split(", ");
            String name;
            int prepTime;
            double price;
            try {
                name = parts[0];
            } catch (Exception e) {
                name = "default name since parsing menu threw exception";
            }
            try {
                prepTime = Integer.parseInt(parts[2]);
            } catch (Exception e) {
                prepTime = -1;
            }
            try {
                price = Double.parseDouble(parts[1]);
            } catch (Exception e) {
                price = 0.0;
            }
            builder.addMenuItem(new MenuItem(name, prepTime, price));
        }
        return builder.build();
    }
}
