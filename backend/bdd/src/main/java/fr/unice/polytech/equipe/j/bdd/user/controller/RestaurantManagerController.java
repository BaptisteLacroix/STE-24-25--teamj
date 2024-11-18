package fr.unice.polytech.equipe.j.bdd.user.controller;

import fr.unice.polytech.equipe.j.bdd.user.dao.RestaurantManagerDAO;
import fr.unice.polytech.equipe.j.bdd.user.dto.RestaurantManagerDTO;
import fr.unice.polytech.equipe.j.bdd.user.mapper.RestaurantManagerMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/managers")
public class RestaurantManagerController {

    @PostMapping
    public void createManager(@RequestBody RestaurantManagerDTO dto) {
        RestaurantManagerDAO.save(RestaurantManagerMapper.toEntity(dto));
    }

    @GetMapping
    public List<RestaurantManagerDTO> getAllManagers() {
        return RestaurantManagerDAO.getAll().stream().map(RestaurantManagerMapper::toDTO).collect(Collectors.toList());
    }
}
