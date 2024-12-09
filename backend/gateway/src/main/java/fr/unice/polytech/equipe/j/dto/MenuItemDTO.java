package fr.unice.polytech.equipe.j.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MenuItemDTO {
    @NonNull
    private UUID id;
    @NonNull
    private String name;
    @NonNull
    private int prepTime;
    @NonNull
    private double price;

    public MenuItemDTO() {
    }

    public MenuItemDTO(UUID id, String name, int prepTime, double price) {
        this.id = id;
        this.name = name;
        this.prepTime = prepTime;
        this.price = price;
    }
}
