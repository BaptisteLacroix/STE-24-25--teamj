package fr.unice.polytech.equipe.j.order.mapper;

import fr.unice.polytech.equipe.j.order.dto.DeliveryLocationDTO;
import fr.unice.polytech.equipe.j.order.entities.DeliveryLocationEntity;

public class DeliveryLocationMapper {
    public static DeliveryLocationDTO toDTO(DeliveryLocationEntity entity) {
        return new DeliveryLocationDTO(entity.getId(), entity.getLocationName(), entity.getAddress());
    }

    public static DeliveryLocationEntity toEntity(DeliveryLocationDTO dto) {
        return new DeliveryLocationEntity(dto.getId(), dto.getLocationName(), dto.getAddress());
    }
}
