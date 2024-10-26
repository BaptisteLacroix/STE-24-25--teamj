package fr.unice.polytech.equipe.j.order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupOrderRepository {
    private final List<GroupOrderProxy> groupOrders;
    private static GroupOrderRepository instance;

    private GroupOrderRepository() {
        groupOrders = new ArrayList<>();
    }

    public static GroupOrderRepository getInstance() {
        if (instance == null) {
            instance = new GroupOrderRepository();
        }
        return instance;
    }

    public void addGroupOrder(GroupOrderProxy groupOrder) {
        groupOrders.add(groupOrder);
    }

    public void removeGroupOrder(GroupOrderProxy groupOrder) {
        groupOrders.remove(groupOrder);
    }

    public List<GroupOrderProxy> getGroupOrders() {
        return groupOrders;
    }

    public GroupOrderProxy findGroupOrderById(UUID id) {
        return groupOrders.stream()
                .filter(groupOrder -> groupOrder.getGroupOrderId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Group order not found"));
    }
}
