package fr.unice.polytech.equipe.j.bdd.order.mapper;

import fr.unice.polytech.equipe.j.bdd.order.dto.DeliveryLocationDTO;
import fr.unice.polytech.equipe.j.bdd.order.entities.DeliveryLocationEntity;

public class DeliveryLocationMapper {
    public static DeliveryLocationDTO toDTO(DeliveryLocationEntity entity) {
        return new DeliveryLocationDTO(entity.getId(), entity.getLocationName(), entity.getAddress());
    }

    public static DeliveryLocationEntity toEntity(DeliveryLocationDTO dto) {
        return new DeliveryLocationEntity(dto.getId(), dto.getLocationName(), dto.getAddress());
    }
}
