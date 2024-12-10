package fr.unice.polytech.equipe.j.order.grouporder.dto;

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


    public RestaurantDTO() {
    }

    public RestaurantDTO(UUID restaurantId, String restaurantName, LocalDateTime openingTime, LocalDateTime closingTime) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
}
