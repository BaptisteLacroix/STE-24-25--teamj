package fr.unice.polytech.equipe.j.dto;

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
    private SlotDTO slot;

    public OrderDTO() {
    }

    public OrderDTO(UUID id, UUID restaurantId, UUID userId, List<MenuItemDTO> items, String status, SlotDTO slot) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.items = items;
        this.status = status;
        this.slot = slot;
    }
}
