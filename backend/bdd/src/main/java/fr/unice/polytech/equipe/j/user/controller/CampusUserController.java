package fr.unice.polytech.equipe.j.user.controller;

import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.user.dao.CampusUserDAO;
import fr.unice.polytech.equipe.j.user.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.user.mapper.CampusUserMapper;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;

import java.util.stream.Collectors;

@Controller("/api/users")
public class CampusUserController {
    @Route(value = "/create", method = HttpMethod.POST)
    public void createUser(CampusUserDTO dto) {
        CampusUserDAO.save(CampusUserMapper.toEntity(dto));
    }

    @Route(value = "/all", method = HttpMethod.GET)
    public HttpResponse getAllUsers() {
        return new HttpResponse(HttpCode.HTTP_200, CampusUserDAO.getAll().stream().map(CampusUserMapper::toDTO).collect(Collectors.toList()));
    }
}
