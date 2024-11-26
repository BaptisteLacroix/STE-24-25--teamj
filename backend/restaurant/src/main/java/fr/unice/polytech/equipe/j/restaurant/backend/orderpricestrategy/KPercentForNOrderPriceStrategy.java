package fr.unice.polytech.equipe.j.restaurant.backend.orderpricestrategy;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.restaurant.backend.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.backend.menu.MenuItem;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.util.Map;
import java.util.stream.Collectors;

public class KPercentForNOrderPriceStrategy implements OrderPriceStrategy {

    private final double discountReduction;
    private final double orderNumber;

    public KPercentForNOrderPriceStrategy(double k, int n) {
        this.discountReduction = k;
        this.orderNumber = n;
    }

    @Override
    public OrderPrice processOrderPrice(Order order, IRestaurant restaurant) {
        CampusUser user = order.getUser();
        long previousOrders = restaurant.getOrdersHistory().stream()
                .filter((o) -> o.getUser().equals(user))
                .count();

        // map order menuItems to their prices
        Map<MenuItem, Double> prices = order.getItems().stream().collect(Collectors.toMap((item) -> item, MenuItem::getPrice));
        double totalPrice = prices.values().stream().mapToDouble(Double::doubleValue).sum();

        String description = "No discount";
        // if the order is dividable by n, apply k% discount
        if ((previousOrders + 1) % orderNumber == 0) {
            totalPrice = totalPrice - totalPrice * (discountReduction / 100.0);
            description = "Price is reduced by " + discountReduction + " percent since it's " + user +
                    "'s " + (previousOrders + 1) + "'th command";
        }

        return new OrderPrice(
                prices,
                totalPrice,
                description
        );
    }
}
