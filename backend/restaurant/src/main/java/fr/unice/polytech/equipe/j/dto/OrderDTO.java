package fr.unice.polytech.equipe.j.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {
    @NonNull
    private UUID id;
    @NonNull
    private UUID restaurantId;
    @NonNull
    private UUID userId;
    @NonNull
    private List<MenuItemDTO> items;
    @NonNull
    private OrderStatus status;
    private SlotDTO slot;

    public OrderDTO() {
    }

    public OrderDTO(UUID id, UUID restaurantId, UUID userId, List<MenuItemDTO> items, OrderStatus status) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.items = items;
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", userId=" + userId +
                ", items=" + items +
                ", status=" + status +
                '}';
    }
}
