package fr.unice.polytech.equipe.j.restaurant;

public class ReductionAfterNOrder implements DiscountStrategy {
    private int orderCount;
    private int requiredOrders;
    private double discountRate;

    public ReductionAfterNOrder(int requiredOrders, double discountRate) {
        this.requiredOrders = requiredOrders;
        this.discountRate = discountRate;
    }

    @Override
    public double applyDiscount(double price) {
        return price;
    }

    @Override
    public String getDescription() {
        return "Discount after " + requiredOrders + " orders with a rate of " + discountRate * 100 + "%";
    }

    public void incrementOrderCount() {
        this.orderCount++;
    }
}