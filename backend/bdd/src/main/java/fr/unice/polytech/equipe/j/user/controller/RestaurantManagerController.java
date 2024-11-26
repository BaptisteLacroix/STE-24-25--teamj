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

@Controller("/api/database/managers")
public class RestaurantManagerController {

    @Route(value = "/create", method = HttpMethod.POST)
    public HttpResponse createManager(@BeanParam RestaurantManagerDTO dto) {
        return RestaurantManagerDAO.save(RestaurantManagerMapper.toEntity(dto));
    }

    @Route(value = "/all", method = HttpMethod.GET)
    public HttpResponse getAllManagers() {
        List<RestaurantManagerDTO> managers = RestaurantManagerDAO.getAll().stream()
                .map(RestaurantManagerMapper::toDTO)
                .collect(Collectors.toList());
        return new HttpResponse(HttpCode.HTTP_200, managers);
    }

    @Route(value = "/{id}", method = HttpMethod.GET)
    public HttpResponse getManagerById(@PathParam("id") UUID id) {
        RestaurantManagerEntity restaurantManagerEntity = RestaurantManagerDAO.getManagerById(id);
        if (restaurantManagerEntity != null) {
            return new HttpResponse(HttpCode.HTTP_200, RestaurantManagerMapper.toDTO(restaurantManagerEntity));
        } else {
            return new HttpResponse(HttpCode.HTTP_404, "Manager not found");
        }
    }

    @Route(value = "/{id}", method = HttpMethod.DELETE)
    public HttpResponse deleteManagerById(@PathParam("id") UUID id) {
        return RestaurantManagerDAO.delete(id);
    }
}
