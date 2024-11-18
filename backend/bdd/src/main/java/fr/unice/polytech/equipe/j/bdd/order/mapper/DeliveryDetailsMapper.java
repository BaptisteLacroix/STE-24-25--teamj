package fr.unice.polytech.equipe.j.bdd.order.mapper;

import fr.unice.polytech.equipe.j.bdd.order.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.bdd.order.entities.DeliveryDetailsEntity;

public class DeliveryDetailsMapper {

    public static DeliveryDetailsDTO toDTO(DeliveryDetailsEntity entity) {
        DeliveryDetailsDTO deliveryDetailsDTO = new DeliveryDetailsDTO();
        deliveryDetailsDTO.setId(entity.getId());
        deliveryDetailsDTO.setDeliveryLocation(DeliveryLocationMapper.toDTO(entity.getDeliveryLocation()));
        deliveryDetailsDTO.setDeliveryTime(entity.getDeliveryTime());
        return deliveryDetailsDTO;
    }

    public static DeliveryDetailsEntity toEntity(DeliveryDetailsDTO dto) {
        DeliveryDetailsEntity deliveryDetails = new DeliveryDetailsEntity();
        deliveryDetails.setId(dto.getId());
        deliveryDetails.setDeliveryLocation(DeliveryLocationMapper.toEntity(dto.getDeliveryLocation()));
        deliveryDetails.setDeliveryTime(dto.getDeliveryTime());
        return deliveryDetails;
    }
}
