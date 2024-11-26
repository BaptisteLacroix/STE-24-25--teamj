package fr.unice.polytech.equipe.j.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RestaurantDTO {
    private UUID uuid;
    private String restaurantName;
    private String openingTime;
    private String closingTime;
    private List<SlotDTO> slots;
    private MenuDTO menu;
    private OrderPriceStrategyDTO orOrderPriceStrategy;

    public RestaurantDTO() {
    }

    public RestaurantDTO(UUID uuid, String restaurantName, String openingTime, String closingTime, List<SlotDTO> slots, MenuDTO menu) {
        this.uuid = uuid;
        this.restaurantName = restaurantName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.slots = slots;
        this.menu = menu;
    }
}
