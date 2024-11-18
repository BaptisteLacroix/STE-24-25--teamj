package fr.unice.polytech.equipe.j.order.dto;

import fr.unice.polytech.equipe.j.user.dto.CampusUserDTO;
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
}
