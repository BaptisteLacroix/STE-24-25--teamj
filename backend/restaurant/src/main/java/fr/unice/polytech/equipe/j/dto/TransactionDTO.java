package fr.unice.polytech.equipe.j.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TransactionDTO {
    private UUID id;
    private CampusUserDTO user;
    private OrderDTO order;
    private double amount;
    private LocalDateTime timestamp;

    public TransactionDTO() {
    }

    public TransactionDTO(UUID id, CampusUserDTO user, OrderDTO order, double amount, LocalDateTime timestamp) {
        this.id = id;
        this.user = user;
        this.order = order;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}
