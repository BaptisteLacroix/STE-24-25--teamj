package fr.unice.polytech.equipe.j.user.dto;

import fr.unice.polytech.equipe.j.order.dto.OrderDTO;
import fr.unice.polytech.equipe.j.order.dto.PaymentMethod;
import fr.unice.polytech.equipe.j.order.dto.TransactionDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CampusUserDTO {
    private UUID id;
    private String name;
    private double balance;
    private PaymentMethod defaultPaymentMethod;
    private List<OrderDTO> ordersHistory;
    private List<TransactionDTO> transactions;

    // Constructors
    public CampusUserDTO() {
    }

    public CampusUserDTO(UUID id, String name, double balance, PaymentMethod defaultPaymentMethod, List<OrderDTO> ordersHistory, List<TransactionDTO> transactions) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.defaultPaymentMethod = defaultPaymentMethod;
        this.ordersHistory = ordersHistory;
        this.transactions = transactions;
    }
}
