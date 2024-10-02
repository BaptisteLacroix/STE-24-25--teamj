package fr.unice.polytech.equipe.j.restaurant;

public class MenuItem {
    private String name;
    private double prepTime;
    private float Price;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(double prepTime) {
        this.prepTime = prepTime;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public MenuItem(String name, float price, double prepTime) {
        this.name = name;
        Price = price;
        this.prepTime = prepTime;
    }
}
