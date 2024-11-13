package fr.unice.polytech.equipe.j.restaurant.orderpricestrategy;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.menu.MenuItem;

import java.util.Map;
import java.util.stream.Collectors;

public class FreeItemFotNItemsOrderPriceStrategy implements OrderPriceStrategy{

    private final int n;

    public FreeItemFotNItemsOrderPriceStrategy(int n) {
        this.n = n;
    }

    @Override
    public OrderPrice processOrderPrice(Order order, IRestaurant restaurant) {
        Map<MenuItem, Double> prices = order.getItems().stream().collect(Collectors.toMap((item)->item, MenuItem::getPrice));
        Map.Entry<MenuItem, Double> min = null;
        for (Map.Entry<MenuItem, Double> entry : prices.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        double totalPrice = prices.values().stream().mapToDouble(Double::doubleValue).sum();

        // if there is no elements or less that n elements, don't change a thing
        if (min == null || prices.size() < n) {
            return new OrderPrice(
                    prices,
                    totalPrice,
                    "No discount since there are less than " + n + " items"
            );
        }

        prices.put(min.getKey(), 0.0);
        return new OrderPrice(
                prices,
                totalPrice,
                "The item " + min.getKey() + " is free because there are at least " + n + " items"
        );
    }
}
