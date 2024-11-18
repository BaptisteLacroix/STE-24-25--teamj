package fr.unice.polytech.equipe.j.bdd.restaurant.controller;

import fr.unice.polytech.equipe.j.bdd.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.bdd.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.bdd.restaurant.entities.RestaurantEntity;
import fr.unice.polytech.equipe.j.bdd.restaurant.mapper.RestaurantMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        List<RestaurantEntity> restaurantEntities = RestaurantDAO.getAllRestaurants();
        return restaurantEntities.stream().map(RestaurantMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public RestaurantDTO getRestaurantById(@PathVariable UUID id) {
        RestaurantEntity restaurantEntity = RestaurantDAO.getRestaurantById(id);
        return RestaurantMapper.toDTO(restaurantEntity);
    }

    @PostMapping
    public ResponseEntity<String> addRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        RestaurantEntity restaurantEntity = RestaurantMapper.toEntity(restaurantDTO);
        RestaurantDAO.saveRestaurant(restaurantEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body("Restaurant added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRestaurant(@PathVariable UUID id, @RequestBody RestaurantDTO restaurantDTO) {
        RestaurantEntity existing = RestaurantDAO.getRestaurantById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant not found");
        }
        RestaurantEntity updated = RestaurantMapper.toEntity(restaurantDTO);
        updated.setId(id);
        RestaurantDAO.saveRestaurant(updated);
        return ResponseEntity.ok("Restaurant updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable UUID id) {
        RestaurantDAO.deleteRestaurant(id);
        return ResponseEntity.ok("Restaurant deleted successfully");
    }
}
