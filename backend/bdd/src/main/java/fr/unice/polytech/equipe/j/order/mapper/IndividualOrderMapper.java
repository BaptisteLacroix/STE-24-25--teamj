package fr.unice.polytech.equipe.j.order.mapper;

import fr.unice.polytech.equipe.j.order.dto.IndividualOrderDTO;
import fr.unice.polytech.equipe.j.order.dto.OrderStatus;
import fr.unice.polytech.equipe.j.order.entities.IndividualOrderEntity;
import fr.unice.polytech.equipe.j.restaurant.mapper.MenuItemMapper;

public class IndividualOrderMapper {
    public static IndividualOrderDTO toDTO(IndividualOrderEntity entity) {
        System.out.println("IndividualOrderMapper.toDTO: " + entity);
        IndividualOrderDTO dto = new IndividualOrderDTO();
        dto.setId(entity.getId());
        dto.setRestaurantId(entity.getRestaurantId());
        dto.setUserId(entity.getUserId());
        dto.setItems(entity.getItems().stream().map(MenuItemMapper::toDTO).toList());
        dto.setStatus(entity.getStatus().name());
        dto.setDeliveryDetails(DeliveryDetailsMapper.toDTO(entity.getDeliveryDetails()));
        System.out.println("IndividualOrderMapper.toDTO: " + dto);
        return dto;
    }

    public static IndividualOrderEntity toEntity(IndividualOrderDTO dto) {
        IndividualOrderEntity entity = new IndividualOrderEntity();
        entity.setId(dto.getId());
        entity.setRestaurantId(dto.getRestaurantId());
        entity.setUserId(dto.getUserId());
        System.out.println("IndividualOrderMapper.toEntity: " + dto);
        entity.setItems(dto.getItems().stream().map(MenuItemMapper::toEntity).toList());
        entity.setStatus(OrderStatus.valueOf(dto.getStatus()));
        entity.setDeliveryDetails(DeliveryDetailsMapper.toEntity(dto.getDeliveryDetails()));
        return entity;
    }
}
