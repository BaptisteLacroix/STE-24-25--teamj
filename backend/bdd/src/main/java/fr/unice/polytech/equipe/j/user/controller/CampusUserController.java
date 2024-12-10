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

/**
 * Controller class for managing campus user-related HTTP requests.
 * It provides endpoints to create, retrieve, update, and delete campus users.
 */
@Controller("/api/database/campusUsers")
public class CampusUserController {

    /**
     * Creates a new campus user based on the provided CampusUserDTO.
     *
     * @param dto the CampusUserDTO containing the details of the user to be created.
     * @return an HttpResponse with the status of the operation and the ID of the created user.
     */
    @Route(value = "/create", method = HttpMethod.POST)
    public HttpResponse createUser(@BeanParam CampusUserDTO dto) {
        System.out.println("Create user");
        return CampusUserDAO.save(CampusUserMapper.toEntity(dto));
    }

    /**
     * Retrieves all campus users from the database.
     *
     * @return an HttpResponse containing a list of all campus users as CampusUserDTO objects.
     */
    @Route(value = "/all", method = HttpMethod.GET)
    public HttpResponse getAllUsers() {
        System.out.println("Get all users");
        return new HttpResponse(HttpCode.HTTP_200,
                CampusUserDAO.getAll().stream()
                        .map(CampusUserMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Retrieves a campus user by their unique ID.
     *
     * @param id the UUID of the campus user to retrieve.
     * @return an HttpResponse containing the CampusUserDTO of the requested user, or an error message if not found.
     */
    @Route(value = "/{id}", method = HttpMethod.GET)
    public HttpResponse getUserById(@PathParam("id") UUID id) {
        System.out.println("Get user by id: " + id);
        CampusUserEntity user = CampusUserDAO.getUserById(id);
        if (user == null) {
            return new HttpResponse(HttpCode.HTTP_404, "User not found");
        }
        return new HttpResponse(HttpCode.HTTP_200, CampusUserMapper.toDTO(user));
    }

    /**
     * Deletes a campus user by their unique ID.
     *
     * @param id the UUID of the user to delete.
     * @return an HttpResponse with the status of the deletion operation.
     */
    @Route(value = "/{id}", method = HttpMethod.DELETE)
    public HttpResponse deleteUserById(@PathParam("id") UUID id) {
        System.out.println("Delete user by id: " + id);
        return CampusUserDAO.delete(id);
    }
}