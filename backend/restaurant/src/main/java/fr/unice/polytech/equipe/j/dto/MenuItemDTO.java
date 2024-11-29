package fr.unice.polytech.equipe.j.dto;

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

    public MenuItemDTO() {
    }

    @Override
    public String toString() {
        return "MenuItemDTO{" +
                "uuid=" + id +
                ", name='" + name + '\'' +
                ", prepTime=" + prepTime +
                ", price=" + price +
                '}';
    }
}
