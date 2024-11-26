package java.fr.unice.polytech.equipe.j.order.grouporder.mapper;

import java.fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import java.fr.unice.polytech.equipe.j.order.grouporder.backend.GroupOrder;
import java.fr.unice.polytech.equipe.j.order.grouporder.dto.DeliveryDetailsDTO;
import java.fr.unice.polytech.equipe.j.order.grouporder.dto.GroupOrderDTO;

public class DTOMapper {

    // Méthode pour mapper un GroupOrderDTO en un GroupOrder
    public static GroupOrder toGroupOrder(GroupOrderDTO groupOrderDTO) {
        // Mapper les détails de livraison (DeliveryDetailsDTO -> DeliveryDetails)
        DeliveryDetails deliveryDetails = toDeliveryDetails(groupOrderDTO.getDeliveryDetails());

        // Créer le GroupOrder avec les détails de livraison
        GroupOrder groupOrder = new GroupOrder(deliveryDetails);

        // Assigner l'ID du GroupOrder (ID venant du DTO)
        groupOrder.setGroupOrderId(groupOrderDTO.getGroupOrderId());

        return groupOrder;
    }

    // Méthode pour convertir DeliveryDetailsDTO en DeliveryDetails
    private static DeliveryDetails toDeliveryDetails(DeliveryDetailsDTO deliveryDetailsDTO) {
        return new DeliveryDetails(
                deliveryDetailsDTO.getDeliveryLocation(),
                deliveryDetailsDTO.getDeliveryTime()
        );
    }
}