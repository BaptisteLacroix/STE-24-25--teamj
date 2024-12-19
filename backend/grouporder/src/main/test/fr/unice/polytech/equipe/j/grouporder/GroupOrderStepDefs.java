package fr.unice.polytech.equipe.j.grouporder;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.backend.GroupOrderProxy;
import fr.unice.polytech.equipe.j.backend.IGroupOrder;
import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.dto.DeliveryLocationDTO;
import fr.unice.polytech.equipe.j.dto.GroupOrderDTO;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.user.CampusUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GroupOrderStepDefs {
    private CampusUserDTO user;
    private IGroupOrder groupOrderProxy;

    @Before
    public void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
    }

    @Given("[GroupOrder]the user is registered")
    public void the_user_is_registered() {
        user = new CampusUserDTO("John", 0);
    }

    @When("[GroupOrder]the user creates a group order with delivery location {string}")
    public void the_user_creates_a_group_order_with_delivery_location(String deliveryLocation) {
        DeliveryLocationDTO location = DeliveryLocationManager.getInstance().findLocationByName(deliveryLocation);
        DeliveryDetailsDTO deliveryDetails = new DeliveryDetails(location, null);
        groupOrderProxy = new GroupOrderProxy(new GroupOrderDTO(deliveryDetails));
    }

    @Then("[GroupOrder]the user receives a group order identifier")
    public void the_user_receives_a_group_order_identifier() {
        assertNotNull(groupOrderProxy.getGroupOrderId());
    }

    @Then("[GroupOrder]the group order delivery location is {string}")
    public void the_group_order_delivery_location_is(String expectedLocation) {
        assertEquals(expectedLocation, groupOrderProxy.getDeliveryDetails().getDeliveryLocation().locationName());
    }

    @Then("[GroupOrder]the group order delivery time is {int}:{int} PM")
    public void the_group_order_delivery_time_is(Integer hour, Integer minute) {
        LocalDateTime expectedTime = TimeUtils.getNow()
                .withHour(hour)
                .withMinute(minute);
        assertEquals(expectedTime.getHour(), groupOrderProxy.getDeliveryDetails().getDeliveryTime().get().getHour());
        assertEquals(expectedTime.getMinute(), groupOrderProxy.getDeliveryDetails().getDeliveryTime().get().getMinute());
    }

    @When("[GroupOrder]the user tries to create a group order without specifying a delivery location")
    public void group_order_the_user_tries_to_create_a_group_order_without_specifying_a_delivery_location() {
        assertThrows(IllegalArgumentException.class, () -> new GroupOrderProxy(new GroupOrder(new DeliveryDetails(null, null))));
    }

    @Then("[GroupOrder]the user receives an error message {string}")
    public void group_order_the_user_receives_an_error_message(String string) {
        System.out.println(string);
    }

    @Then("[GroupOrder]the group order is not created")
    public void group_order_the_group_order_is_not_created() {
        assertNull(groupOrderProxy);
    }

    @When("[GroupOrder]the user creates a group order with delivery location {string} and delivery time of {int}:{int} PM")
    public void group_order_the_user_creates_a_group_order_with_delivery_location_and_delivery_time_of_pm(String string, Integer int1, Integer int2) {
        LocalDateTime deliveryTime = TimeUtils.getNow()
                .withHour(int1)
                .withMinute(int2);
        DeliveryLocation location = DeliveryLocationManager.getInstance().findLocationByName(string);
        DeliveryDetails deliveryDetails = new DeliveryDetails(location, deliveryTime);
        groupOrderProxy = new GroupOrderProxy(new GroupOrder(deliveryDetails));
        assertTrue(groupOrderProxy.getDeliveryDetails().getDeliveryTime().isPresent());
        assertEquals(int1.intValue(), groupOrderProxy.getDeliveryDetails().getDeliveryTime().get().getHour());
        assertEquals(int2.intValue(), groupOrderProxy.getDeliveryDetails().getDeliveryTime().get().getMinute());
    }
}
