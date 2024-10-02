package fr.unice.polytech.equipe.j.restaurant;

import java.util.List;

public class Menu {
    private String name;
    private  double totalPrepTime;
    private double totalPrice;

    List<MenuItem> menuItemList;

    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }

    public void setMenuItemList(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }

    public Menu(String name, double totalPrepTime, double totalPrice) {
        this.name = name;
        this.totalPrepTime = totalPrepTime;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalPrepTime() {
        return totalPrepTime;
    }

    public void setTotalPrepTime(double totalPrepTime) {
        this.totalPrepTime = totalPrepTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }


}
