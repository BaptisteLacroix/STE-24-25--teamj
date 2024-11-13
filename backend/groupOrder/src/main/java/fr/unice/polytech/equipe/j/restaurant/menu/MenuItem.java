package fr.unice.polytech.equipe.j.restaurant.menu;

public class MenuItem {
    private final String name;
    private int prepTime;
    private double price;

    public MenuItem(String name, int prepTime, double price) {
        this.name = name;
        this.prepTime = prepTime;
        this.price = price;
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


    @Override
    public String toString() {
        return name + " - " + price + " EUR" + " - " + prepTime + "s";
    }
}
