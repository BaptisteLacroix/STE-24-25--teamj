package fr.unice.polytech.equipe.j.restaurant;


public class ProductOffer implements DiscountStrategy {
    private double discountAmount;

    public ProductOffer(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public double applyDiscount(double price) {
        return price - discountAmount;
    }

    @Override
    public String getDescription() {
        return "Fixed product offer discount of " + discountAmount;
    }
}