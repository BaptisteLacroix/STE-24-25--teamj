package fr.unice.polytech.equipe.j.restaurant;

public class MenuItem {
    private String name;
    private String description;
    private int prepTime;
    private int price;
    private int capacity;

    public MenuItem(String name, String description, int prepTime, int price, int capacity) {
        this.name = name;
        this.description = description;
        this.prepTime = prepTime;
        this.price = price;
        this.capacity = capacity;
    }

    public void setPrepTime(int prepTime){
        this.prepTime = prepTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
