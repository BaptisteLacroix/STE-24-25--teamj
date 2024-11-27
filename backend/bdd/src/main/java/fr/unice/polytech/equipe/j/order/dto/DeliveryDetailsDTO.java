package fr.unice.polytech.equipe.j.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class DeliveryDetailsDTO {
    private UUID id;
    private DeliveryLocationDTO deliveryLocation;
    private String deliveryTime;

    public DeliveryDetailsDTO() {
    }

    public DeliveryDetailsDTO(UUID id, DeliveryLocationDTO deliveryLocation, String deliveryTime) {
        this.id = id;
        this.deliveryLocation = deliveryLocation;
        this.deliveryTime = deliveryTime;
    }
}
