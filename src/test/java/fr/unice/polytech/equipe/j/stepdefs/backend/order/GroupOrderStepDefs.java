package fr.unice.polytech.equipe.j.stepdefs.backend.order;

import fr.unice.polytech.equipe.j.order.DeliveryDetails;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;
import fr.unice.polytech.equipe.j.user.ConnectedUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class GroupOrderStepDefs {
    private ConnectedUser user;
    private RestaurantProxy restaurantProxy;

    public void setUp() {
        Restaurant restaurant = new Restaurant("McDonald's",null, LocalDateTime.of(2021, 1, 1, 8, 0), LocalDateTime.of(2021, 1, 1, 22, 0), null);
        restaurantProxy = new RestaurantProxy(List.of(restaurant));
    }

    @Given("[GroupOrder]the user is registered")
    public void the_user_is_registered() {
        this.setUp();
        user = new ConnectedUser("john@example.com", "password123", 100.0);
    }

    @When("[GroupOrder]the user creates a group order with delivery location {string}")
    public void the_user_creates_a_group_order_with_delivery_location(String deliveryLocation) {
        user.startGroupOrder(restaurantProxy, new DeliveryDetails(deliveryLocation, null));
        assertNotNull(user.getGroupOrderUUID());
    }

    @Then("[GroupOrder]the user receives a group order identifier")
    public void the_user_receives_a_group_order_identifier() {
        assertNotNull(restaurantProxy.getGroupOrder(user.getGroupOrderUUID()));
        assertNotNull(restaurantProxy.getGroupOrder(user.getGroupOrderUUID()).getGroupOrderId());
    }

    @Then("[GroupOrder]the group order delivery location is {string}")
    public void the_group_order_delivery_location_is(String expectedLocation) {
        assertEquals(expectedLocation, restaurantProxy.getGroupOrder(user.getGroupOrderUUID()).getDeliveryDetails().getDeliveryLocation());
    }

    @Then("[GroupOrder]the group order delivery time is {int}:{int} PM")
    public void the_group_order_delivery_time_is(Integer hour, Integer minute) {
        LocalDateTime expectedTime = LocalDateTime.now()
                .withHour(hour)
                .withMinute(minute);
        assertEquals(expectedTime.getHour(), restaurantProxy.getGroupOrder(user.getGroupOrderUUID()).getDeliveryDetails().getDeliveryTime().get().getHour());
        assertEquals(expectedTime.getMinute(), restaurantProxy.getGroupOrder(user.getGroupOrderUUID()).getDeliveryDetails().getDeliveryTime().get().getMinute());
    }

    @When("[GroupOrder]the user tries to create a group order without specifying a delivery location")
    public void group_order_the_user_tries_to_create_a_group_order_without_specifying_a_delivery_location() {
        assertThrows(IllegalArgumentException.class, () -> user.startGroupOrder(restaurantProxy, new DeliveryDetails(null, null)));
    }

    @Then("[GroupOrder]the user receives an error message {string}")
    public void group_order_the_user_receives_an_error_message(String string) {
        // The exception message is not checked in the test
    }

    @Then("[GroupOrder]the group order is not created")
    public void group_order_the_group_order_is_not_created() {
        assertNull(user.getGroupOrderUUID());
    }

    @When("[GroupOrder]the user creates a group order with delivery location {string} and delivery time of {int}:{int} PM")
    public void group_order_the_user_creates_a_group_order_with_delivery_location_and_delivery_time_of_pm(String string, Integer int1, Integer int2) {
        LocalDateTime deliveryTime = LocalDateTime.now()
                .withHour(int1)
                .withMinute(int2);
        user.startGroupOrder(restaurantProxy, new DeliveryDetails(string, deliveryTime));
        assertNotNull(user.getGroupOrderUUID());
    }

    @When("[GroupOrder]the user tries to change the delivery time to {int}:{int} PM")
    public void group_order_the_user_tries_to_change_the_delivery_location_to(Integer hour, Integer minute) {
        LocalDateTime deliveryTime = LocalDateTime.now()
                .withHour(hour)
                .withMinute(minute);
        assertThrows(UnsupportedOperationException.class, () -> user.changeGroupDeliveryTime(restaurantProxy, deliveryTime));
    }

    @When("[GroupOrder]the user tries to specify a delivery time in the past of {int}:{int} PM")
    public void group_order_the_user_tries_to_specify_a_delivery_time_in_the_past_of_pm(Integer int1, Integer int2) {
        LocalDateTime deliveryTime = LocalDateTime.now()
                .withHour(int1)
                .withMinute(int2);
        assertThrows(UnsupportedOperationException.class, () -> user.changeGroupDeliveryTime(restaurantProxy, deliveryTime));
    }
}
