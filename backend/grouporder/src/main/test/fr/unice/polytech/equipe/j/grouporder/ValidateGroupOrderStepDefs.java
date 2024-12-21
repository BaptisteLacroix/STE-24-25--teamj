package fr.unice.polytech.equipe.j.grouporder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.backend.IGroupOrder;
import fr.unice.polytech.equipe.j.dto.*;
import fr.unice.polytech.equipe.j.restaurant.RestaurantDatabaseSeeder;
import fr.unice.polytech.equipe.j.user.UserDatabaseSeeder;
import fr.unice.polytech.equipe.j.utils.TimeUtils;
import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.net.http.HttpResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static fr.unice.polytech.equipe.j.utils.RequestUtil.request;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ValidateGroupOrderStepDefs {
    private IGroupOrder groupOrder;
    private OrderDTO orderUser1;
    private OrderDTO orderUser2;
    private UUID usrId1;
    private java.net.http.HttpResponse<String> creationResponse;
    private UUID groupOrderId;
    private GroupOrderDTO groupOrderDTO;

    @Before
    public void setUp() {
        RestaurantDatabaseSeeder.seedDatabase();
        UserDatabaseSeeder.seedDatabase();
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
    }

    @Given("[ValidateGroupOrder]the user {string} is registered")
    public void validategrouporderTheUserIsRegistered(String id) {
        this.usrId1 = UUID.fromString(id);
        this.creationResponse = request("http://localhost:5008/api/campus-user", "/"+usrId1, HttpMethod.GET, "");
        assertFalse(creationResponse.body().isEmpty());
    }


    @Given("[ValidateGroupOrder]the user creates a group order with delivery location {string} and delivery time of {int}:{int} PM")
    public void validate_group_order_the_user_creates_a_group_order_with_delivery_location_and_delivery_time_of_pm(String string, Integer hour, Integer minute) throws JsonProcessingException {
        var urlGroupOrder = "http://localhost:5008/api/group-order";
        DeliveryLocationDTO dld = new DeliveryLocationDTO(UUID.fromString("5fbac108-4198-4ee9-adae-1400499736d3"), "home", string);
        DeliveryDetailsDTO ddd = new DeliveryDetailsDTO(UUID.fromString("c5cbe3d1-bb5c-4be5-a9c0-0ba9f6264218"), dld, LocalDateTime.now().withHour(hour).withMinute(minute));
        this.creationResponse = request(urlGroupOrder, "/"+usrId1+"/create", HttpMethod.POST, new ObjectMapper().writeValueAsString(ddd));
        assertFalse(creationResponse.body().isEmpty());
        HttpResponse<String> groupOrderResponse = request(urlGroupOrder, "/"+usrId1, HttpMethod.GET, "");
        this.groupOrderDTO = new ObjectMapper().readValue(groupOrderResponse.body(), GroupOrderDTO.class);
        assertNotNull(groupOrderDTO);
        Assertions.assertEquals(hour, this.groupOrderDTO.getDeliveryDetails().getDeliveryTime().getHour());
        Assertions.assertEquals(minute, this.groupOrderDTO.getDeliveryDetails().getDeliveryTime().getMinute());
        assertEquals(1, groupOrderDTO.getUsers().size());

        HttpResponse<String> userResponse = request("http://localhost:5008/api/campusUsers", "/"+usrId1, HttpMethod.GET, "");
        CampusUserDTO userDTO = new ObjectMapper().readValue(userResponse.body(), CampusUserDTO.class);
        assertEquals(userDTO, groupOrder.getUsers().getFirst());
    }

    @Given("[ValidateGroupOrder]the user receives a group order identifier")
    public void validate_group_order_the_user_receives_a_group_order_identifier() throws JsonProcessingException {
        this.groupOrderId = new ObjectMapper().readValue(this.creationResponse.body(), UUID.class);
        assertNotNull(this.groupOrderId);
    }




}
