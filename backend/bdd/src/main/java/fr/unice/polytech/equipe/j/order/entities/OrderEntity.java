package fr.unice.polytech.equipe.j.order.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.unice.polytech.equipe.j.order.dto.OrderStatus;
import fr.unice.polytech.equipe.j.order.mapper.OrderStatusConverter;
import fr.unice.polytech.equipe.j.restaurant.entities.MenuItemEntity;
import fr.unice.polytech.equipe.j.restaurant.entities.SlotEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<MenuItemEntity> items = new ArrayList<>();

    @Convert(converter = OrderStatusConverter.class)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "slot_entity_id")
    private SlotEntity slotEntity;

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
