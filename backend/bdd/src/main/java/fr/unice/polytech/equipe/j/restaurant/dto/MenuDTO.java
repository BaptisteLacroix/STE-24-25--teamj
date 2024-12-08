package fr.unice.polytech.equipe.j.restaurant.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MenuDTO {
    @NonNull
    private UUID uuid;
    private List<MenuItemDTO> items;

    public MenuDTO() {
    }

    public MenuDTO(UUID uuid, List<MenuItemDTO> items) {
        this.uuid = uuid;
        this.items = items;
    }
}
