package fr.unice.polytech.equipe.j.bdd.user.mapper;

import fr.unice.polytech.equipe.j.bdd.restaurant.mapper.RestaurantMapper;
import fr.unice.polytech.equipe.j.bdd.user.dto.RestaurantManagerDTO;
import fr.unice.polytech.equipe.j.bdd.user.entities.RestaurantManagerEntity;

public class RestaurantManagerMapper {
    public static RestaurantManagerDTO toDTO(RestaurantManagerEntity entity) {
        return new RestaurantManagerDTO(
                entity.getId(),
                entity.getEmail(),
                entity.getName(),
                RestaurantMapper.toDTO(entity.getRestaurant())
        );
    }

    public static RestaurantManagerEntity toEntity(RestaurantManagerDTO dto) {
        RestaurantManagerEntity entity = new RestaurantManagerEntity();
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setName(dto.getName());
        entity.setRestaurant(RestaurantMapper.toEntity(dto.getRestaurant()));
        return entity;
    }
}
