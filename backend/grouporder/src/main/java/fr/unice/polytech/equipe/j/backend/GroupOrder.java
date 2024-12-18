package fr.unice.polytech.equipe.j.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.dto.OrderDTO;
import fr.unice.polytech.equipe.j.dto.OrderStatus;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.mapper.DTOMapper;
import fr.unice.polytech.equipe.j.utils.JacksonConfig;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fr.unice.polytech.equipe.j.utils.RequestUtil.DATABASE_GROUPORDER_SERVICE_URI;
import static fr.unice.polytech.equipe.j.utils.RequestUtil.request;

public class GroupOrder implements IGroupOrder {
    @Setter
    private UUID groupOrderId;
    private final List<OrderDTO> orders = new ArrayList<>();
    private final DeliveryDetailsDTO deliveryDetails;
    private final List<CampusUserDTO> campusUsers = new ArrayList<>();
    private OrderStatus status = OrderStatus.PENDING;

    @Override
    public List<CampusUserDTO> getUsers() {
        return this.campusUsers;
    }

    @Override
    public HttpResponse addUser(CampusUserDTO user) {
        try {
            // if the user is not already in the group order, add it, check with the id
            if (this.getUsers().stream().anyMatch(u -> u.getId().equals(user.getId()))) {
                return new HttpResponse(HttpCode.HTTP_400, "User already in group order");
            }
            this.getUsers().add(user);
            try {
                ObjectMapper mapper = JacksonConfig.configureObjectMapper();
                java.net.http.HttpResponse response = request(DATABASE_GROUPORDER_SERVICE_URI, "/update", HttpMethod.PUT, mapper.writeValueAsString(DTOMapper.toGroupOrderDTO(this)));
                if (response.statusCode() == 201) {
                    return new HttpResponse(HttpCode.HTTP_201, this.groupOrderId);
                } else {
                    return new HttpResponse(HttpCode.HTTP_500, response.body());
                }
            } catch (Exception e) {
                return new HttpResponse(HttpCode.HTTP_400, "Can't update groupOrder");
            }
        } catch (Exception e) {
            return new HttpResponse(HttpCode.HTTP_404, "Can't find groupOrder");
        }
    }

    public GroupOrder(DeliveryDetailsDTO deliveryDetails) {
        this.groupOrderId = UUID.randomUUID();
        this.deliveryDetails = deliveryDetails;
    }

    @Override
    public void addOrder(OrderDTO order) {
        this.orders.add(order);
    }

    @Override
    public HttpResponse validate(CampusUserDTO user) {
        setStatus(OrderStatus.VALIDATED);
        // Set Cancelled all orders that are not validated
        for (OrderDTO order : orders) {
            if (!order.getStatus().equals(OrderStatus.VALIDATED.toString())) {
                order.setStatus(OrderStatus.CANCELLED.toString());
            }
        }
        try {
            // Sauvegardez les changements dans la base de données
            ObjectMapper mapper = JacksonConfig.configureObjectMapper();
            java.net.http.HttpResponse response = request(DATABASE_GROUPORDER_SERVICE_URI, "/update", HttpMethod.PUT, mapper.writeValueAsString(DTOMapper.toGroupOrderDTO(this)));
            if (response.statusCode() == 201) {
                return new HttpResponse(HttpCode.HTTP_200, "GroupOrder validated successfully.");
            } else {
                return new HttpResponse(HttpCode.HTTP_500, "Failed to update GroupOrder in database: " + response.body());
            }
        } catch (Exception e) {
            return new HttpResponse(HttpCode.HTTP_500, "Internal server error while validating GroupOrder: " + e.getMessage());
        }
    }

    // Getters
    @Override
    public UUID getGroupOrderId() {
        return groupOrderId;
    }

    @Override
    public List<OrderDTO> getOrders() {

        return this.orders;
    }

    @Override
    public DeliveryDetailsDTO getDeliveryDetails() {
        return deliveryDetails;
    }

    @Override
    public void setDeliveryTime(LocalDateTime deliveryTime) {
        deliveryDetails.setDeliveryTime(deliveryTime);
    }

    @Override
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GroupOrder{" +
                "groupOrderId=" + groupOrderId +
                "deliveryDetails=" + deliveryDetails +
                ", orders=" + this.getOrders() +
                '}';
    }

    @Override
    public OrderStatus getStatus() {
        return status;
    }


}
