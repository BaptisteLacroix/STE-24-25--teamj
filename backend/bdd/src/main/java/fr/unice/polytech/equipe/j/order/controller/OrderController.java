package fr.unice.polytech.equipe.j.order.controller;

import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.order.dao.OrderDAO;
import fr.unice.polytech.equipe.j.order.dto.IndividualOrderDTO;
import fr.unice.polytech.equipe.j.order.dto.OrderDTO;
import fr.unice.polytech.equipe.j.order.entities.IndividualOrderEntity;
import fr.unice.polytech.equipe.j.order.entities.OrderEntity;
import fr.unice.polytech.equipe.j.order.mapper.IndividualOrderMapper;
import fr.unice.polytech.equipe.j.order.mapper.OrderMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller("/api/database/orders")
public class OrderController {

    // Route to fetch all orders
    @Route(value = "/all", method = HttpMethod.GET)
    public HttpResponse getAllOrders() {
        List<OrderEntity> orders = OrderDAO.getAllOrders();
        if (orders != null) {
            // Convert entities to DTOs using the OrderMapper
            return new HttpResponse(HttpCode.HTTP_200, orders.stream().map(OrderMapper::toDTO).collect(Collectors.toList()));
        }
        return new HttpResponse(HttpCode.HTTP_400, "Error while fetching orders");
    }

    // Route to create a new order
    @Route(value = "/create", method = HttpMethod.POST)
    public HttpResponse createOrder(@BeanParam OrderDTO orderDTO) {
        // Convert the DTO to entity using the OrderMapper
        OrderEntity orderEntity = OrderMapper.toEntity(orderDTO);
        // Save the order to the database
        return OrderDAO.save(orderEntity);
    }

    // Route to get an order by its UUID
    @Route(value = "/{id}", method = HttpMethod.GET)
    public HttpResponse getOrderById(@PathParam("id") UUID id) {
        OrderEntity orderEntity = OrderDAO.getOrderById(id);
        if (orderEntity != null) {
            return new HttpResponse(HttpCode.HTTP_200, OrderMapper.toDTO(orderEntity));
        }
        return new HttpResponse(HttpCode.HTTP_400, "Order not found");
    }

    // Route to update an order (PUT request)
    @Route(value = "/{id}", method = HttpMethod.PUT)
    public HttpResponse updateOrder(@PathParam("id") UUID id, @BeanParam OrderDTO orderDTO) {
        OrderEntity orderEntity = OrderMapper.toEntity(orderDTO);
        if (orderEntity != null) {
            return OrderDAO.update(orderEntity);
        }
        return new HttpResponse(HttpCode.HTTP_400, "Order not found");
    }

    // Route to delete an order by its UUID
    @Route(value = "/{id}", method = HttpMethod.DELETE)
    public HttpResponse deleteOrder(@PathParam("id") UUID id) {
        // Delete the order by ID
        return OrderDAO.delete(id);
    }


    // TODO

    // Route to create an individual order
    @Route(value = "/individual/create", method = HttpMethod.POST)
    public HttpResponse createIndividualOrder(@BeanParam IndividualOrderDTO individualOrderDTO) {
        // Convert the IndividualOrderDTO to IndividualOrderEntity using the IndividualOrderMapper
        IndividualOrderEntity individualOrderEntity = IndividualOrderMapper.toEntity(individualOrderDTO);

        // Save the individual order to the database
        return OrderDAO.saveIndividualOrder(individualOrderEntity);
    }

    // Route to get an individual order by its UUID
    @Route(value = "/individual/{id}", method = HttpMethod.GET)
    public HttpResponse getIndividualOrderById(@PathParam("id") UUID id) {
        IndividualOrderEntity individualOrderEntity = OrderDAO.getIndividualOrderById(id);
        if (individualOrderEntity != null) {
            // Convert entity to DTO using the IndividualOrderMapper
            return new HttpResponse(HttpCode.HTTP_200, IndividualOrderMapper.toDTO(individualOrderEntity));
        }
        return new HttpResponse(HttpCode.HTTP_400, "Individual order not found");
    }
}
