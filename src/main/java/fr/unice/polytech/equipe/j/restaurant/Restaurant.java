package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.DeliverableOrder;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Restaurant (
    OrderPriceStrategy orderPriceStrategy,
    List<DeliverableOrder> individualOrdersHistory,
    List<GroupOrder> groupOrdersHistory
){
    public void addOrderToHistory(DeliverableOrder order) {
        this.individualOrdersHistory.add(order);
    }

    public void addOrderToHistory(GroupOrder order) {
        this.groupOrdersHistory.add(order);
    }

    public OrderPrice processOrderPrice(DeliverableOrder indivOder) {
        return this.orderPriceStrategy().processOrderPrice(indivOder.user(), indivOder.order(), this);
    }

    public Map<Order, OrderPrice> processOrderPrice(GroupOrder groupOrder) {
        return groupOrder.orderToConnectedUserMap().entrySet().stream()
                .map((entry)-> {
                    Order order = entry.getKey();
                    ConnectedUser user = entry.getValue();
                    return Map.entry(
                            order,
                            this.orderPriceStrategy().processOrderPrice(user, order, this)
                    );
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
