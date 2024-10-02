package fr.unice.polytech.equipe.j.restaurant;

public class NoDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double price) {
        return price; // No discount applied
    }

    @Override
    public String getDescription() {
        return "No discount applied.";
    }
}