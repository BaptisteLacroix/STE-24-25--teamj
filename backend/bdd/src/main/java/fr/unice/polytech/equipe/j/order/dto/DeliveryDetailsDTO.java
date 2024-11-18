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
    private LocalDateTime deliveryTime;
}
