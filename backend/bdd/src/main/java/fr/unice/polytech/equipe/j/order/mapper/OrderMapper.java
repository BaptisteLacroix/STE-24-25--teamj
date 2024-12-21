package fr.unice.polytech.equipe.j.order.mapper;

import fr.unice.polytech.equipe.j.order.dto.OrderDTO;
import fr.unice.polytech.equipe.j.order.dto.OrderStatus;
import fr.unice.polytech.equipe.j.order.entities.OrderEntity;
import fr.unice.polytech.equipe.j.restaurant.mapper.MenuItemMapper;
import fr.unice.polytech.equipe.j.restaurant.mapper.RestaurantMapper;

public class OrderMapper {
    public static OrderDTO toDTO(OrderEntity entity) {
        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setRestaurantId(entity.getRestaurantId());
        dto.setUserId(entity.getUserId());
        dto.setItems(entity.getItems().stream().map(MenuItemMapper::toDTO).toList());
        dto.setStatus(entity.getStatus().name());
        dto.setSlot(RestaurantMapper.toDTO(entity.getSlotEntity()));
        return dto;
    }

    public static OrderEntity toEntity(OrderDTO dto) {
        OrderEntity entity = new OrderEntity();
        entity.setId(dto.getId());
        entity.setRestaurantId(dto.getRestaurantId());
        entity.setUserId(dto.getUserId());
        entity.setStatus(OrderStatus.valueOf(dto.getStatus()));
        entity.setItems(dto.getItems().stream().map(MenuItemMapper::toEntity).toList());
        entity.setSlotEntity(RestaurantMapper.toEntity(dto.getSlot()));
        return entity;
    }
}
