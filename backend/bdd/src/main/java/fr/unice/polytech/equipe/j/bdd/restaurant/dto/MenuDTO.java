package fr.unice.polytech.equipe.j.bdd.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MenuDTO {
    private UUID id;
    private List<MenuItemDTO> items;

    // Constructors, Getters, and Setters
    public MenuDTO() {
    }

    public MenuDTO(UUID id, List<MenuItemDTO> items) {
        this.id = id;
        this.items = items;
    }
}
