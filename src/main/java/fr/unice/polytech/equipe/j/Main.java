package fr.unice.polytech.equipe.j;

import fr.unice.polytech.equipe.j.order.DeliverableOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.restaurant.Menu;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.OrderPriceStrategyFactory;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        ConnectedUser user = new ConnectedUser("mail", "psw", 4);
        var strat1 = OrderPriceStrategyFactory.makeSubstractKpercentforNOrder(50, 5);
        var strat2 = OrderPriceStrategyFactory.makeGiveItemForNItems(3);

        var orderItems = List.of(
                new MenuItem("Ketchup", 7),
                new MenuItem("BigMac", 7),
                new MenuItem("Ketchap", 7)
        );

        var restaurant = new Restaurant(
                "Test Restaurant",
                LocalDateTime.of(2024, 10, 1, 9, 0),
                LocalDateTime.of(2024, 10, 1, 21, 0),
                new Menu.MenuBuilder().addMenuItems(orderItems).build()
                );


        for (int i = 0; i < 20; i++) {
            var order = new Order(restaurant, UUID.randomUUID());
            order.addItem(new MenuItem("Ketchup", 7));
            order.addItem(new MenuItem("BigMac", 7));
            order.addItem(new MenuItem("Ketchap", 7));

            var indivOder1 = new DeliverableOrder(order, user);
            var orderPrice = restaurant.processOrderPrice(indivOder1);
            restaurant.addOrderToHistory(indivOder1);
            // if (orderPrice.totalPrice() != 12)
            System.out.println(orderPrice);
        }
    }
}
