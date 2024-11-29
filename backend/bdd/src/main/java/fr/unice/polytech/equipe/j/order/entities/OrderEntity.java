package fr.unice.polytech.equipe.j.order.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.unice.polytech.equipe.j.order.dto.OrderStatus;
import fr.unice.polytech.equipe.j.order.mapper.OrderStatusConverter;
import fr.unice.polytech.equipe.j.restaurant.entities.MenuItemEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MenuItemEntity> items = new ArrayList<>();

    @Convert(converter = OrderStatusConverter.class)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    public OrderEntity() {
        this.status = OrderStatus.PENDING;
    }

    @Override
    @JsonIgnore
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", userId=" + userId +
                ", items=" + items +
                ", status=" + status +
                '}';
    }
}
