package fr.unice.polytech.equipe.j.order.controller;

import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.order.dto.OrderDTO;

import java.util.List;

@Controller("/api/database/orders")
public class OrderController {

    @Route(value = "/all", method = HttpMethod.GET)
    public List<OrderDTO> getAllOrders() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Route(value = "/create", method = HttpMethod.POST)
    public void createOrder(@BeanParam OrderDTO dto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
