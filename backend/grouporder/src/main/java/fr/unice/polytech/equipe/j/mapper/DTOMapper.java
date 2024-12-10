package fr.unice.polytech.equipe.j.mapper;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.backend.GroupOrder;
import fr.unice.polytech.equipe.j.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.dto.DeliveryLocationDTO;
import fr.unice.polytech.equipe.j.dto.GroupOrderDTO;
import fr.unice.polytech.equipe.j.dto.OrderStatus;

public class DTOMapper {

    public static GroupOrder toGroupOrder(GroupOrderDTO groupOrderDTO) {
        GroupOrder groupOrder = new GroupOrder(groupOrderDTO.getDeliveryDetails());
        groupOrder.setGroupOrderId(groupOrderDTO.getGroupOrderId());

        // Ajout des utilisateurs
        if (groupOrderDTO.getUsers() != null) {
            groupOrderDTO.getUsers().forEach(userDTO -> groupOrder.getUsers().add(userDTO));
        }

        // Ajout des commandes
        if (groupOrderDTO.getOrders() != null) {
            groupOrderDTO.getOrders().forEach(orderDTO -> groupOrder.getOrders().add(orderDTO));
        }

        groupOrder.setStatus(OrderStatus.valueOf(groupOrderDTO.getStatus()));
        return groupOrder;
    }

    public static GroupOrderDTO toGroupOrderDTO(GroupOrder groupOrder) {
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
