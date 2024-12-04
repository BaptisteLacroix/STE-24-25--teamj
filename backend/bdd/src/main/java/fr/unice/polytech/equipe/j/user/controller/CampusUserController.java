package fr.unice.polytech.equipe.j.user.controller;

import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.user.dao.CampusUserDAO;
import fr.unice.polytech.equipe.j.user.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.user.mapper.CampusUserMapper;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;

import java.util.UUID;
import java.util.stream.Collectors;

@Controller("/api/database/users")
public class CampusUserController {
    @Route(value = "/create", method = HttpMethod.POST)
    public void createUser(CampusUserDTO dto) {
        CampusUserDAO.save(CampusUserMapper.toEntity(dto));
    }

    @Route(value = "/all", method = HttpMethod.GET)
    public HttpResponse getAllUsers() {
        return new HttpResponse(HttpCode.HTTP_200, CampusUserDAO.getAll().stream().map(CampusUserMapper::toDTO).collect(Collectors.toList()));
    }

    @Route(value = "/{id}", method = HttpMethod.GET)
    public HttpResponse getUser(@PathParam("{userId}")UUID userId){
        return new HttpResponse(HttpCode.HTTP_200,CampusUserDAO.getUser(userId));
    }
}
