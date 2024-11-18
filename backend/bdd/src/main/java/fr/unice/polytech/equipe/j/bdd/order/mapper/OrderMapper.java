package fr.unice.polytech.equipe.j.bdd.order.mapper;

import fr.unice.polytech.equipe.j.bdd.order.dto.OrderDTO;
import fr.unice.polytech.equipe.j.bdd.order.dto.OrderStatus;
import fr.unice.polytech.equipe.j.bdd.order.entities.OrderEntity;
import fr.unice.polytech.equipe.j.bdd.restaurant.mapper.RestaurantMapper;
import fr.unice.polytech.equipe.j.bdd.user.mapper.CampusUserMapper;

public class OrderMapper {
    public static OrderDTO toDTO(OrderEntity entity) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderUUID(entity.getId());
        dto.setRestaurant(RestaurantMapper.toDTO(entity.getRestaurant()));
        dto.setUser(CampusUserMapper.toDTO(entity.getUser()));
        dto.setItems(entity.getItems().stream().map(MenuItemMapper::toDTO).toList());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }

    public static OrderEntity toEntity(OrderDTO dto) {
        OrderEntity entity = new OrderEntity();
        entity.setId(dto.getOrderUUID());
        entity.setRestaurant(RestaurantMapper.toEntity(dto.getRestaurant()));
        entity.setUser(CampusUserMapper.toEntity(dto.getUser()));
        entity.setStatus(OrderStatus.valueOf(dto.getStatus()));
        return entity;
    }
}
