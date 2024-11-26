package fr.unice.polytech.equipe.j.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RestaurantDTO {
    private UUID uuid;
    private String restaurantName;
    private MenuDTO menu;
    private List<SlotDTO> slots;
    private String openingTime;
    private String closingTime;
    private OrderPriceStrategyDTO orOrderPriceStrategy;

    public RestaurantDTO() {
    }
}

