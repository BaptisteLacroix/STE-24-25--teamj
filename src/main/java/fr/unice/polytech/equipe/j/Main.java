package fr.unice.polytech.equipe.j;

import fr.unice.polytech.equipe.j.order.DeliverableOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.OrderPriceStrategyFactory;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConnectedUser user = new ConnectedUser();
        var strat1 = OrderPriceStrategyFactory.makeSubstractKpercentforNOrder(50, 5);
        var strat2 = OrderPriceStrategyFactory.makeGiveItemForNItems(3);
        Restaurant restaurant = new Restaurant(
                strat2,
                new ArrayList<>(),
                new ArrayList<>()
        );

        for (int i = 0; i < 20; i++) {
            var order = new Order(new ArrayList<>(), restaurant);
            order.items.add(new MenuItem("Ketchup", 7));
            order.items.add(new MenuItem("BigMac", 7));
            order.items.add(new MenuItem("Ketchap", 7));

            var indivOder1 = new DeliverableOrder(order, user);
            var orderPrice = restaurant.processOrderPrice(indivOder1);
            restaurant.addOrderToHistory(indivOder1);
            // if (orderPrice.totalPrice() != 12)
            System.out.println(orderPrice);
        }
    }
}
