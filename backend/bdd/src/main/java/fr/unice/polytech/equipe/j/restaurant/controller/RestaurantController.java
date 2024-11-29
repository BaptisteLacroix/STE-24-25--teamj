package fr.unice.polytech.equipe.j.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.JacksonConfig;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.entities.RestaurantEntity;
import fr.unice.polytech.equipe.j.restaurant.mapper.RestaurantMapper;

import java.util.List;
import java.util.UUID;

@Controller("/api/database/restaurants")
public class RestaurantController {
    private final ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();

    @Route(value = "/all", method = HttpMethod.GET)
    public HttpResponse getAllRestaurants() {
        System.out.println("Get all restaurants");
        List<RestaurantEntity> restaurantEntities = RestaurantDAO.getAllRestaurants();
        try {
            return new HttpResponse(HttpCode.HTTP_200, objectMapper.writeValueAsString(restaurantEntities.stream().map(RestaurantMapper::toDTO).toList()));
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(HttpCode.HTTP_500, "Internal server error");
        }
    }

    @Route(value = "/{id}", method = HttpMethod.GET)
    public HttpResponse getRestaurantById(@PathParam("id") UUID id) {
        System.out.println("Get restaurant by id: " + id);
        RestaurantEntity restaurantEntity = RestaurantDAO.getRestaurantById(id);
        if (restaurantEntity != null) {
            try {
                return new HttpResponse(HttpCode.HTTP_200, objectMapper.writeValueAsString(RestaurantMapper.toDTO(restaurantEntity)));
            } catch (Exception e) {
                e.printStackTrace();
                return new HttpResponse(HttpCode.HTTP_500, "Internal server error");
            }
        } else {
            return new HttpResponse(HttpCode.HTTP_404, "Restaurant not found");
        }
    }

    @Route(value = "/create", method = HttpMethod.POST)
    public HttpResponse createRestaurant(@BeanParam RestaurantDTO restaurantDTO) {
        System.out.println("Create restaurant");
        RestaurantEntity restaurantEntity = RestaurantMapper.toEntity(restaurantDTO);
        return RestaurantDAO.save(restaurantEntity);
    }

    @Route(value = "/update", method = HttpMethod.PUT)
    public HttpResponse updateRestaurant(@BeanParam RestaurantDTO restaurantDTO) {
        System.out.println("Update restaurant");
        RestaurantEntity restaurantEntity = RestaurantMapper.toEntity(restaurantDTO);
        return RestaurantDAO.save(restaurantEntity);
    }

    @Route(value = "/{id}", method = HttpMethod.DELETE)
    public HttpResponse deleteRestaurant(@PathParam("id") UUID id) {
        System.out.println("Delete restaurant");
        return RestaurantDAO.delete(id);
    }

    // Get menuItem from restaurant
    @Route(value = "/{restaurantId}/menuItem/{menuItemId}", method = HttpMethod.GET)
    public HttpResponse getMenuItem(@PathParam("restaurantId") UUID restaurantId, @PathParam("menuItemId") UUID menuItemId) {
        System.out.println("Get menu item");
        RestaurantEntity restaurantEntity = RestaurantDAO.getRestaurantById(restaurantId);
        RestaurantDTO restaurantDTO = RestaurantMapper.toDTO(restaurantEntity);
        MenuItemDTO menuItemEntity = restaurantDTO.getMenu().getItems().stream().filter(item -> item.getId().equals(menuItemId)).findFirst().orElse(null);
        if (menuItemEntity != null) {
            try {
                return new HttpResponse(HttpCode.HTTP_200, objectMapper.writeValueAsString(menuItemEntity));
            } catch (Exception e) {
                e.printStackTrace();
                return new HttpResponse(HttpCode.HTTP_500, "Internal server error");
            }
        } else {
            return new HttpResponse(HttpCode.HTTP_404, "Menu item not found");
        }
    }
}
