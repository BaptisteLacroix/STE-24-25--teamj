package fr.unice.polytech.equipe.j.user.controller;

import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.user.dao.CampusUserDAO;
import fr.unice.polytech.equipe.j.user.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.user.entities.CampusUserEntity;
import fr.unice.polytech.equipe.j.user.mapper.CampusUserMapper;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;

import java.util.UUID;
import java.util.stream.Collectors;

@Controller("/api/database/campusUsers")
public class CampusUserController {
    @Route(value = "/create", method = HttpMethod.POST)
    public HttpResponse createUser(@BeanParam CampusUserDTO dto) {
        System.out.println("Create user");
        return CampusUserDAO.save(CampusUserMapper.toEntity(dto));
    }

    @Route(value = "/all", method = HttpMethod.GET)
    public HttpResponse getAllUsers() {
        System.out.println("Get all users");
        return new HttpResponse(HttpCode.HTTP_200, CampusUserDAO.getAll().stream().map(CampusUserMapper::toDTO).collect(Collectors.toList()));
    }

    @Route(value = "/{id}", method = HttpMethod.GET)
    public HttpResponse getUserById(@PathParam("id") UUID id) {
        System.out.println("Get user by id: " + id);
        CampusUserEntity user = CampusUserDAO.getUserById(id);
        if (user == null) {
            return new HttpResponse(HttpCode.HTTP_404, "User not found");
        }
        return new HttpResponse(HttpCode.HTTP_200, CampusUserMapper.toDTO(user));
    }

    @Route(value = "/{id}", method = HttpMethod.DELETE)
    public HttpResponse deleteUserById(@PathParam("id") UUID id) {
        System.out.println("Delete user by id: " + id);
        return CampusUserDAO.delete(id);
    }
}
