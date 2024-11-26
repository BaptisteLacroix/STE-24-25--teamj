package fr.unice.polytech.equipe.j.order.mapper;

import fr.unice.polytech.equipe.j.order.dto.IndividualOrderDTO;
import fr.unice.polytech.equipe.j.order.entities.IndividualOrderEntity;

public class IndividualOrderMapper {
    public static IndividualOrderDTO toDTO(IndividualOrderEntity entity) {
        IndividualOrderDTO dto = (IndividualOrderDTO) OrderMapper.toDTO(entity);
        dto.setDeliveryDetails(DeliveryDetailsMapper.toDTO(entity.getDeliveryDetails()));
        return dto;
    }

    public static IndividualOrderEntity toEntity(IndividualOrderDTO dto) {
        IndividualOrderEntity entity = (IndividualOrderEntity) OrderMapper.toEntity(dto);
        entity.setDeliveryDetails(DeliveryDetailsMapper.toEntity(dto.getDeliveryDetails()));
        return entity;
    }
}
