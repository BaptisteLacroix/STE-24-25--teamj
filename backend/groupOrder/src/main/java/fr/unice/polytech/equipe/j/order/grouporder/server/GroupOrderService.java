package java.fr.unice.polytech.equipe.j.order.grouporder.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.unice.polytech.equipe.j.JaxsonUtils;
import fr.unice.polytech.equipe.j.grouporder.backend.GroupOrder;
import fr.unice.polytech.equipe.j.order.Order;
import picocli.CommandLine;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupOrderService implements HttpHandler {
    private final GroupOrder groupOrder;
    Logger logger = Logger.getLogger("GroupOrderService");

    public GroupOrderService(GroupOrder groupOrder) {
        this.groupOrder = groupOrder;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Accept, X-Requested-With, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization");

        String method = exchange.getRequestMethod();
        try {
            switch (method){
                case "GET":
                    logger.log(Level.INFO, "GET method called");
                    if (exchange.getRequestURI().getPath().equals("/api/{groupOrderId}/orders")) {
                        logger.log(Level.FINE, "GET all orders in the groupOrder");
                        answerWithAllOrders(exchange);
                    }else {

                    }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void answerWithAllOrders(HttpExchange exchange) throws IOException {
        List<Order> orders = groupOrder.getOrders();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        String response = JaxsonUtils.toJson(orders);
        logger.log(Level.FINE, "Response: " + response);
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
