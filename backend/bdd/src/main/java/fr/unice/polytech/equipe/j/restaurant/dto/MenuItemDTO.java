package fr.unice.polytech.equipe.j.restaurant.dto;

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
    @NonNull
    private String type;

    public MenuItemDTO() {
    }

    public MenuItemDTO(UUID id, String name, int prepTime, double price, String type) {
        this.id = id;
        this.name = name;
        this.prepTime = prepTime;
        this.price = price;
        this.type = type;
    }

    @Override
    public String toString() {
        return "MenuItemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prepTime=" + prepTime +
                ", price=" + price +
                ", type='" + type + '\'' +
                '}';
    }
}
