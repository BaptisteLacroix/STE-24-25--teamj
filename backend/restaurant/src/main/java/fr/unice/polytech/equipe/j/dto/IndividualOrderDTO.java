package fr.unice.polytech.equipe.j.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndividualOrderDTO extends OrderDTO {
    private DeliveryDetailsDTO deliveryDetails;

    public IndividualOrderDTO() {
    }

    public IndividualOrderDTO(OrderDTO orderDTO, DeliveryDetailsDTO deliveryDetails) {
        super(orderDTO.getId(), orderDTO.getRestaurantId(), orderDTO.getUserId(), orderDTO.getItems(), orderDTO.getStatus());
        this.deliveryDetails = deliveryDetails;
    }

    @Override
    public String toString() {
        return "IndividualOrderDTO{" +
                super.toString() +
                "deliveryDetails=" + deliveryDetails +
                '}';
    }

}
