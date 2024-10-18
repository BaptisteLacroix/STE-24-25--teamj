package fr.unice.polytech.equipe.j.restaurant;

public class MenuItem {
    private final String name;
    private String description;
    private int prepTime;
    private double price;
    private int capacity;

    public MenuItem(String name, String description, int prepTime, double price, int capacity) {
        this.name = name;
        this.prepTime = prepTime;
        this.price = price;
        this.capacity = capacity;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public String toString() {
        return name + " - " + price + " EUR";
    }
}
