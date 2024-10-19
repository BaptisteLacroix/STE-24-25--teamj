package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderPriceStrategyFactory {
    public static OrderPriceStrategy makeSubstractKpercentforNOrder(double k, int n) {
        return (Order order, Restaurant restaurant)-> {
            ConnectedUser user = order.getUser();
            long previousOrders =  restaurant.getOrderHistory().stream()
                    .filter((o) -> o.getUser().equals(user))
                    .count();

            // map order menuItems to their prices
            Map<MenuItem, Double> prices = order.getItems().stream().collect(Collectors.toMap((item)->item, MenuItem::price));
            double totalPrice = prices.values().stream().mapToDouble(Double::doubleValue).sum();

            String description = "No discount";
            // if the order is dividable by n, apply k% discount
            if ((previousOrders + 1) % n == 0) {
                totalPrice = totalPrice - totalPrice * (k/100.0);
                description = "Price is reduced by " + k + " percent since it's " + user +
                        "'s " + (previousOrders + 1) + "'th command";
            }

            return new OrderPrice(
                    prices,
                    totalPrice,
                    description
            );
        };
    }

    public static OrderPriceStrategy makeGiveItemForNItems(int n) {
        return (Order order, Restaurant restaurant)-> {
            ConnectedUser user = order.getUser();
            Map<MenuItem, Double> prices = order.getItems().stream().collect(Collectors.toMap((item)->item, MenuItem::price));
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
        };
    }
}
