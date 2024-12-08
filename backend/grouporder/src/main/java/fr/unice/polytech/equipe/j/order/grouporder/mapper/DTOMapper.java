package fr.unice.polytech.equipe.j.order.grouporder.mapper;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.order.grouporder.backend.GroupOrder;
import fr.unice.polytech.equipe.j.order.grouporder.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.order.grouporder.dto.DeliveryLocationDTO;
import fr.unice.polytech.equipe.j.order.grouporder.dto.GroupOrderDTO;

public class DTOMapper {

    public static GroupOrder toGroupOrder(GroupOrderDTO groupOrderDTO) {
        GroupOrder groupOrder = new GroupOrder(groupOrderDTO.getDeliveryDetails());
        groupOrder.setGroupOrderId(groupOrderDTO.getGroupOrderId());
        return groupOrder;
    }

    public static GroupOrderDTO toGroupOrderDTO(GroupOrder groupOrder){
        GroupOrderDTO groupOrderDTO = new GroupOrderDTO();
        groupOrderDTO.setGroupOrderId(groupOrder.getGroupOrderId());
        groupOrderDTO.setDeliveryDetails(groupOrder.getDeliveryDetails());
        groupOrderDTO.setOrders(groupOrder.getOrders());
        groupOrderDTO.setUsers(groupOrder.getUsers());
        groupOrderDTO.setStatus(groupOrder.getStatus().name());
        return groupOrderDTO;
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
