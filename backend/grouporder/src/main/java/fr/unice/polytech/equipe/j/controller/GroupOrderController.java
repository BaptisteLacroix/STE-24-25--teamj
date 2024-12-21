package fr.unice.polytech.equipe.j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.dto.OrderDTO;
import fr.unice.polytech.equipe.j.utils.JacksonConfig;
import fr.unice.polytech.equipe.j.annotations.BeanParam;
import fr.unice.polytech.equipe.j.annotations.Controller;
import fr.unice.polytech.equipe.j.annotations.PathParam;
import fr.unice.polytech.equipe.j.annotations.Route;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.mapper.DTOMapper;

import fr.unice.polytech.equipe.j.backend.GroupOrder;

import fr.unice.polytech.equipe.j.backend.GroupOrderProxy;
import fr.unice.polytech.equipe.j.backend.IGroupOrder;
import fr.unice.polytech.equipe.j.dto.GroupOrderDTO;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import static fr.unice.polytech.equipe.j.utils.RequestUtil.request;
import static fr.unice.polytech.equipe.j.utils.RequestUtil.*;

@Controller("/api/group-order")
public class GroupOrderController {
    private ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
    private final Logger logger = Logger.getLogger("GroupOrderService");

    public GroupOrderController() {
    }

    @Route(value = "/{groupOrderId}", method = HttpMethod.GET)
    public HttpResponse getGroupOrder(@PathParam("groupOrderId") UUID groupOrderId) {
        try {
            IGroupOrder groupOrderProxy = createGroupOrderProxy(groupOrderId);
            if (groupOrderProxy == null) {
                System.out.println("[getGroupOrder] Error while processing orders");
                return new HttpResponse(HttpCode.HTTP_500, "Error while processing orders");
            }
            String groupOrderJson = objectMapper.writeValueAsString(groupOrderProxy);
            return new HttpResponse(HttpCode.HTTP_200, groupOrderJson);
        } catch (Exception e) {
            return new HttpResponse(HttpCode.HTTP_500, "Error while processing orders: " + e.getMessage());
        }
    }

    @Route(value = "/{groupOrderId}/getOrders", method = HttpMethod.GET)
    public HttpResponse answerWithAllOrders(@PathParam("groupOrderId") UUID groupOrderId) {
        IGroupOrder groupOrderProxy = createGroupOrderProxy(groupOrderId);
        if (groupOrderProxy == null) {
            System.out.println("[answerWithAllOrders] Error while processing orders");
            return new HttpResponse(HttpCode.HTTP_500, "Error while processing orders");
        }
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

    @Route(value = "/{userId}/create", method = HttpMethod.POST)
    public HttpResponse createGroupOrder(@PathParam("userId") UUID userdId, @BeanParam DeliveryDetailsDTO deliveryDetailsDTO) {
        System.out.println("test : ");
        try {
            System.out.println("Creating groupOrder");
            GroupOrder groupOrder1 = new GroupOrder(deliveryDetailsDTO);
            GroupOrderDTO groupOrderDTO = new GroupOrderDTO(groupOrder1.getGroupOrderId(), null, new ArrayList<>(), deliveryDetailsDTO, "pending");
            ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(groupOrderDTO);
            request(DATABASE_GROUPORDER_SERVICE_URI, "/create", HttpMethod.POST, json);
            IGroupOrder groupOrderProxy = createGroupOrderProxy(groupOrder1.getGroupOrderId());
            if (groupOrderProxy == null) {
                System.out.println("[createGroupOrder] Error while processing orders");
                return new HttpResponse(HttpCode.HTTP_500, "Error while processing orders");
            }
            try {
                java.net.http.HttpResponse<String> user = request(DATABASE_CAMPUS_USER_SERVICE_URI, "/" + userdId.toString(), HttpMethod.GET, null);
                CampusUserDTO userDTO = objectMapper.readValue(user.body(), CampusUserDTO.class);
                return groupOrderProxy.addUser(userDTO);
            } catch (Exception e) {
                System.out.println("[createGroupOrder] Error while processing orders");
                return new HttpResponse(HttpCode.HTTP_400, "User can't be added to his groupOrder Creation.");
            }
        } catch (Exception e) {
            return new HttpResponse(HttpCode.HTTP_500, "Internal server error: " + e.getMessage());
        }
    }

    @Route(value = "/{groupOrderId}/join/{userId}", method = HttpMethod.PUT)
    public HttpResponse joinGroupOrder(@PathParam("groupOrderId") UUID groupOrderId, @PathParam("userId") UUID userId) {
        try {
            java.net.http.HttpResponse<String> user = request(DATABASE_CAMPUS_USER_SERVICE_URI, "/" + userId.toString(), HttpMethod.GET, null);
            IGroupOrder groupOrderProxy = createGroupOrderProxy(groupOrderId);
            if (groupOrderProxy == null) {
                System.out.println("[joinGroupOrder] Error while processing orders");
                return new HttpResponse(HttpCode.HTTP_500, "Error while processing groupOrder");
            }
            CampusUserDTO userDTO = objectMapper.readValue(user.body(), CampusUserDTO.class);
            return groupOrderProxy.addUser(userDTO);
        } catch (Exception e) {
            System.out.println("[joinGroupOrder] Error while processing orders: " + e.getMessage());
            return new HttpResponse(HttpCode.HTTP_500, "User can't join groupOrder");
        }

    }

    @Route(value = "/{groupOrderId}/{userId}/validate", method = HttpMethod.PUT)
    public HttpResponse validateGroupOrder(@PathParam("groupOrderId") UUID groupOrderId, @PathParam("userId") UUID userId) {
        try {
            IGroupOrder groupOrderProxy = createGroupOrderProxy(groupOrderId);
            if (groupOrderProxy == null) {
                System.out.println("[validateGroupOrder] Error while processing orders");
                return new HttpResponse(HttpCode.HTTP_500, "Error while processing orders");
            }
            java.net.http.HttpResponse<String> response = request(DATABASE_CAMPUS_USER_SERVICE_URI, "/" + userId.toString(), HttpMethod.GET, null);
            CampusUserDTO userDTO = objectMapper.readValue(response.body(), CampusUserDTO.class);
            return groupOrderProxy.validate(userDTO);
        } catch (Exception e) {
            return new HttpResponse(HttpCode.HTTP_500, "GroupOrder not validated");
        }
    }

    @Route(value = "/user/{userId}", method = HttpMethod.GET)
    public HttpResponse getGroupOrdersByUser(@PathParam("userId") UUID userId) {
        try {
            System.out.println("Get Group Orders by user id: " + userId);
            java.net.http.HttpResponse<String> response = request(DATABASE_GROUPORDER_SERVICE_URI, "/user/" + userId.toString(), HttpMethod.GET, null);
            return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
        } catch (Exception e) {
            return new HttpResponse(HttpCode.HTTP_500, "Error while processing orders");
        }
    }

    @Route(value = "/{groupOrderId}/addOrder/{orderId}", method = HttpMethod.PUT)
    public HttpResponse addOrderToGroupOrder(@PathParam("groupOrderId") UUID groupOrderId, @PathParam("orderId") UUID orderId) {
        try {
            System.out.println("Adding order to groupOrder: " + groupOrderId);
            IGroupOrder groupOrderProxy = createGroupOrderProxy(groupOrderId);
            if (groupOrderProxy == null) {
                System.out.println("[addOrderToGroupOrder] Error while processing orders");
                return new HttpResponse(HttpCode.HTTP_500, "Error while processing orders");
            }
            java.net.http.HttpResponse<String> response = request(DATABASE_ORDER_SERVICE_URI, "/" + orderId.toString(), HttpMethod.GET, null);
            if (response.statusCode() != HttpCode.HTTP_200.getCode()) {
                System.out.println("[addOrderToGroupOrder] Error while processing orders");
                return new HttpResponse(HttpCode.HTTP_500, "Error while processing orders");
            }
            OrderDTO orderDTO = objectMapper.readValue(response.body(), OrderDTO.class);
            groupOrderProxy.addOrder(orderDTO);
            // Update the group order in the database
            ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(DTOMapper.toGroupOrderDTO(groupOrderProxy));
            response = request(DATABASE_GROUPORDER_SERVICE_URI, "/update", HttpMethod.PUT, json);
            return new HttpResponse(HttpCode.fromCode(response.statusCode()), response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(HttpCode.HTTP_500, "Error while processing orders");
        }
    }

    public IGroupOrder createGroupOrderProxy(UUID groupOrderId) {
        try {
            java.net.http.HttpResponse<String> groupOrderResponse = request(DATABASE_GROUPORDER_SERVICE_URI, "/" + groupOrderId, HttpMethod.GET, null);
            if (groupOrderResponse.statusCode() != HttpCode.HTTP_200.getCode()) {
                return null;
            }
            ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
            GroupOrderDTO groupOrderDTO = objectMapper.readValue(groupOrderResponse.body(), GroupOrderDTO.class);
            return new GroupOrderProxy(DTOMapper.toGroupOrder(groupOrderDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
