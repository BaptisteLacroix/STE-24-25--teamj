package fr.unice.polytech.equipe.j.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeliveryLocationDTO {
    private UUID id;
    private String locationName;
    private String address;
}
