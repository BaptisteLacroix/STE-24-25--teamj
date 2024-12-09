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
        dto.setDeliveryDetails(entity.getDeliveryDetails() != null
                ? DeliveryDetailsMapper.toDTO(entity.getDeliveryDetails())
                : null);
        dto.setStatus(entity.getStatus().name());
        return dto;
    }

    public static GroupOrderEntity toEntity(GroupOrderDTO dto) {
        GroupOrderEntity entity = new GroupOrderEntity();
        entity.setId(dto.getGroupOrderId());
        if (dto.getDeliveryDetails() != null) {
            entity.setDeliveryDetails(DeliveryDetailsMapper.toEntity(dto.getDeliveryDetails()));
        } else {
            entity.setDeliveryDetails(null);
        }
        return entity;
    }
}
