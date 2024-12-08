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

/**
 * Controller class for handling HTTP requests related to orders.
 * It provides endpoints to fetch, create, update, and delete both regular and individual orders.
 */
@Controller("/api/database/orders")
public class OrderController {

    /**
     * Retrieves all orders from the database, excluding individual orders.
     *
     * @return an HttpResponse containing a list of OrderDTOs representing all orders in the system.
     */
    @Route(value = "/all", method = HttpMethod.GET)
    public HttpResponse getAllOrders() {
        System.out.println("Get all orders");
        List<OrderEntity> orders = OrderDAO.getAllOrders();
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (OrderEntity order : orders) {
            if (order instanceof IndividualOrderEntity) {
                continue;  // Skip individual orders
            }
            orderDTOS.add(OrderMapper.toDTO(order));
        }
        return new HttpResponse(HttpCode.HTTP_200, orderDTOS);
    }

    /**
     * Retrieves all individual orders from the database.
     *
     * @return an HttpResponse containing a list of IndividualOrderDTOs, or an error message if no individual orders are found.
     */
    @Route(value = "/individual/all", method = HttpMethod.GET)
    public HttpResponse getAllIndividualOrders() {
        System.out.println("Get all individual orders");
        List<IndividualOrderEntity> orders = OrderDAO.getAllIndividualOrders();
        if (orders != null) {
            return new HttpResponse(HttpCode.HTTP_200, orders.stream().map(IndividualOrderMapper::toDTO).collect(Collectors.toList()));
        }
        return new HttpResponse(HttpCode.HTTP_400, "Error while fetching orders");
    }

    /**
     * Creates a new order based on the provided OrderDTO.
     *
     * @param orderDTO the OrderDTO containing the details of the new order.
     * @return an HttpResponse with the HTTP status code and the ID of the created order.
     */
    @Route(value = "/create", method = HttpMethod.POST)
    public HttpResponse createOrder(@BeanParam OrderDTO orderDTO) {
        System.out.println("Create order");
        OrderEntity orderEntity = OrderMapper.toEntity(orderDTO);
        return OrderDAO.save(orderEntity);
    }

    /**
     * Retrieves an order by its unique UUID.
     *
     * @param id the UUID of the order to retrieve.
     * @return an HttpResponse containing the OrderDTO of the requested order or an error message if not found.
     */
    @Route(value = "/{id}", method = HttpMethod.GET)
    public HttpResponse getOrderById(@PathParam("id") UUID id) {
        System.out.println("Get order by id: " + id);
        OrderEntity orderEntity = OrderDAO.getOrderById(id);
        if (orderEntity != null && !(orderEntity instanceof IndividualOrderEntity)) {
            return new HttpResponse(HttpCode.HTTP_200, OrderMapper.toDTO(orderEntity));
        }
        return new HttpResponse(HttpCode.HTTP_400, "Order not found");
    }

    /**
     * Updates an existing order based on the provided OrderDTO.
     *
     * @param orderDTO the OrderDTO containing the updated details of the order.
     * @return an HttpResponse with the status of the update operation.
     */
    @Route(value = "/update", method = HttpMethod.PUT)
    public HttpResponse updateOrder(@BeanParam OrderDTO orderDTO) {
        System.out.println("Update order");
        OrderEntity orderEntity = OrderMapper.toEntity(orderDTO);
        return OrderDAO.save(orderEntity);
    }

    /**
     * Deletes an order by its UUID.
     *
     * @param id the UUID of the order to delete.
     * @return an HttpResponse with the result of the deletion.
     */
    @Route(value = "/{id}", method = HttpMethod.DELETE)
    public HttpResponse deleteOrder(@PathParam("id") UUID id) {
        System.out.println("Delete order");
        return OrderDAO.delete(id);
    }

    /**
     * Creates a new individual order based on the provided IndividualOrderDTO.
     *
     * @param individualOrderDTO the IndividualOrderDTO containing the details of the new individual order.
     * @return an HttpResponse with the HTTP status code and the ID of the created individual order.
     */
    @Route(value = "/individual/create", method = HttpMethod.POST)
    public HttpResponse createIndividualOrder(@BeanParam IndividualOrderDTO individualOrderDTO) {
        System.out.println("Create individual order");
        IndividualOrderEntity individualOrderEntity = IndividualOrderMapper.toEntity(individualOrderDTO);
        return OrderDAO.save(individualOrderEntity);
    }

    /**
     * Updates an existing individual order based on the provided IndividualOrderDTO.
     *
     * @param orderDTO the IndividualOrderDTO containing the updated details of the individual order.
     * @return an HttpResponse with the status of the update operation.
     */
    @Route(value = "/individual/update", method = HttpMethod.PUT)
    public HttpResponse updateOrder(@BeanParam IndividualOrderDTO orderDTO) {
        System.out.println("Update individual order");
        IndividualOrderEntity orderEntity = IndividualOrderMapper.toEntity(orderDTO);
        return OrderDAO.save(orderEntity);
    }

    /**
     * Retrieves an individual order by its unique UUID.
     *
     * @param id the UUID of the individual order to retrieve.
     * @return an HttpResponse containing the IndividualOrderDTO of the requested individual order or an error message if not found.
     */
    @Route(value = "/individual/{id}", method = HttpMethod.GET)
    public HttpResponse getIndividualOrderById(@PathParam("id") UUID id) {
        System.out.println("Get individual order by id: " + id);
        IndividualOrderEntity individualOrderEntity = OrderDAO.getIndividualOrderById(id);
        if (individualOrderEntity != null) {
            IndividualOrderDTO individualOrderDTO = IndividualOrderMapper.toDTO(individualOrderEntity);
            return new HttpResponse(HttpCode.HTTP_200, individualOrderDTO);
        }
        return new HttpResponse(HttpCode.HTTP_404, new ArrayList<>());
    }
}
