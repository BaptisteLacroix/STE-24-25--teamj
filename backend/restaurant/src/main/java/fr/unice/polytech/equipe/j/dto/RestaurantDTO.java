package fr.unice.polytech.equipe.j.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Map<UUID, Set<UUID>> pendingOrders;

    public RestaurantDTO() {
    }

    @Override
    @JsonIgnore
    public String toString() {
        return "RestaurantDTO{" +
                "id=" + id +
                ", restaurantName='" + restaurantName + '\'' +
                ", menu=" + menu +
                ", slots=" + slots +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                ", orOrderPriceStrategy=" + orOrderPriceStrategy +
                ", pendingOrders=" + pendingOrders +
                '}';
    }
}

