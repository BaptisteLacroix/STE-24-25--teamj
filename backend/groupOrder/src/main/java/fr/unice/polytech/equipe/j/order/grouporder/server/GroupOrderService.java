package java.fr.unice.polytech.equipe.j.order.grouporder.server;

import fr.unice.polytech.equipe.j.flexiblerestserver.Controller;
import fr.unice.polytech.equipe.j.flexiblerestserver.HttpMethod;
import fr.unice.polytech.equipe.j.flexiblerestserver.Route;
import java.fr.unice.polytech.equipe.j.order.grouporder.backend.GroupOrder;
import java.fr.unice.polytech.equipe.j.order.Order;

import java.fr.unice.polytech.equipe.j.order.grouporder.backend.GroupOrderProxy;
import java.fr.unice.polytech.equipe.j.order.grouporder.backend.GroupOrderRepository;
import java.util.List;
import java.util.logging.Logger;

@Controller("/GroupOrder")
public class GroupOrderService {
    private final
    Logger logger = Logger.getLogger("GroupOrderService");
    GroupOrder groupOrder;
    GroupOrderProxy groupOrderProxy = new GroupOrderProxy(groupOrder);

    public GroupOrderService() {
    }


    @Route(value = "/get",method = HttpMethod.GET)
    private List<Order> answerWithAllOrders() {
        return groupOrderProxy.getOrders();
    }

    @Route(value="/create",method = HttpMethod.POST)
    private GroupOrder createGroupOrder() {
        return new GroupOrder(null);
    }

    @Route(value = "join/{orderId}", method = HttpMethod.PUT)
    private void joinGroupOrder(int id){

    }
}
