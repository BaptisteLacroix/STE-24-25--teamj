package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;
import java.util.Date;
import java.util.List;

public class Restaurant {

    private List<DiscountStrategy> discountStrategies;
    private List<Menu> menus;

    public Restaurant(List<DiscountStrategy> discountStrategies, List<Menu> menus) {
        this.discountStrategies = discountStrategies;
        this.menus = menus;
    }

    // Method to calculate the price of an order
    public double calculatePrice(Order order) {
        double basePrice = order.getBasePrice();
        double finalPrice = basePrice;

        for (DiscountStrategy strategy : discountStrategies) {
            finalPrice = strategy.applyDiscount(finalPrice);
        }

        return finalPrice;
    }

    // Method to get the list of menus
    public List<Menu> getMenus() {
        return menus;
    }

    public List<Restaurant> getRestaurants() {
        return null;
    }

    // Method to get production capacity starting from a given moment
    public int getProductionCapacity(Date startMoment) {
        return 0;
    }

    public Date addOrderToProductionLine(Order order) {
        return new Date(); // Placeholder
    }

    public List<DiscountStrategy> getDiscountStrategies() {
        return discountStrategies;
    }

    public void setDiscountStrategies(List<DiscountStrategy> discountStrategies) {
        this.discountStrategies = discountStrategies;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
}
