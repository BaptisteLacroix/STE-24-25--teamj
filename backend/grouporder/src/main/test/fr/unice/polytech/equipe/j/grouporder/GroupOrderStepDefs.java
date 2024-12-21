package fr.unice.polytech.equipe.j.grouporder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.dto.GroupOrderDTO;
import fr.unice.polytech.equipe.j.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.dto.DeliveryLocationDTO;
import fr.unice.polytech.equipe.j.utils.TimeUtils;
import fr.unice.polytech.equipe.j.utils.JacksonConfig;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;




import java.net.http.HttpResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static fr.unice.polytech.equipe.j.utils.RequestUtil.request;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupOrderStepDefs {
    private UUID usrId1;
    private java.net.http.HttpResponse<String> creationResponse;
    private UUID groupOrderId;
    private GroupOrderDTO groupOrderDTO;
    private ObjectMapper mapper = new ObjectMapper();
    private HttpResponse<String> groupOrderRequestResponse;
    private LocalDateTime deliveryTime;

    @Before
    public void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
    }

    @Given("the restaurant service manager configured the following restaurants:")
    public void theRestaurantServiceManagerConfiguredTheFollowingRestaurants(DataTable dataTable) {
       // Tout est configuré dans la DB
    }

    @Given("[GroupOrder]the user {string} is registered")
    public void the_user_is_registered(String id) {
        this.usrId1 = UUID.fromString(id);
        this.creationResponse = request("http://localhost:5000/api/database/campusUsers", "/"+usrId1, HttpMethod.GET, "");
        assertFalse(creationResponse.body().isEmpty());
    }

    @When("[GroupOrder]the user creates a group order with delivery location {string}")
    public void the_user_creates_a_group_order_with_delivery_location(String deliveryLocation) throws JsonProcessingException {
        var url = "http://localhost:5008/api/group-order";
        DeliveryLocationDTO dld = new DeliveryLocationDTO(UUID.fromString("5fbac108-4198-4ee9-adae-1400499736d3"), "home", deliveryLocation);
        DeliveryDetailsDTO ddd = new DeliveryDetailsDTO(UUID.fromString("c5cbe3d1-bb5c-4be5-a9c0-0ba9f6264218"), dld, null);
        this.groupOrderRequestResponse =  request(url, "/"+usrId1+"/create", HttpMethod.POST,this.mapper.writeValueAsString(ddd));
        assertFalse(groupOrderRequestResponse.body().isEmpty());
        String id = mapper.readValue(groupOrderRequestResponse.body(), String.class);
        HttpResponse<String> groupOrderResponse2 = request(url, "/"+id, HttpMethod.GET, "");
        this.groupOrderDTO = mapper.readValue(groupOrderResponse2.body(), GroupOrderDTO.class);
        assertEquals(deliveryLocation, groupOrderDTO.getDeliveryDetails().getDeliveryLocation().getAddress());
    }

    @Then("[GroupOrder]the user receives a group order identifier")
    public void the_user_receives_a_group_order_identifier() throws JsonProcessingException {
        String responseBody = groupOrderRequestResponse.body();
        UUID extractedUUID = mapper.readValue(responseBody, UUID.class);
        this.groupOrderId = extractedUUID;
        assertNotNull(this.groupOrderId);
        assertEquals(this.groupOrderId, this.groupOrderDTO.getGroupOrderId());
    }



    @Then("[GroupOrder]the group order delivery location is {string}")
    public void the_group_order_delivery_location_is(String expectedLocation) {
        assertEquals(this.groupOrderDTO.getDeliveryDetails().getDeliveryLocation().getAddress(), expectedLocation);
    }

    @When("[GroupOrder]the user creates a group order with delivery time {int} : {int}")
    public void grouporderTheUserCreatesAGroupOrderWithDeliveryTime(int hour, int minute) throws JsonProcessingException {
        var url = "http://localhost:5008/api/group-order";
        DeliveryLocationDTO dld = new DeliveryLocationDTO(UUID.fromString("0b53ccf7-2c33-463d-814a-bfd525e8fa45"), "home", "1600 Amphitheatre Parkway, Mountain View, CA 94043, USA");
        this.deliveryTime = LocalDateTime.now(TimeUtils.getClock()).withHour(hour).withMinute(minute);;
        DeliveryDetailsDTO ddd = new DeliveryDetailsDTO(UUID.fromString("1e7fb210-5d64-4d0a-8f51-bca07b89cab0"), dld, deliveryTime);
        this.groupOrderRequestResponse = request(url, "/"+usrId1+"/create", HttpMethod.POST, JacksonConfig.configureObjectMapper().writeValueAsString(ddd));
        assertFalse(groupOrderRequestResponse.body().isEmpty());
        String id = mapper.readValue(groupOrderRequestResponse.body(), String.class);
        HttpResponse<String> groupOrderResponse = request(url, "/"+id, HttpMethod.GET, "");
        this.groupOrderDTO = JacksonConfig.configureObjectMapper().readValue(groupOrderResponse.body(),GroupOrderDTO.class);
        assertEquals(hour, this.groupOrderDTO.getDeliveryDetails().getDeliveryTime().getHour());
        assertEquals(minute, this.groupOrderDTO.getDeliveryDetails().getDeliveryTime().getMinute());

    }

    @And("[GroupOrder]the group order delivery time is {int}:{int}")
    public void grouporderTheGroupOrderDeliveryTimeIs(int hour, int minute) {
        LocalDateTime expectedTime = LocalDateTime.now(TimeUtils.getClock())
                .withHour(hour)
                .withMinute(minute);
        assertEquals(this.groupOrderDTO.getDeliveryDetails().getDeliveryTime(), expectedTime);
    }
}
