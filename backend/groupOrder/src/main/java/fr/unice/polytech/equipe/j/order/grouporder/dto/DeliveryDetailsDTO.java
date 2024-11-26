package java.fr.unice.polytech.equipe.j.order.grouporder.dto;

import lombok.Getter;
import lombok.Setter;

import java.fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class DeliveryDetailsDTO {
    private UUID id;
    private DeliveryLocation deliveryLocation;
    private LocalDateTime deliveryTime;

    public DeliveryDetailsDTO() {
    }

    public DeliveryDetailsDTO(UUID id, DeliveryLocation deliveryLocation, LocalDateTime deliveryTime) {
        this.id = id;
        this.deliveryLocation = deliveryLocation;
        this.deliveryTime = deliveryTime;
    }
}
