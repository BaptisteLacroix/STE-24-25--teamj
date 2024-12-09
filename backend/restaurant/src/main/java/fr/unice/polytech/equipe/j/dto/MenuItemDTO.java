package fr.unice.polytech.equipe.j.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private MenuDTO menuDTO;

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
