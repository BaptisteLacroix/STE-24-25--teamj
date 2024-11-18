package fr.unice.polytech.equipe.j.bdd.order.entities;

import fr.unice.polytech.equipe.j.bdd.order.dto.OrderStatus;
import fr.unice.polytech.equipe.j.bdd.restaurant.entities.MenuItemEntity;
import fr.unice.polytech.equipe.j.bdd.restaurant.entities.RestaurantEntity;
import fr.unice.polytech.equipe.j.bdd.user.entities.CampusUserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class OrderEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private CampusUserEntity user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MenuItemEntity> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public OrderEntity() {
        this.status = OrderStatus.PENDING;
    }
}
