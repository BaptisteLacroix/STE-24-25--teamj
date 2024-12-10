package fr.unice.polytech.equipe.j.deliverylocation.mapper;

import fr.unice.polytech.equipe.j.deliverylocation.dto.DeliveryLocationDTO;
import fr.unice.polytech.equipe.j.deliverylocation.entities.DeliveryLocationEntity;

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