package fr.unice.polytech.equipe.j.bdd.order.dto;

import fr.unice.polytech.equipe.j.bdd.user.dto.CampusUserDTO;
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
}
