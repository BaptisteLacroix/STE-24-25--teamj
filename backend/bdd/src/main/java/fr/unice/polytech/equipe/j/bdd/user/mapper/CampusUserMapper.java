package fr.unice.polytech.equipe.j.bdd.user.mapper;

import fr.unice.polytech.equipe.j.bdd.order.mapper.OrderMapper;
import fr.unice.polytech.equipe.j.bdd.order.mapper.TransactionMapper;
import fr.unice.polytech.equipe.j.bdd.user.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.bdd.user.entities.CampusUserEntity;

import java.util.stream.Collectors;

public class CampusUserMapper {
    public static CampusUserDTO toDTO(CampusUserEntity entity) {
        return new CampusUserDTO(
                entity.getId(),
                entity.getName(),
                entity.getBalance(),
                entity.getDefaultPaymentMethod(),
                entity.getOrdersHistory().stream().map(OrderMapper::toDTO).collect(Collectors.toList()),
                entity.getTransactions().stream().map(TransactionMapper::toDTO).collect(Collectors.toList())
        );
    }

    public static CampusUserEntity toEntity(CampusUserDTO dto) {
        CampusUserEntity entity = new CampusUserEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setBalance(dto.getBalance());
        entity.setDefaultPaymentMethod(dto.getDefaultPaymentMethod());
        // Assuming OrderMapper and transactions mapping logic is provided
        return entity;
    }
}
