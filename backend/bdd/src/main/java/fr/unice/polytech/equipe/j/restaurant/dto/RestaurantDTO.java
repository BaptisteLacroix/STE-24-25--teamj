package fr.unice.polytech.equipe.j.restaurant.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RestaurantDTO {
    @NonNull
    private UUID id;
    @NonNull
    private String restaurantName;
    private MenuDTO menu;
    @NonNull
    private List<SlotDTO> slots;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private OrderPriceStrategyDTO orOrderPriceStrategy;

    public RestaurantDTO() {
    }

    public RestaurantDTO(UUID id, String restaurantName, LocalDateTime openingTime, LocalDateTime closingTime, List<SlotDTO> slots, MenuDTO menu) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.slots = slots;
        this.menu = menu;
    }
}
