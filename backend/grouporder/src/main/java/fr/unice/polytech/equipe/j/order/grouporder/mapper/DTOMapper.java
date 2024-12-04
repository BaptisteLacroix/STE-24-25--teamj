package fr.unice.polytech.equipe.j.order.grouporder.mapper;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.order.grouporder.backend.GroupOrder;
import fr.unice.polytech.equipe.j.order.grouporder.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.order.grouporder.dto.DeliveryLocationDTO;
import fr.unice.polytech.equipe.j.order.grouporder.dto.GroupOrderDTO;

public class DTOMapper {

    public static GroupOrder toGroupOrder(GroupOrderDTO groupOrderDTO) {
        DeliveryDetails deliveryDetails = toDeliveryDetails(groupOrderDTO.getDeliveryDetails());
        GroupOrder groupOrder = new GroupOrder(deliveryDetails);
        groupOrder.setGroupOrderId(groupOrderDTO.getGroupOrderId());
        return groupOrder;
    }

    private static DeliveryDetails toDeliveryDetails(DeliveryDetailsDTO deliveryDetailsDTO) {
        DeliveryLocation deliveryLocation = toDeliveryLocation(deliveryDetailsDTO.getDeliveryLocation());
        return new DeliveryDetails(
                deliveryLocation,
                deliveryDetailsDTO.getDeliveryTime()
        );

    }

    private static DeliveryLocation toDeliveryLocation(DeliveryLocationDTO deliveryLocationDTO) {
       return new DeliveryLocation(
                deliveryLocationDTO.getLocationName(),
                deliveryLocationDTO.getAddress()
        );
    }
}
