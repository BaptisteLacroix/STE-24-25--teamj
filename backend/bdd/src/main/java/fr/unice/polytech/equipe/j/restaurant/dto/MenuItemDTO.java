package fr.unice.polytech.equipe.j.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MenuItemDTO {
    private UUID uuid;
    private String name;
    private int prepTime;
    private double price;

    public MenuItemDTO() {
    }

    public MenuItemDTO(UUID uuid, String name, int prepTime, double price) {
        this.uuid = uuid;
        this.name = name;
        this.prepTime = prepTime;
        this.price = price;
    }
}
