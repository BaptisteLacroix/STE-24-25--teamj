package fr.unice.polytech.equipe.j.order.mapper;

import fr.unice.polytech.equipe.j.order.dto.DeliveryLocationDTO;
import fr.unice.polytech.equipe.j.order.entities.DeliveryLocationEntity;

public class DeliveryLocationMapper {
    public static DeliveryLocationDTO toDTO(DeliveryLocationEntity entity) {
        DeliveryLocationDTO deliveryLocationDTO = new DeliveryLocationDTO();
        deliveryLocationDTO.setId(entity.getId());
        deliveryLocationDTO.setLocationName(entity.getLocationName());
        deliveryLocationDTO.setAddress(entity.getAddress());
        return deliveryLocationDTO;
    }

    public static DeliveryLocationEntity toEntity(DeliveryLocationDTO dto) {
        DeliveryLocationEntity deliveryLocationEntity = new DeliveryLocationEntity();
        deliveryLocationEntity.setId(dto.getId());
        deliveryLocationEntity.setLocationName(dto.getLocationName());
        deliveryLocationEntity.setAddress(dto.getAddress());
        return deliveryLocationEntity;
    }
}