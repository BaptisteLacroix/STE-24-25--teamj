package fr.unice.polytech.equipe.j.order.grouporder.dto;

import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.user.dto.CampusUserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {
    private UUID orderUUID;
    private RestaurantDTO restaurant;
    private CampusUserDTO user;
    private List<MenuItemDTO> items;
    private String status;

    public OrderDTO() {
    }

    public OrderDTO(UUID orderUUID, RestaurantDTO restaurant, CampusUserDTO user, List<MenuItemDTO> items, String status) {
        this.orderUUID = orderUUID;
        this.restaurant = restaurant;
        this.user = user;
        this.items = items;
        this.status = status;
    }
}
