package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderPriceStrategyFactory {
    public static OrderPriceStrategy makeSubstractKpercentforNOrder(int k, int n) {
        return (ConnectedUser user, Order order, Restaurant restaurant)-> {
            // get number of user previous commands
            long previousGroupOrders = restaurant.groupOrdersHistory().stream()
                    .filter(groupOrder -> groupOrder.orderToConnectedUserMap().containsValue(user))
                    .count();
            long previousIndividualOrders =  restaurant.individualOrdersHistory().stream()
                    .filter(individualOrder -> individualOrder.user().equals(user))
                    .count();

            // map order menuItems to their prices
            var prices = order.items.stream().collect(Collectors.toMap((item)->item, MenuItem::price));
            double totalPrice = prices.values().stream().mapToInt(Integer::intValue).sum();

            String description = "No discount";
            // if the order is dividable by n, apply k% discount
            if ((previousGroupOrders + previousIndividualOrders + 1) % n == 0) {
                totalPrice = totalPrice - totalPrice * ((float)k/100.0);
                description = "Price is reduced by " + k + " percent since it's " + user +
                        "'s " + (previousGroupOrders + previousIndividualOrders + 1) + "'th command";
            }

            return new OrderPrice(
                    prices,
                    totalPrice,
                    description
            );
        };
    }

    public static OrderPriceStrategy makeGiveItemForNItems(int n) {
        return (ConnectedUser user, Order order, Restaurant restaurant)-> {
            var prices = order.items.stream().collect(Collectors.toMap((item)->item, MenuItem::price));
            Map.Entry<MenuItem, Integer> min = null;
            for (Map.Entry<MenuItem, Integer> entry : prices.entrySet()) {
                if (min == null || min.getValue() > entry.getValue()) {
                    min = entry;
                }
            }
            double totalPrice = prices.values().stream().mapToInt(Integer::intValue).sum();

            // if there is no elements or less that n elements, don't change a thing
            if (min == null || prices.size() < n) {
                return new OrderPrice(
                        prices,
                        totalPrice,
                        "No discount since there are less than " + n + " items"
                );
            }

            prices.put(min.getKey(), 0);
            return new OrderPrice(
                    prices,
                    totalPrice,
                    "The item " + min.getKey() + " is free because there are at least " + n + " items"
            );
        };
    }
}
