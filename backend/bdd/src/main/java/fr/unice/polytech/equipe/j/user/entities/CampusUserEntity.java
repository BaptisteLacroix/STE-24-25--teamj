package fr.unice.polytech.equipe.j.user.entities;

import fr.unice.polytech.equipe.j.order.dto.PaymentMethod;
import fr.unice.polytech.equipe.j.order.entities.OrderEntity;
import fr.unice.polytech.equipe.j.order.entities.TransactionEntity;
import fr.unice.polytech.equipe.j.order.mapper.PaymentMethodConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "campus_users")
@Getter
@Setter
public class CampusUserEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double balance;

    @Convert(converter = PaymentMethodConverter.class)
    @Column(name = "defaultPaymentMethod", nullable = false)
    private PaymentMethod defaultPaymentMethod = PaymentMethod.CREDIT_CARD;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderEntity> ordersHistory = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TransactionEntity> transactions = new ArrayList<>();
}
