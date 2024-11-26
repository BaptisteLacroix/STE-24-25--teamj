package fr.unice.polytech.equipe.j.order.dto;

import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {
    private UUID id;
    private UUID restaurantId;
    private UUID userId;
    private List<MenuItemDTO> items;
    private String status;

    public OrderDTO() {
    }

    public OrderDTO(UUID id, UUID restaurantId, UUID userId, List<MenuItemDTO> items, String status) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.items = items;
        this.status = status;
    }
}
