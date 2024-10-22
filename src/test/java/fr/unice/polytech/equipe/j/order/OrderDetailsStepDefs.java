package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.user.CampusUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;

import java.sql.Time;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OrderDetailsStepDefs {
    private CampusUser user;
    private Restaurant restaurant;

    @Before
    public void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
    }

    @Given("[OrderDetails]the user is registered")
    public void order_details_the_user_is_registered() {
        user = new CampusUser("john@example.com", "password123", new OrderManager());
    }

    @When("[OrderDetails]the user selects the restaurant {string}")
    public void order_details_the_user_selects_the_restaurant(String string) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
    }

    @Then("[OrderDetails]the user start a single order by specifying the delivery location from the pre-recorded locations")
    public void order_details_the_user_start_a_single_order_by_specifying_the_delivery_location_from_the_pre_recorded_location() {
        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, null);
        IndividualOrder individualOrder = new IndividualOrder(restaurant, deliveryDetails, user);
        user.setCurrentOrder(individualOrder);
        assertEquals(((IndividualOrder) user.getCurrentOrder()).getDeliveryDetails().getDeliveryLocation().locationName(), deliveryLocation.locationName());
        assertEquals(((IndividualOrder) user.getCurrentOrder()).getDeliveryDetails().getDeliveryLocation().address(), deliveryLocation.address());
    }

    @Then("[OrderDetails]the user choose a delivery date within the restaurant's preparation capabilities. {int} minutes before closing time")
    public void order_details_the_user_choose_a_delivery_date_within_the_restaurant_s_preparation_capabilities_minutes_before_closing_time(Integer int1) {
        LocalDateTime localDateTime = user.getCurrentOrder().getRestaurant().getClosingTime().get();
        ((IndividualOrder) user.getCurrentOrder()).getDeliveryDetails().setDeliveryTime(localDateTime.minusMinutes(int1));
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
