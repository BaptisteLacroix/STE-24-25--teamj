package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.user.CampusUser;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OrderDetailsStepDefs {
    private CampusUser user;
    private IRestaurant restaurant;
    private IndividualOrder individualOrder;

    @Before
    public void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
    }

    @Given("[OrderDetails]the user is registered")
    public void order_details_the_user_is_registered() {
        user = new CampusUser("John", 0);
    }

    @When("[OrderDetails]the user selects the restaurant {string}")
    public void order_details_the_user_selects_the_restaurant(String string) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
        new OrderManager(restaurant);
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
        assertNull(individualOrder);
    }

    @Then("[OrderDetails]the user start a single order by specifying the delivery location from the pre-recorded locations and specifying the delivery date that is {int} minutes before closing time")
    public void orderdetailsTheUserStartASingleOrderBySpecifyingTheDeliveryLocationFromThePreRecordedLocationsAndSpecifyingTheDeliveryDateThatIsMinutesBeforeClosingTime(int arg0) {
        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, restaurant.getClosingTime().orElseThrow().minusMinutes(arg0));
        individualOrder = new IndividualOrder(restaurant, deliveryDetails, user);
        assertEquals(deliveryLocation.locationName(), individualOrder.getDeliveryDetails().getDeliveryLocation().locationName());
        assertEquals(deliveryLocation.address(), individualOrder.getDeliveryDetails().getDeliveryLocation().address());
    }
}
