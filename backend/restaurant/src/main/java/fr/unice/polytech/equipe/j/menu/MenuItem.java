package fr.unice.polytech.equipe.j.menu;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MenuItem {
    private UUID uuid;
    private final String name;
    private int prepTime;
    private double price;

    public MenuItem(UUID uuid, String name, int prepTime, double price) {
        this.uuid = uuid;
        this.name = name;
        this.prepTime = prepTime;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " - " + price + " EUR" + " - " + prepTime + "s";
    }
}
