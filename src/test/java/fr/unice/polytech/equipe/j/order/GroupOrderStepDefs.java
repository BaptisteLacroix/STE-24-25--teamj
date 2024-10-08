package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.user.ConnectedUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GroupOrderStepDefs {
    private ConnectedUser user;
    private GroupOrder groupOrder;

    @Given("the user is registered")
    public void the_user_is_registered() {
        user = new ConnectedUser("john@example.com", "password123", 100.0);
    }

    @Given("the user has selected the restaurant {string}")
    public void the_user_has_selected_the_restaurant(String restaurantName) {
        new Restaurant(restaurantName, null, null, null);
    }

    @When("the user creates a group order with delivery location {string}")
    public void the_user_creates_a_group_order_with_delivery_location(String deliveryLocation) {
        groupOrder = user.createGroupOrder(deliveryLocation, null);
        assertNotNull(user.getGroupOrder());
    }

    @When("the user specifies a delivery time of {int}:{int} PM")
    public void the_user_specifies_a_delivery_time_of_pm(Integer hour, Integer minute) {
        LocalDateTime deliveryTime = LocalDateTime.now()
                .withHour(hour)
                .withMinute(minute);
        user.getGroupOrder().setDeliveryTime(deliveryTime);
    }

    @Then("the user receives a group order identifier")
    public void the_user_receives_a_group_order_identifier() {
        assertNotNull(groupOrder);
        assertNotNull(groupOrder.getGroupOrderId());
    }

    @Then("the group order delivery location is {string}")
    public void the_group_order_delivery_location_is(String expectedLocation) {
        assertEquals(expectedLocation, groupOrder.getDeliveryLocation());
    }

    @Then("the group order delivery time is {int}:{int} PM")
    public void the_group_order_delivery_time_is(Integer hour, Integer minute) {
        LocalDateTime expectedTime = LocalDateTime.now()
                .withHour(hour)
                .withMinute(minute);
        assertEquals(expectedTime.getHour(), groupOrder.getDeliveryTime().getHour());
        assertEquals(expectedTime.getMinute(), groupOrder.getDeliveryTime().getMinute());
    }
}
