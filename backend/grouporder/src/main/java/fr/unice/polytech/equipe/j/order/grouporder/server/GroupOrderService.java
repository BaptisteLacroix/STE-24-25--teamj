package fr.unice.polytech.equipe.j.order.grouporder.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.JacksonConfig;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.order.grouporder.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.order.grouporder.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.order.grouporder.mapper.DTOMapper;
import fr.unice.polytech.equipe.j.order.grouporder.dto.CampusUserDTO;

import fr.unice.polytech.equipe.j.order.grouporder.backend.GroupOrder;

import fr.unice.polytech.equipe.j.order.grouporder.backend.GroupOrderProxy;
import fr.unice.polytech.equipe.j.order.grouporder.backend.IGroupOrder;
import fr.unice.polytech.equipe.j.order.grouporder.dto.GroupOrderDTO;
import fr.unice.polytech.equipe.j.order.grouporder.mapper.DTOMapper;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import static fr.unice.polytech.equipe.j.RequestUtil.DATABASE_ORDER_SERVICE_URI;
import static fr.unice.polytech.equipe.j.RequestUtil.request;
import static fr.unice.polytech.equipe.j.RequestUtil.*;

@Controller("/api/group-order")
public class GroupOrderService {
    private final Logger logger = Logger.getLogger("GroupOrderService");

    public GroupOrderService() {
    }


    @Route(value = "/{groupOrderId}/getOrders",method = HttpMethod.GET)
    public HttpResponse answerWithAllOrders(@PathParam("groupOrderId") UUID groupOrderId) {
        IGroupOrder groupOrderProxy = createGroupOrderProxy(groupOrderId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convertir la liste en une chaîne JSON
            String ordersJson = objectMapper.writeValueAsString(groupOrderProxy.getOrders());

            // Créer une réponse HTTP avec le JSON
            return new HttpResponse(HttpCode.HTTP_200, ordersJson);

        } catch (Exception e) {
            // En cas d'erreur de conversion en JSON, retourner une erreur HTTP
            return new HttpResponse(HttpCode.HTTP_500, "Error while processing orders: " + e.getMessage());
        }
    }

    @Route(value="/{userId}/create",method = HttpMethod.POST)
    public HttpResponse createGroupOrder(@PathParam("userId") UUID userdId, @BeanParam DeliveryDetailsDTO deliveryDetailsDTO) {
        try {
            GroupOrder groupOrder1 = new GroupOrder(deliveryDetailsDTO);
            GroupOrderDTO groupOrderDTO = new GroupOrderDTO(groupOrder1.getGroupOrderId(),null,new ArrayList<CampusUserDTO>(),null, "pending");
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(groupOrderDTO);
            request(DATABASE_GROUPORDER_SERVICE_URI,"/create",HttpMethod.POST,json);
            IGroupOrder groupOrderProxy = createGroupOrderProxy(groupOrder1.getGroupOrderId());
            try {
                java.net.http.HttpResponse<String> user = request(DATABASE_CAMPUS_USER_SERVICE_URI, "/" + userdId.toString(), HttpMethod.GET, null);
                CampusUserDTO userDTO = new ObjectMapper().readValue(user.body(), CampusUserDTO.class);
                return groupOrderProxy.addUser(userDTO);
            } catch (Exception e) {
                return new HttpResponse(HttpCode.HTTP_400,"User can't be added to his groupOrder Creation.");
            }
        } catch (Exception e) {
            return new HttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }



    @Route(value = "/{groupOrderId}/join/{userId}", method = HttpMethod.PUT)
    public HttpResponse joinGroupOrder(@PathParam("groupOrderId") UUID groupOrderId,@PathParam("userId") UUID userId) {
    try {
            java.net.http.HttpResponse<String> user = request(DATABASE_CAMPUS_USER_SERVICE_URI, "/"+userId.toString(),HttpMethod.GET, null);
            IGroupOrder groupOrderProxy = createGroupOrderProxy(groupOrderId);
            CampusUserDTO userDTO = new ObjectMapper().readValue(user.body(), CampusUserDTO.class);
            return groupOrderProxy.addUser(userDTO);
        } catch (Exception e) {
            return new HttpResponse(HttpCode.HTTP_500,"User can't join groupOrder");
        }

    }

    @Route(value = "/{groupOrderId}/{userId}/validate", method = HttpMethod.PUT)
    public HttpResponse validateGroupOrder(@PathParam("groupOrderId") UUID groupOrderId, @PathParam("userId") UUID userId) {
        try{
            IGroupOrder groupOrderProxy = createGroupOrderProxy(groupOrderId);
            java.net.http.HttpResponse<String> response = request(DATABASE_CAMPUS_USER_SERVICE_URI, "/" + userId.toString(), HttpMethod.GET, null);
            CampusUserDTO userDTO = new ObjectMapper().readValue(response.body(), CampusUserDTO.class);
            return groupOrderProxy.validate(userDTO);
        } catch (Exception e) {
            return new HttpResponse(HttpCode.HTTP_500, "GroupOrder not validated");
        }
    }


    public IGroupOrder createGroupOrderProxy(UUID groupOrderId){
        try {
            java.net.http.HttpResponse<String> groupOrderResponse = request(DATABASE_GROUPORDER_SERVICE_URI, "/" + groupOrderId, HttpMethod.GET, null);
            ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
            GroupOrderDTO groupOrderDTO = objectMapper.readValue(groupOrderResponse.body(), GroupOrderDTO.class);
            return new GroupOrderProxy(DTOMapper.toGroupOrder(groupOrderDTO));
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
    }
}
