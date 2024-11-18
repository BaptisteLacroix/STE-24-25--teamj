package fr.unice.polytech.equipe.j.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RestaurantDTO {
    private UUID restaurantId;
    private String restaurantName;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private List<SlotDTO> slots;
    private MenuDTO menu;
    private OrderPriceStrategyDTO orOrderPriceStrategy;

    public RestaurantDTO(UUID restaurantId, String restaurantName, LocalDateTime openingTime, LocalDateTime closingTime, List<SlotDTO> slots, MenuDTO menu) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.slots = slots;
        this.menu = menu;
    }
}
