package fr.unice.polytech.equipe.j.menu;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MenuItem {
    private UUID uuid;
    private String name;
    private int prepTime;
    private double price;

    public MenuItem(UUID uuid, String name, int prepTime, double price) {
        this.uuid = uuid;
        this.name = name;
        this.prepTime = prepTime;
        this.price = price;
    }

    public MenuItem() {

    }

    @Override
    public String toString() {
        return uuid + " - " + name + " - " + price + " EUR" + " - " + prepTime + "s";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MenuItem)) {
            return false;
        }
        MenuItem menuItem = (MenuItem) obj;
        return menuItem.getUuid().equals(uuid) && menuItem.getName().equals(name) && menuItem.getPrepTime() == prepTime && menuItem.getPrice() == price;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
