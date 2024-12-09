package fr.unice.polytech.equipe.j.user.dto;

import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RestaurantManagerDTO {
    private UUID id;
    private String email;
    private String name;
    private RestaurantDTO restaurant;

    // Constructors
    public RestaurantManagerDTO() {
    }

    public RestaurantManagerDTO(UUID id, String email, String name, RestaurantDTO restaurant) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.restaurant = restaurant;
    }
}
