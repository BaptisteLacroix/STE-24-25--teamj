package fr.unice.polytech.equipe.j.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.order.dao.GroupOrderDAO;
import fr.unice.polytech.equipe.j.order.dao.OrderDAO;
import fr.unice.polytech.equipe.j.order.dto.GroupOrderDTO;
import fr.unice.polytech.equipe.j.order.dto.OrderDTO;
import fr.unice.polytech.equipe.j.order.entities.GroupOrderEntity;
import fr.unice.polytech.equipe.j.order.mapper.GroupOrderMapper;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.entities.RestaurantEntity;
import fr.unice.polytech.equipe.j.restaurant.mapper.RestaurantMapper;

import java.util.UUID;

@Controller("/api/database/groupOrders")
public class GroupOrderController {

    @Route(value = "/create", method = HttpMethod.POST)
    public HttpResponse createGroupOrder(@BeanParam GroupOrderDTO dto) {
        System.out.println("Creating groupOrder: " + dto.getGroupOrderId());
        GroupOrderEntity groupOrderEntity = GroupOrderMapper.toEntity(dto);
        GroupOrderDAO.save(groupOrderEntity);
        return new HttpResponse(HttpCode.HTTP_201, groupOrderEntity.getId());
    }

    @Route(value = "/update", method = HttpMethod.PUT)
    public HttpResponse updateGroupOrder(@BeanParam GroupOrderDTO groupOrderDTO) {
        System.out.println("Update GroupOrder");
        GroupOrderEntity groupOrderEntity = GroupOrderMapper.toEntity(groupOrderDTO);
        GroupOrderDAO.save(groupOrderEntity);
        return new HttpResponse(HttpCode.HTTP_201,groupOrderEntity.getId());
    }




    @Route(value = "/{id}", method = HttpMethod.GET)
    public HttpResponse getGroupOrderById(@PathParam("id") UUID id) {
        System.out.println("Get Group Order by id: " + id);
        GroupOrderEntity groupOrderEntity = GroupOrderDAO.getGroupOrderById(id);;
        if (groupOrderEntity != null) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                String json = ow.writeValueAsString(GroupOrderMapper.toDTO(groupOrderEntity));
                return new HttpResponse(HttpCode.HTTP_200, json);
            } catch (Exception e) {
                e.printStackTrace();
                return new HttpResponse(HttpCode.HTTP_500, "Internal server error");
            }
        } else {
            return new HttpResponse(HttpCode.HTTP_404, "GroupOrder not found");
        }
    }
}