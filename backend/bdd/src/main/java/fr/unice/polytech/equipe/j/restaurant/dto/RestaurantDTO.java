package fr.unice.polytech.equipe.j.restaurant.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.unice.polytech.equipe.j.order.dto.OrderDTO;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    @JsonSerialize
    @JsonDeserialize
    private Map<UUID, Set<UUID>> pendingOrders;

    public RestaurantDTO() {
    }

    public RestaurantDTO(UUID id, String restaurantName, LocalDateTime openingTime, LocalDateTime closingTime, List<SlotDTO> slots, MenuDTO menu,Map<UUID, Set<UUID>> pendingOrders) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.slots = slots;
        this.menu = menu;
        this.pendingOrders = pendingOrders;
    }
}
