package fr.unice.polytech.equipe.j.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Order {
    private UUID uuid;
    private List<MenuItemDTO> items;
    private OrderStatus status;
    private double totalPrice;
    private UUID userId;

    public Order() {
    }
}
