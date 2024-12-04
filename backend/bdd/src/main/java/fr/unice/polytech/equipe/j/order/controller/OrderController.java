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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller("/api/database/orders")
public class OrderController {

    // Route to fetch all orders
    @Route(value = "/all", method = HttpMethod.GET)
    public HttpResponse getAllOrders() {
        System.out.println("Get all orders");
        List<OrderEntity> orders = OrderDAO.getAllOrders();
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (OrderEntity order : orders) {
            if (order instanceof IndividualOrderEntity) {
                continue;
            }
            orderDTOS.add(OrderMapper.toDTO(order));
        }
        return new HttpResponse(HttpCode.HTTP_200, orderDTOS);
    }

    @Route(value = "/individual/all", method = HttpMethod.GET)
    public HttpResponse getAllIndividualOrders() {
        System.out.println("Get all orders");
        List<IndividualOrderEntity> orders = OrderDAO.getAllIndividualOrders();
        if (orders != null) {
            return new HttpResponse(HttpCode.HTTP_200, orders.stream().map(IndividualOrderMapper::toDTO).collect(Collectors.toList()));
        }
        return new HttpResponse(HttpCode.HTTP_400, "Error while fetching orders");
    }

    // Route to create a new order
    @Route(value = "/create", method = HttpMethod.POST)
    public HttpResponse createOrder(@BeanParam OrderDTO orderDTO) {
        System.out.println("Create order");
        OrderEntity orderEntity = OrderMapper.toEntity(orderDTO);
        return OrderDAO.save(orderEntity);
    }

    // Route to get an order by its UUID
    @Route(value = "/{id}", method = HttpMethod.GET)
    public HttpResponse getOrderById(@PathParam("id") UUID id) {
        System.out.println("Get order by id: " + id);
        OrderEntity orderEntity = OrderDAO.getOrderById(id);
        if (orderEntity != null && !(orderEntity instanceof IndividualOrderEntity)) {
            return new HttpResponse(HttpCode.HTTP_200, OrderMapper.toDTO(orderEntity));
        }
        return new HttpResponse(HttpCode.HTTP_400, "Order not found");
    }

    // Route to update an order (PUT request)
    @Route(value = "/update", method = HttpMethod.PUT)
    public HttpResponse updateOrder(@BeanParam OrderDTO orderDTO) {
        System.out.println("Update order");
        OrderEntity orderEntity = OrderMapper.toEntity(orderDTO);
        return OrderDAO.save(orderEntity);
    }

    // Route to delete an order by its UUID
    @Route(value = "/{id}", method = HttpMethod.DELETE)
    public HttpResponse deleteOrder(@PathParam("id") UUID id) {
        System.out.println("Delete order");
        return OrderDAO.delete(id);
    }

    // Route to create an individual order
    @Route(value = "/individual/create", method = HttpMethod.POST)
    public HttpResponse createIndividualOrder(@BeanParam IndividualOrderDTO individualOrderDTO) {
        System.out.println("Create individual order");
        IndividualOrderEntity individualOrderEntity = IndividualOrderMapper.toEntity(individualOrderDTO);
        return OrderDAO.save(individualOrderEntity);
    }

    @Route(value = "/individual/update", method = HttpMethod.PUT)
    public HttpResponse updateOrder(@BeanParam IndividualOrderDTO orderDTO) {
        System.out.println("Update individual order");
        IndividualOrderEntity orderEntity = IndividualOrderMapper.toEntity(orderDTO);
        return OrderDAO.save(orderEntity);
    }


    // Route to get an individual order by its UUID
    @Route(value = "/individual/{id}", method = HttpMethod.GET)
    public HttpResponse getIndividualOrderById(@PathParam("id") UUID id) {
        System.out.println("Get individual order by id: " + id);
        IndividualOrderEntity individualOrderEntity = OrderDAO.getIndividualOrderById(id);
        if (individualOrderEntity != null) {
            IndividualOrderDTO individualOrderDTO = IndividualOrderMapper.toDTO(individualOrderEntity);
            return new HttpResponse(HttpCode.HTTP_200, individualOrderDTO);
        }
        return new HttpResponse(HttpCode.HTTP_400, "Individual order not found");
    }
}
