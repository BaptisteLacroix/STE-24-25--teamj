package fr.unice.polytech.equipe.j.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndividualOrderDTO extends OrderDTO {
    private DeliveryDetailsDTO deliveryDetails;
}
