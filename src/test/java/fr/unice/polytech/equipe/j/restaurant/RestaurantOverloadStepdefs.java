package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.slot.Slot;
import fr.unice.polytech.equipe.j.user.CampusUser;
import fr.unice.polytech.equipe.j.user.RestaurantManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RestaurantOverloadStepdefs {
    private RestaurantManager restaurantManager;
    private CampusUser user;
    private Clock clock;
    private Restaurant restaurant;
    private final Menu menu = new Menu(new ArrayList<>());
    private Order order;
    private OrderManager orderManager;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Before
    public void setUp() {
        clock = Clock.fixed(Instant.parse("2024-10-18T14:00:00Z"), ZoneId.of("Europe/Paris"));
    }

    @Given("the restaurant is open from {string} to {string}")
    public void theRestaurantHasSlots(String start, String end, DataTable dataTable) {
    }


    @And("the current time is {string}")
    public void theCurrentTimeIs(String time) {
    }

    @When("the user places an order for a {string} with a preparation time of {int} seconds")
    public void theUserPlacesAnOrderForAWithAPreparationTimeOfMinutes(String itemName, int prepTime) {

    }

    @Then("the system checks for available slots within the next {int} slots")
    public void theSystemChecksForAvailableSlotsWithinTheNextSlots(int numberOfSlots) {
    }

    @And("the order should be accepted because preparation can be completed by {string}")
    public void theOrderShouldBeAcceptedBecausePreparationCanBeCompletedBy(String arg0) {
        assertTrue("The order should be accepted.", restaurant.isOrderValid(order));
    }
}
