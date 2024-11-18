package fr.unice.polytech.equipe.j.order.controller;

import fr.unice.polytech.equipe.j.order.dto.OrderDTO;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return service.getAllOrders();
    }

    @PostMapping
    public void createOrder(@RequestBody OrderDTO dto) {
        service.createOrder(dto);
    }
}
