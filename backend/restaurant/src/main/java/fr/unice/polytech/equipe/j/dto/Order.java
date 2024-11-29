package fr.unice.polytech.equipe.j.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Order {
    @NonNull
    private UUID id;
    @NonNull
    private UUID restaurantId;
    @NonNull
    private UUID userId;
    @NonNull
    private List<MenuItemDTO> items;
    @NonNull
    private OrderStatus status;

    public Order() {
    }
}
