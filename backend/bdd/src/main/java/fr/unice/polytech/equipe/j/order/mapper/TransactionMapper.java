package fr.unice.polytech.equipe.j.order.mapper;

import fr.unice.polytech.equipe.j.order.dto.TransactionDTO;
import fr.unice.polytech.equipe.j.order.entities.TransactionEntity;
import fr.unice.polytech.equipe.j.user.mapper.CampusUserMapper;

public class TransactionMapper {

    public static TransactionDTO toDTO(TransactionEntity entity) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(entity.getId());
        transactionDTO.setAmount(entity.getAmount());
        transactionDTO.setTimestamp(entity.getTimestamp());
        transactionDTO.setUser(CampusUserMapper.toDTO(entity.getUser()));
        transactionDTO.setOrder(OrderMapper.toDTO(entity.getOrder()));
        return transactionDTO;
    }

    public TransactionEntity toEntity(TransactionDTO dto) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(dto.getId());
        transactionEntity.setAmount(dto.getAmount());
        transactionEntity.setUser(CampusUserMapper.toEntity(dto.getUser()));
        transactionEntity.setOrder(OrderMapper.toEntity(dto.getOrder()));
        transactionEntity.setTimestamp(dto.getTimestamp());
        return transactionEntity;
    }
}
