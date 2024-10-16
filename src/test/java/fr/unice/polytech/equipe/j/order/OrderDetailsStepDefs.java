package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.user.ConnectedUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OrderDetailsStepDefs {
    private ConnectedUser user;
    private Restaurant restaurant;

    @Given("[OrderDetails]the user is registered")
    public void order_details_the_user_is_registered() {
        user = new ConnectedUser("john@example.com", "password123", 100.0, new OrderManager());
    }

    @When("[OrderDetails]the user selects the restaurant {string}")
    public void order_details_the_user_selects_the_restaurant(String string) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
    }

    @Then("[OrderDetails]the user start a single order by specifying the delivery location from the pre-recorded locations")
    public void order_details_the_user_start_a_single_order_by_specifying_the_delivery_location_from_the_pre_recorded_location() {
        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, null);
        user.startIndividualOrder(restaurant, deliveryDetails);
        assertEquals(((IndividualOrder) user.getCurrentOrder()).getDeliveryDetails().getDeliveryLocation().locationName(), deliveryLocation.locationName());
        assertEquals(((IndividualOrder) user.getCurrentOrder()).getDeliveryDetails().getDeliveryLocation().address(), deliveryLocation.address());
    }

    @Then("[OrderDetails]the user choose a delivery date within the restaurant's preparation capabilities")
    public void order_details_choose_and_delivery_date_within_the_restaurant_s_preparation_capabilities() {
        throw new io.cucumber.java.PendingException("Wait the restaurant manager feature merge");
    }

    @Then("[OrderDetails]the user tries to select {string} as the delivery location")
    public void order_details_the_user_tries_to_select_as_delivery_location(String string) {
        DeliveryLocation deliveryLocation = new DeliveryLocation(string, "123 " + string + " Street");
        assertThrows(IllegalArgumentException.class, () -> new DeliveryDetails(deliveryLocation, null));
    }

    @Then("[OrderDetails]the user receives an error message {string}")
    public void order_details_the_user_receives_an_error_message(String string) {
        System.out.println(string);
    }

    @Then("[OrderDetails]the user is not able to proceed with the order")
    public void order_details_the_user_is_not_able_to_proceed_with_the_order() {
        assertNull(user.getCurrentOrder());
    }
}
