package fr.unice.polytech.equipe.j.order.mapper;

import fr.unice.polytech.equipe.j.order.dto.GroupOrderDTO;
import fr.unice.polytech.equipe.j.order.entities.GroupOrderEntity;
import fr.unice.polytech.equipe.j.user.mapper.CampusUserMapper;

public class GroupOrderMapper {
    public static GroupOrderDTO toDTO(GroupOrderEntity entity) {
        GroupOrderDTO dto = new GroupOrderDTO();
        dto.setGroupOrderId(entity.getId());
        dto.setOrders(entity.getOrders().stream().map(OrderMapper::toDTO).toList());
        dto.setUsers(entity.getUsers().stream().map(CampusUserMapper::toDTO).toList());
        dto.setDeliveryDetails(DeliveryDetailsMapper.toDTO(entity.getDeliveryDetails()));
        dto.setStatus(entity.getStatus().name());
        return dto;
    }

    public static GroupOrderEntity toEntity(GroupOrderDTO dto) {
        GroupOrderEntity entity = new GroupOrderEntity();
        entity.setId(dto.getGroupOrderId());
        entity.setDeliveryDetails(DeliveryDetailsMapper.toEntity(dto.getDeliveryDetails()));
        return entity;
    }
}
