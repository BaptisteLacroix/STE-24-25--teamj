package fr.unice.polytech.equipe.j.restaurant;

public class MenuItem {
    private final String name;
    private String description;
    private int prepTime;
    private double price;

    public MenuItem(String name, String description, int prepTime, double price) {
        this.name = name;
        this.prepTime = prepTime;
        this.price = price;
        this.description = description;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public String getName() {
        return name;
    }


    public int getPrepTime() {
        return prepTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public String toString() {
        return name + " - " + price + " EUR";
    }
}
