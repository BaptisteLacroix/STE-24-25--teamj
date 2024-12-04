package fr.unice.polytech.equipe.j.order.grouporder.dto;

import fr.unice.polytech.equipe.j.order.grouporder.dto.CampusUserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GroupOrderDTO {
    private UUID groupOrderId;
    private List<OrderDTO> orders;
    private List<CampusUserDTO> users;
    private DeliveryDetailsDTO deliveryDetails;
    private String status;

    public GroupOrderDTO() {
    }

    public GroupOrderDTO(UUID groupOrderId, List<OrderDTO> orders, List<CampusUserDTO> users, DeliveryDetailsDTO deliveryDetails, String status) {
        this.groupOrderId = groupOrderId;
        this.orders = orders;
        this.users = users;
        this.deliveryDetails = deliveryDetails;
        this.status = status;
    }
}
