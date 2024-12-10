package fr.unice.polytech.equipe.j.order.mapper;

import fr.unice.polytech.equipe.j.order.dto.GroupOrderDTO;
import fr.unice.polytech.equipe.j.order.dto.OrderStatus;
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

        // Map des DeliveryDetails si disponibles
        if (dto.getDeliveryDetails() != null) {
            entity.setDeliveryDetails(DeliveryDetailsMapper.toEntity(dto.getDeliveryDetails()));
        } else {
            entity.setDeliveryDetails(null);
        }

        // Map des utilisateurs si disponibles
        if (dto.getUsers() != null) {
            entity.setUsers(dto.getUsers().stream()
                    .map(CampusUserMapper::toEntity) // Conversion DTO -> Entité
                    .toList());
        } else {
            entity.setUsers(null);
        }

        // Map des commandes si disponibles
        if (dto.getOrders() != null) {
            entity.setOrders(dto.getOrders().stream()
                    .map(OrderMapper::toEntity) // Conversion DTO -> Entité
                    .toList());
        } else {
            entity.setOrders(null);
        }

        // Status
        if (dto.getStatus() != null) {
            try {
                entity.setStatus(OrderStatus.valueOf(dto.getStatus().toUpperCase())); // Convertit en majuscules avant la conversion
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + dto.getStatus(), e);
            }
        }


        return entity;
    }

}
