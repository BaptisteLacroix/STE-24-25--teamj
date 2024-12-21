package fr.unice.polytech.equipe.j.grouporder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.backend.IGroupOrder;
import fr.unice.polytech.equipe.j.dto.*;
import fr.unice.polytech.equipe.j.restaurant.RestaurantDatabaseSeeder;
import fr.unice.polytech.equipe.j.user.UserDatabaseSeeder;
import fr.unice.polytech.equipe.j.utils.JacksonConfig;
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
import java.util.List;
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
    private ObjectMapper mapper = new ObjectMapper();
    private OrderDTO orderUser1;
    private OrderDTO orderUser2;
    private UUID usrId1;
    private java.net.http.HttpResponse<String> creationResponse;
    private HttpResponse<String> groupOrderRequestResponse;
    private UUID groupOrderId;
    private GroupOrderDTO groupOrderDTO;

    @Before
    public void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
    }

    @Given("[ValidateGroupOrder]the user {string} is registered")
    public void validategrouporderTheUserIsRegistered(String id) {
        this.usrId1 = UUID.fromString(id);
        this.creationResponse = request("http://localhost:5000/api/database/campusUsers", "/"+usrId1, HttpMethod.GET, "");
        assertFalse(creationResponse.body().isEmpty());
    }


    @Given("[ValidateGroupOrder]the user creates a group order with delivery location {string} and delivery time of {int}:{int} PM")
    public void validate_group_order_the_user_creates_a_group_order_with_delivery_location_and_delivery_time_of_pm(String string, Integer hour, Integer minute) throws JsonProcessingException {
        var url = "http://localhost:5008/api/group-order";
        DeliveryLocationDTO dld = new DeliveryLocationDTO(UUID.fromString("0b53ccf7-2c33-463d-814a-bfd525e8fa45"), "home", string);
        LocalDateTime deliveryTime = LocalDateTime.now(TimeUtils.getClock()).withHour(hour).withMinute(minute);;
        DeliveryDetailsDTO ddd = new DeliveryDetailsDTO(UUID.fromString("1e7fb210-5d64-4d0a-8f51-bca07b89cab0"), dld, deliveryTime);
        this.groupOrderRequestResponse = request(url, "/"+usrId1+"/create", HttpMethod.POST, JacksonConfig.configureObjectMapper().writeValueAsString(ddd));
        assertFalse(groupOrderRequestResponse.body().isEmpty());
        String id = mapper.readValue(groupOrderRequestResponse.body(), String.class);
        HttpResponse<String> groupOrderResponse = request(url, "/"+id, HttpMethod.GET, "");
        this.groupOrderDTO = JacksonConfig.configureObjectMapper().readValue(groupOrderResponse.body(),GroupOrderDTO.class);
        Assertions.assertEquals(hour, this.groupOrderDTO.getDeliveryDetails().getDeliveryTime().getHour());
        Assertions.assertEquals(minute, this.groupOrderDTO.getDeliveryDetails().getDeliveryTime().getMinute());
        assertEquals(1, groupOrderDTO.getUsers().size());

        HttpResponse<String> userResponse = request("http://localhost:5002/api/database/campusUsers", "/"+usrId1, HttpMethod.GET, "");
        CampusUserDTO userDTO = new ObjectMapper().readValue(userResponse.body(), CampusUserDTO.class);
        assertEquals(userDTO, this.groupOrderDTO.getUsers().getFirst());
    }

    @Given("[ValidateGroupOrder]the user receives a group order identifier")
    public void validate_group_order_the_user_receives_a_group_order_identifier() throws JsonProcessingException {
        String responseBody = groupOrderRequestResponse.body();
        UUID extractedUUID = mapper.readValue(responseBody, UUID.class);
        this.groupOrderId = extractedUUID;
        assertNotNull(this.groupOrderId);
        Assertions.assertEquals(this.groupOrderId, this.groupOrderDTO.getGroupOrderId());
    }

    @When("[ValidateGroupOrder]the user adds the following items to his order from the restaurant {string}:")
    public void validategrouporderTheUserAddsTheFollowingItemsToHisOrderFromTheRestaurant(String restaurantId, io.cucumber.datatable.DataTable dataTable) throws JsonProcessingException {
        var url = "http://localhost:5008/api/group-order";
        // Mapper la DataTable en une liste de MenuItemDTO
        List<MenuItemDTO> menuItems = dataTable.asList().stream()
                .map(itemName -> {MenuItemDTO item = new MenuItemDTO();
                    item.setId(UUID.randomUUID());
                    item.setName(itemName);
                    item.setPrepTime(10);
                    item.setPrice(12.5);
                    item.setType("MAIN");
                    return item;
                })
                .toList();
        OrderDTO order = new OrderDTO(UUID.fromString("0b53ccf7-2c33-463d-814a-bfd525e8fa45"), UUID.randomUUID(), usrId1, menuItems, "PENDING", null);
        HttpResponse<String> response = request(url, "/" + this.groupOrderId + "/addOrder", HttpMethod.POST, JacksonConfig.configureObjectMapper().writeValueAsString(order));
        assertEquals(201, response.statusCode());

        HttpResponse<String> groupOrderResponse = request(url, "/" + groupOrderId, HttpMethod.GET, "");
        this.groupOrderDTO = JacksonConfig.configureObjectMapper().readValue(groupOrderResponse.body(), GroupOrderDTO.class);
    }

    @When("[ValidateGroupOrder] the creator user {string} validates the group order")
    public void validategrouporderTheCreatorUserValidatesTheGroupOrder(String userId) throws JsonProcessingException {
        var url = "http://localhost:5008/api/group-order";
        UUID usrId = UUID.fromString(userId);
        HttpResponse<String> response = request(url, "/"+this.groupOrderId+"/"+usrId+"/validate", HttpMethod.PUT, "");
        assertEquals(200, response.statusCode());
        HttpResponse<String> groupOrderResponse = request(url, "/"+groupOrderId, HttpMethod.GET, "");
        this.groupOrderDTO = JacksonConfig.configureObjectMapper().readValue(groupOrderResponse.body(),GroupOrderDTO.class);
    }




    @Then("[ValidateGroupOrder] the group order is validated")
    public void validategrouporderTheGroupOrderIsValidated() {
        assertEquals(this.groupOrderDTO.getStatus(), OrderStatus.VALIDATED);
    }


    @Given("[ValidateGroupOrder]the user creates a group order with delivery location {string}")
    public void validate_group_order_the_user_creates_a_group_order_with_delivery_location(String string) throws JsonProcessingException {
        var url = "http://localhost:5008/api/group-order";
        DeliveryLocationDTO dld = new DeliveryLocationDTO(UUID.fromString("0b53ccf7-2c33-463d-814a-bfd525e8fa46"), "home", string);
        DeliveryDetailsDTO ddd = new DeliveryDetailsDTO(UUID.fromString("1e7fb210-5d64-4d0a-8f51-bca07b89cab0"), dld, null);
        this.groupOrderRequestResponse = request(url, "/"+usrId1+"/create", HttpMethod.POST, JacksonConfig.configureObjectMapper().writeValueAsString(ddd));
        assertFalse(groupOrderRequestResponse.body().isEmpty());
        String id = mapper.readValue(groupOrderRequestResponse.body(), String.class);
        HttpResponse<String> groupOrderResponse = request(url, "/"+id, HttpMethod.GET, "");
        this.groupOrderDTO = JacksonConfig.configureObjectMapper().readValue(groupOrderResponse.body(),GroupOrderDTO.class);
        assertEquals(1, groupOrderDTO.getUsers().size());

        HttpResponse<String> userResponse = request("http://localhost:5008/api/campusUsers", "/"+usrId1, HttpMethod.GET, "");
        CampusUserDTO userDTO = new ObjectMapper().readValue(userResponse.body(), CampusUserDTO.class);
        assertEquals(userDTO, groupOrder.getUsers().getFirst());

    }


}

