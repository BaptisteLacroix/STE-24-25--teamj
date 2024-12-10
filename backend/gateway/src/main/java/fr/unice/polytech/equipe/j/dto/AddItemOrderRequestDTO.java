package fr.unice.polytech.equipe.j.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddItemOrderRequestDTO {
    private DeliveryDetailsDTO deliveryDetails;
    private String groupOrderId;

    public AddItemOrderRequestDTO() {
    }

    public AddItemOrderRequestDTO(DeliveryDetailsDTO deliveryDetails, String groupOrderId) {
        this.deliveryDetails = deliveryDetails;
        this.groupOrderId = groupOrderId;
    }
}
