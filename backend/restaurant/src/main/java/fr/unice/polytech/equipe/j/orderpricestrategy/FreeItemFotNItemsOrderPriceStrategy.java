package fr.unice.polytech.equipe.j.orderpricestrategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import fr.unice.polytech.equipe.j.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.dto.Order;
import fr.unice.polytech.equipe.j.mapper.DTOMapper;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;
import fr.unice.polytech.equipe.j.menu.MenuItem;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class FreeItemFotNItemsOrderPriceStrategy implements OrderPriceStrategy{

    @JsonProperty("n")
    private final int n;

    public FreeItemFotNItemsOrderPriceStrategy(int n) {
        this.n = n;
    }

    @Override
    public OrderPrice processOrderPrice(Order order, IRestaurant restaurant) {
        Map<MenuItem, Double> prices = order.getItems()
                .stream()
                .collect(Collectors.toMap(
                        DTOMapper::toMenuItem,
                        MenuItemDTO::getPrice
                ));

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
