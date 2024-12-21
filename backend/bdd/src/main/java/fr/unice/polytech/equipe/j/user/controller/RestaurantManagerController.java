package fr.unice.polytech.equipe.j.user.controller;

import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.user.dao.RestaurantManagerDAO;
import fr.unice.polytech.equipe.j.user.dto.RestaurantManagerDTO;
import fr.unice.polytech.equipe.j.user.entities.RestaurantManagerEntity;
import fr.unice.polytech.equipe.j.user.mapper.RestaurantManagerMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller class for managing restaurant manager-related HTTP requests.
 * It provides endpoints to create, retrieve, update, and delete restaurant managers.
 */
@Controller("/api/database/managers")
public class RestaurantManagerController {

    /**
     * Creates a new restaurant manager based on the provided RestaurantManagerDTO.
     *
     * @param dto the RestaurantManagerDTO containing the details of the manager to be created.
     * @return an HttpResponse with the status of the operation and the ID of the created manager.
     */
    @Route(value = "/create", method = HttpMethod.POST)
    public HttpResponse createManager(@BeanParam RestaurantManagerDTO dto) {
        return RestaurantManagerDAO.save(RestaurantManagerMapper.toEntity(dto));
    }

    /**
     * Retrieves all restaurant managers from the database.
     *
     * @return an HttpResponse containing a list of all restaurant managers as RestaurantManagerDTO objects.
     */
    @Route(value = "/all", method = HttpMethod.GET)
    public HttpResponse getAllManagers() {
        System.out.println("Get all managers");
        List<RestaurantManagerDTO> managers = RestaurantManagerDAO.getAll().stream()
                .map(RestaurantManagerMapper::toDTO)
                .collect(Collectors.toList());
        return new HttpResponse(HttpCode.HTTP_200, managers);
    }

    /**
     * Retrieves a restaurant manager by their unique ID.
     *
     * @param id the UUID of the restaurant manager to retrieve.
     * @return an HttpResponse containing the RestaurantManagerDTO of the requested manager, or an error message if not found.
     */
    @Route(value = "/{id}", method = HttpMethod.GET)
    public HttpResponse getManagerById(@PathParam("id") UUID id) {
        System.out.println("Get manager by id: " + id);
        RestaurantManagerEntity restaurantManagerEntity = RestaurantManagerDAO.getManagerById(id);
        if (restaurantManagerEntity != null) {
            return new HttpResponse(HttpCode.HTTP_200, RestaurantManagerMapper.toDTO(restaurantManagerEntity));
        } else {
            return new HttpResponse(HttpCode.HTTP_404, "Manager not found");
        }
    }

    /**
     * Deletes a restaurant manager by their unique ID.
     *
     * @param id the UUID of the manager to delete.
     * @return an HttpResponse with the status of the deletion operation.
     */
    @Route(value = "/{id}", method = HttpMethod.DELETE)
    public HttpResponse deleteManagerById(@PathParam("id") UUID id) {
        System.out.println("Delete manager by id: " + id);
        return RestaurantManagerDAO.delete(id);
    }

    /**
     * Retrieves the restaurant associated with a specific restaurant manager.
     *
     * @param id the UUID of the restaurant manager.
     * @param restaurantId the UUID of the restaurant to check.
     * @return an HttpResponse with the restaurant details if the manager is associated with the restaurant,
     *         or an error message if not.
     */
    @Route(value = "/{id}/restaurant/{restaurantId}", method = HttpMethod.GET)
    public HttpResponse getManagerRestaurant(@PathParam("id") UUID id, @PathParam("restaurantId") UUID restaurantId) {
        System.out.println("Get manager's restaurant by id: " + id);
        RestaurantManagerEntity restaurantManagerEntity = RestaurantManagerDAO.getManagerById(id);
        if (restaurantManagerEntity == null) {
            return new HttpResponse(HttpCode.HTTP_404, "Manager not found");
        }
        if (!restaurantManagerEntity.getRestaurant().getId().equals(restaurantId)) {
            return new HttpResponse(HttpCode.HTTP_404, "Restaurant not found");
        }
        return new HttpResponse(HttpCode.HTTP_200, RestaurantManagerMapper.toDTO(restaurantManagerEntity).getRestaurant());
    }
}
