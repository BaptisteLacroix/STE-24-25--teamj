package java.fr.unice.polytech.equipe.j.order.grouporder.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.order.mapper.GroupOrderMapper;

import java.fr.unice.polytech.equipe.j.order.grouporder.backend.GroupOrder;
import java.fr.unice.polytech.equipe.j.order.Order;

import java.fr.unice.polytech.equipe.j.order.grouporder.backend.GroupOrderProxy;
import java.fr.unice.polytech.equipe.j.order.grouporder.backend.GroupOrderRepository;
import java.fr.unice.polytech.equipe.j.order.grouporder.backend.IGroupOrder;
import java.fr.unice.polytech.equipe.j.order.grouporder.dto.GroupOrderDTO;
import java.fr.unice.polytech.equipe.j.order.grouporder.mapper.DTOMapper;
import java.fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static java.fr.unice.polytech.equipe.j.RequestUtil.request;

@Controller("/GroupOrder")
public class GroupOrderService {
    private final
    Logger logger = Logger.getLogger("GroupOrderService");
    GroupOrder groupOrder;
    GroupOrderProxy groupOrderProxy = new GroupOrderProxy(groupOrder);
    // Recréer le proxy à chaque fois en récupérant le groupOrder !!
    public GroupOrderService() {
    }


    @Route(value = "{groupOrderId}/getOrders",method = HttpMethod.GET)
    private List<Order> answerWithAllOrders() {
        return groupOrderProxy.getOrders();
    }

    @Route(value="/create",method = HttpMethod.POST)
    private HttpResponse createGroupOrder() {
        try {
            GroupOrder groupOrder1 = new GroupOrder(null);
            HttpClient client = HttpClient.newHttpClient();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(groupOrder1);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5003/api/database/groupOrder/create"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if(response == null){
                return new HttpResponse(HttpCode.HTTP_400, "GroupOrder can't be created");
            }
            if (response.statusCode() == 200){
                return new HttpResponse(HttpCode.HTTP_200, "GroupOrder created");
            }
            return new HttpResponse(HttpCode.HTTP_500, "Internal Server Error: "+response.body());
        } catch (Exception e) {
            return new HttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "{groupOrderId}/join/{userId}", method = HttpMethod.PUT)
    private void joinGroupOrder(int groupOrderId, int userId) {

    }



    private IGroupOrder createGroupOrderProxy(UUID groupOrderId){
        java.net.http.HttpResponse<String> groupOrderResponse = request("database/groupOrder/" + groupOrderId, HttpMethod.GET, null);
        ObjectMapper mapper = new ObjectMapper();
        try {
            GroupOrderDTO groupOrderDTO = mapper.readValue(groupOrderResponse.body(), GroupOrderDTO.class);
            return new GroupOrderProxy(DTOMapper.toGroupOrder(groupOrderDTO));
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
    }
}
