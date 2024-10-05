package fr.unice.polytech.equipe.j.equipe.j.restaurant;

public class MenuItem {
    private String itemName;
    private double price;

    public MenuItem(String name, double price) {
        this.itemName = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return itemName + " - " + price + "€";
    }
}
