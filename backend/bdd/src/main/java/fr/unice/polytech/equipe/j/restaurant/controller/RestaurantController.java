package fr.unice.polytech.equipe.j.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.entities.RestaurantEntity;
import fr.unice.polytech.equipe.j.restaurant.mapper.RestaurantMapper;

import java.util.List;
import java.util.UUID;

@Controller("/api/database/restaurants")
public class RestaurantController {

    @Route(value = "/all", method = HttpMethod.GET)
    public HttpResponse getAllRestaurants() {
        List<RestaurantEntity> restaurantEntities = RestaurantDAO.getAllRestaurants();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(restaurantEntities.stream().map(RestaurantMapper::toDTO).toList());
            return new HttpResponse(HttpCode.HTTP_200, json);
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(HttpCode.HTTP_500, "Internal server error");
        }
    }

    @Route(value = "/{id}", method = HttpMethod.GET)
    public HttpResponse getRestaurantById(@PathParam("id") UUID id) {
        RestaurantEntity restaurantEntity = RestaurantDAO.getRestaurantById(id);
        if (restaurantEntity != null) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                String json = ow.writeValueAsString(RestaurantMapper.toDTO(restaurantEntity));
                return new HttpResponse(HttpCode.HTTP_200, json);
            } catch (Exception e) {
                e.printStackTrace();
                return new HttpResponse(HttpCode.HTTP_500, "Internal server error");
            }
        } else {
            return new HttpResponse(HttpCode.HTTP_404, "Restaurant not found");
        }
    }


//    @Route(value = "/search", method = HttpMethod.GET)
//    public HttpResponse searchRestaurants(@QueryParam("name") String name, @QueryParam("location") String location) {
//        List<RestaurantEntity> restaurants = RestaurantDAO.searchByNameAndLocation(name, location);
//        return new HttpResponse(HttpCode.HTTP_200, restaurants.stream().map(RestaurantMapper::toDTO).toList());
//    }

    @Route(value = "/create", method = HttpMethod.POST)
    public HttpResponse createRestaurant(@BeanParam RestaurantDTO restaurantDTO) {
        System.out.println("Creating restaurant: " + restaurantDTO.getRestaurantName());
        RestaurantEntity restaurantEntity = RestaurantMapper.toEntity(restaurantDTO);
        RestaurantDAO.save(restaurantEntity);
        return new HttpResponse(HttpCode.HTTP_201, "Restaurant created successfully");
    }

    @Route(value = "/{id}", method = HttpMethod.PUT)
    public HttpResponse updateRestaurant(UUID id, RestaurantDTO restaurantDTO) {
        RestaurantEntity existing = RestaurantDAO.getRestaurantById(id);
        if (existing == null) {
            return new HttpResponse(HttpCode.HTTP_404, "Restaurant not found");
        }
        RestaurantEntity updated = RestaurantMapper.toEntity(restaurantDTO);
        updated.setId(id);
        RestaurantDAO.save(updated);
        return new HttpResponse(HttpCode.HTTP_200, "Restaurant updated successfully");
    }

    @Route(value = "/{id}", method = HttpMethod.DELETE)
    public HttpResponse deleteRestaurant(UUID id) {
        RestaurantDAO.delete(id);
        return new HttpResponse(HttpCode.HTTP_200, "Restaurant deleted successfully");
    }
}
