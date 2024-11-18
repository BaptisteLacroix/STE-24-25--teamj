package fr.unice.polytech.equipe.j.user.controller;

import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.user.dao.RestaurantManagerDAO;
import fr.unice.polytech.equipe.j.user.dto.RestaurantManagerDTO;
import fr.unice.polytech.equipe.j.user.mapper.RestaurantManagerMapper;

import java.util.List;
import java.util.stream.Collectors;

@Controller("/api/managers")
public class RestaurantManagerController {

    @Route(value = "/create", method = HttpMethod.POST)
    public void createManager(RestaurantManagerDTO dto) {
        RestaurantManagerDAO.save(RestaurantManagerMapper.toEntity(dto));
    }

    @Route(value = "/all", method = HttpMethod.GET)
    public List<RestaurantManagerDTO> getAllManagers() {
        return RestaurantManagerDAO.getAll().stream().map(RestaurantManagerMapper::toDTO).collect(Collectors.toList());
    }
}
