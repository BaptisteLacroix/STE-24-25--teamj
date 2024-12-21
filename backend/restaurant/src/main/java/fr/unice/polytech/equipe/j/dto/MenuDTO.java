package fr.unice.polytech.equipe.j.dto;

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

    public MenuDTO(List<MenuItemDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "MenuDTO{" +
                "uuid=" + uuid +
                ", items=" + items +
                '}';
    }
}
