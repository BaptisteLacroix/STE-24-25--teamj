package fr.unice.polytech.equipe.j.restaurant;

public class MenuItem {
    private final String name;
    private int prepTime;
    private double price;

    public MenuItem(String name, int prepTime, double price) {
        this.name = name;
        this.prepTime = prepTime;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getPrepTime() {
        return prepTime;
    }

    @Override
    public String toString() {
        return name + " - " + price + " EUR";
    }
}
