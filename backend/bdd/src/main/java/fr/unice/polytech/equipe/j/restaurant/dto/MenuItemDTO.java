package fr.unice.polytech.equipe.j.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MenuItemDTO {
    private UUID id;
    private String name;
    private int prepTime;
    private double price;

    // Constructors, Getters, and Setters
    public MenuItemDTO() {
    }

    public MenuItemDTO(UUID id, String name, int prepTime, double price) {
        this.id = id;
        this.name = name;
        this.prepTime = prepTime;
        this.price = price;
    }
}
