package fr.unice.polytech.equipe.j.order.grouporder.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupOrderRepository {
    private final List<IGroupOrder> groupOrders;
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

    public void addGroupOrder(IGroupOrder groupOrder) {
        groupOrders.add(groupOrder);
    }

    public void removeGroupOrder(IGroupOrder groupOrder) {
        groupOrders.remove(groupOrder);
    }

    public List<IGroupOrder> getGroupOrders() {
        return groupOrders;
    }

    public IGroupOrder findGroupOrderById(UUID id) {
        return groupOrders.stream()
                .filter(groupOrder -> groupOrder.getGroupOrderId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Group order not found"));
    }
}
