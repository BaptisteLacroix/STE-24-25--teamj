package fr.unice.polytech.equipe.j.restaurant;

public interface DiscountStrategy {
    double applyDiscount(double price);
    String getDescription();
}
