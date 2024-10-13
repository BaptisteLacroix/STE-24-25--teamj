package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;
import fr.unice.polytech.equipe.j.user.ConnectedUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailsStepDefs {
    private ConnectedUser user;
    private Restaurant restaurant;
    private RestaurantProxy restaurantProxy;

    @Given("[OrderDetails]the user is registered")
    public void order_details_the_user_is_registered() {
        user = new ConnectedUser("john@example.com", "password123", 100.0);
    }

    @When("[OrderDetails]the user selects the restaurant {string}")
    public void order_details_the_user_selects_the_restaurant(String string) {
        restaurant = new Restaurant(string, LocalDateTime.now(), LocalDateTime.now(), null);
        restaurantProxy = new RestaurantProxy();
    }

    @Then("[OrderDetails]the user start a single order by specifying the delivery location from the pre-recorded location")
    public void order_details_the_user_start_a_single_order_by_specifying_the_delivery_location_from_the_pre_recorded_location() {
        user.startIndividualOrder(restaurantProxy, restaurant.getRestaurantId());
    }

    @Then("[OrderDetails]choose and delivery date within the restaurant's preparation capabilities.")
    public void order_details_choose_and_delivery_date_within_the_restaurant_s_preparation_capabilities() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("[OrderDetails]the user tries to select {string} as delivery location")
    public void order_details_the_user_tries_to_select_as_delivery_location(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("[OrderDetails]the user receives an error message {string}")
    public void order_details_the_user_receives_an_error_message(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("[OrderDetails]the user is not able to proceed with the order")
    public void order_details_the_user_is_not_able_to_proceed_with_the_order() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
