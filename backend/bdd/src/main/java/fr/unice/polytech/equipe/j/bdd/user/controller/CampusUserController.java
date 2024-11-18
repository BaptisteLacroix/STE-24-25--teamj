package fr.unice.polytech.equipe.j.bdd.user.controller;

import fr.unice.polytech.equipe.j.bdd.user.dao.CampusUserDAO;
import fr.unice.polytech.equipe.j.bdd.user.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.bdd.user.mapper.CampusUserMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class CampusUserController {
    @PostMapping
    public void createUser(@RequestBody CampusUserDTO dto) {
        CampusUserDAO.save(CampusUserMapper.toEntity(dto));
    }

    @GetMapping
    public List<CampusUserDTO> getAllUsers() {
        return CampusUserDAO.getAll().stream().map(CampusUserMapper::toDTO).collect(Collectors.toList());
    }
}
