package fr.unice.polytech.equipe.j.dto;

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

    @Override
    public String toString() {
        return "MenuItemDTO{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", prepTime=" + prepTime +
                ", price=" + price +
                '}';
    }
}
