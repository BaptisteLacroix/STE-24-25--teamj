package fr.unice.polytech.equipe.j.orderpricestrategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.equipe.j.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.dto.OrderDTO;
import fr.unice.polytech.equipe.j.mapper.DTOMapper;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;
import fr.unice.polytech.equipe.j.menu.MenuItem;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class KPercentForNOrderPriceStrategy implements OrderPriceStrategy {
    @JsonProperty("discountReduction")
    private final double discountReduction;
    @JsonProperty("orderNumber")
    private final double orderNumber;

    public KPercentForNOrderPriceStrategy(double k, int n) {
        this.discountReduction = k;
        this.orderNumber = n;
    }

    @Override
    public OrderPrice processOrderPrice(OrderDTO orderDTO, IRestaurant restaurant) {
        UUID user = orderDTO.getUserId();
        long previousOrders = restaurant.getOrdersHistory().stream()
                .filter((o) -> o.getUserId().equals(user))
                .count();

        // map order menuItems to their prices
        Map<MenuItem, Double> prices = orderDTO.getItems().stream().collect(Collectors.toMap((item) -> DTOMapper.toMenuItem(item), MenuItemDTO::getPrice));
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
