package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.user.ConnectedUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class GroupOrderStepDefs {
    private ConnectedUser user;

    @Given("[GroupOrder]the user is registered")
    public void the_user_is_registered() {
        user = new ConnectedUser("john@example.com", "password123", 100.0, new OrderManager());
    }

    @When("[GroupOrder]the user creates a group order with delivery location {string}")
    public void the_user_creates_a_group_order_with_delivery_location(String deliveryLocation) {
        DeliveryLocation location = DeliveryLocationManager.getInstance().findLocationByName(deliveryLocation);
        DeliveryDetails deliveryDetails = new DeliveryDetails(location, null);
        user.startGroupOrder(deliveryDetails);
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
        LocalDateTime expectedTime = LocalDateTime.now()
                .withHour(hour)
                .withMinute(minute);
        assertEquals(expectedTime.getHour(), user.getCurrentGroupOrder().getDeliveryDetails().getDeliveryTime().get().getHour());
        assertEquals(expectedTime.getMinute(), user.getCurrentGroupOrder().getDeliveryDetails().getDeliveryTime().get().getMinute());
    }

    @When("[GroupOrder]the user tries to create a group order without specifying a delivery location")
    public void group_order_the_user_tries_to_create_a_group_order_without_specifying_a_delivery_location() {
        assertThrows(IllegalArgumentException.class, () -> user.startGroupOrder(new DeliveryDetails(null, null)));
    }

    @Then("[GroupOrder]the user receives an error message {string}")
    public void group_order_the_user_receives_an_error_message(String string) {
        // The exception message is not checked in the test
    }

    @Then("[GroupOrder]the group order is not created")
    public void group_order_the_group_order_is_not_created() {
        assertNull(user.getCurrentGroupOrder());
    }

    @When("[GroupOrder]the user creates a group order with delivery location {string} and delivery time of {int}:{int} PM")
    public void group_order_the_user_creates_a_group_order_with_delivery_location_and_delivery_time_of_pm(String string, Integer int1, Integer int2) {
        LocalDateTime deliveryTime = LocalDateTime.now()
                .withHour(int1)
                .withMinute(int2);
        DeliveryLocation location = DeliveryLocationManager.getInstance().findLocationByName(string);
        DeliveryDetails deliveryDetails = new DeliveryDetails(location, deliveryTime);
        user.startGroupOrder(deliveryDetails);
        assertNotNull(user.getCurrentGroupOrder());
    }

    @When("[GroupOrder]the user tries to change the delivery time to {int}:{int} PM")
    public void group_order_the_user_tries_to_change_the_delivery_location_to(Integer hour, Integer minute) {
        LocalDateTime deliveryTime = LocalDateTime.now()
                .withHour(hour)
                .withMinute(minute);
        assertThrows(UnsupportedOperationException.class, () -> user.changeGroupDeliveryTime(deliveryTime));
    }

    @When("[GroupOrder]the user tries to specify a delivery time in the past of {int}:{int} PM")
    public void group_order_the_user_tries_to_specify_a_delivery_time_in_the_past_of_pm(Integer int1, Integer int2) {
        LocalDateTime deliveryTime = LocalDateTime.now()
                .withHour(int1)
                .withMinute(int2);
        assertThrows(UnsupportedOperationException.class, () -> user.changeGroupDeliveryTime(deliveryTime));
    }
}
