package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
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

public class GroupOrderStepDefs {
    private CampusUser user;
    private Clock clock;

    @Before
    public void setUp() {
        clock = Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris"));
    }

    @Given("[GroupOrder]the user is registered")
    public void the_user_is_registered() {
        user = new CampusUser("john@example.com", "password123", 100.0, new OrderManager(clock));
    }

    @When("[GroupOrder]the user creates a group order with delivery location {string}")
    public void the_user_creates_a_group_order_with_delivery_location(String deliveryLocation) {
        DeliveryLocation location = DeliveryLocationManager.getInstance().findLocationByName(deliveryLocation);
        DeliveryDetails deliveryDetails = new DeliveryDetails(location, null);
        user.createGroupOrder(deliveryDetails);
    }

    @Then("[GroupOrder]the user receives a group order identifier")
    public void the_user_receives_a_group_order_identifier() {
        assertNotNull(user.getCurrentGroupOrder());
    }

    @Then("[GroupOrder]the group order delivery location is {string}")
    public void the_group_order_delivery_location_is(String expectedLocation) {
        assertEquals(expectedLocation, user.getCurrentGroupOrder().getDeliveryDetails().getDeliveryLocation().locationName());
    }

    @Then("[GroupOrder]the group order delivery time is {int}:{int} PM")
    public void the_group_order_delivery_time_is(Integer hour, Integer minute) {
        LocalDateTime expectedTime = LocalDateTime.now(clock)
                .withHour(hour)
                .withMinute(minute);
        assertEquals(expectedTime.getHour(), user.getCurrentGroupOrder().getDeliveryDetails().getDeliveryTime().get().getHour());
        assertEquals(expectedTime.getMinute(), user.getCurrentGroupOrder().getDeliveryDetails().getDeliveryTime().get().getMinute());
    }

    @When("[GroupOrder]the user tries to create a group order without specifying a delivery location")
    public void group_order_the_user_tries_to_create_a_group_order_without_specifying_a_delivery_location() {
        assertThrows(IllegalArgumentException.class, () -> user.createGroupOrder(new DeliveryDetails(null, null)));
    }

    @Then("[GroupOrder]the user receives an error message {string}")
    public void group_order_the_user_receives_an_error_message(String string) {
        System.out.println(string);
    }

    @Then("[GroupOrder]the group order is not created")
    public void group_order_the_group_order_is_not_created() {
        assertNull(user.getCurrentGroupOrder());
    }

    @When("[GroupOrder]the user creates a group order with delivery location {string} and delivery time of {int}:{int} PM")
    public void group_order_the_user_creates_a_group_order_with_delivery_location_and_delivery_time_of_pm(String string, Integer int1, Integer int2) {
        LocalDateTime deliveryTime = LocalDateTime.now(clock)
                .withHour(int1)
                .withMinute(int2);
        DeliveryLocation location = DeliveryLocationManager.getInstance().findLocationByName(string);
        DeliveryDetails deliveryDetails = new DeliveryDetails(location, deliveryTime);
        user.createGroupOrder(deliveryDetails);
        assertNotNull(user.getCurrentGroupOrder());
    }
}
