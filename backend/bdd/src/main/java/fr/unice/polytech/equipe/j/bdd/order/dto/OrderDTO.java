package fr.unice.polytech.equipe.j.bdd.order.dto;

import fr.unice.polytech.equipe.j.bdd.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.bdd.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.bdd.user.dto.CampusUserDTO;
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
}
