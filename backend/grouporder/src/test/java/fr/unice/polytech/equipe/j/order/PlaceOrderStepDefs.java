package java.fr.unice.polytech.equipe.j.order;

import java.fr.unice.polytech.equipe.j.TimeUtils;
import java.fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import java.fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import java.fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.restaurant.backend.IRestaurant;
import java.fr.unice.polytech.equipe.j.restaurant.menu.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.backend.RestaurantServiceManager;
import java.fr.unice.polytech.equipe.j.user.CampusUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PlaceOrderStepDefs {

    private IRestaurant restaurant;
    private CampusUser campusUser;
    private IndividualOrder individualOrder;
    private OrderManager orderManager;

    @Before
    public void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
    }

    @Given("the restaurant service manager configured the following restaurants:")
    public void the_restaurant_service_manager_configured_the_following_restaurants(io.cucumber.datatable.DataTable dataTable) {
        // Initialized in the @Before method
    }

    /**
     * This step definition is used to simulate a user registration.
     */
    @Given("the user is registered")
    public void the_user_is_registered() {
        campusUser = new CampusUser("John", 100.0);
    }

    /**
     * This step definition is used to simulate the user selecting a restaurant.
     *
     * @param restaurantName the name of the restaurant
     */
    @Given("the user has selected the restaurant {string}")
    public void the_user_has_selected_the_restaurant(String restaurantName) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(restaurantName).getFirst();
        orderManager = new OrderManager(restaurant);
    }

    @Given("the user start and order by specifying the delivery location from the pre-recorded locations and specifying the delivery time as {int}:{int}")
    public void the_user_start_and_order_by_specifying_the_delivery_location_from_the_pre_recorded_locations_and_specifying_the_delivery_time_as(Integer int1, Integer int2) {
        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        LocalDateTime deliveryTime = LocalDateTime.now().withHour(int1).withMinute(int2);
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, deliveryTime);
        this.individualOrder = new IndividualOrder(restaurant, deliveryDetails, campusUser);
    }

    /**
     * This step definition is used to simulate the user adding items to their order.
     *
     * @param item1 the first item
     * @param item2 the second item
     */
    @When("the user adds {string} and {string} to their order")
    public void the_user_adds_and_to_their_order(String item1, String item2) {
        MenuItem menuItem1 = restaurant.getMenu().findItemByName(item1);
        MenuItem menuItem2 = restaurant.getMenu().findItemByName(item2);

        orderManager.addItemToOrder(individualOrder, menuItem1);
        orderManager.addItemToOrder(individualOrder, menuItem2);

        assertEquals(2, individualOrder.getItems().size());
    }

    /**
     * This step definition is used to simulate the user adding item to their order.
     *
     * @param item1 the first item
     */
    @When("the user adds {string} to their order")
    public void the_user_adds_to_their_order(String item1) {
        MenuItem menuItem1 = restaurant.getMenu().findItemByName(item1);
        orderManager.addItemToOrder(individualOrder, menuItem1);
        assertEquals(1, individualOrder.getItems().size());
    }

    /**
     * This step definition is used to simulate the user placing the order.
     */
    @When("places the order")
    public void places_the_order() {
        orderManager.validateOrder(individualOrder);
    }

    /**
     * This step definition is used to verify that the order is placed successfully.
     */
    @Then("the order is placed successfully")
    public void the_order_is_placed_successfully() {
        assertEquals(OrderStatus.VALIDATED, individualOrder.getStatus());
    }

    @When("the user tries to add {string} to their order")
    public void the_user_tries_to_add_to_their_order(String string) {
        assertThrows(IllegalArgumentException.class, () -> orderManager.addItemToOrder(individualOrder, restaurant.getMenu().findItemByName(string)));
    }

    @Then("the user gets an error message {string}")
    public void the_user_get_an_error_message(String string) {
        System.out.println(string);
    }

    @Then("the order is not placed")
    public void the_order_is_not_placed() {
        assertEquals(OrderStatus.PENDING, individualOrder.getStatus());
    }

    @Then("the item is not added to the order")
    public void the_item_is_not_added_to_the_order() {
        assertTrue(individualOrder.getItems().isEmpty());
    }

    @When("the user tries to place the order without adding any menu items")
    public void the_user_tries_to_place_the_order_without_adding_any_menu_items() {
        assertThrows(IllegalArgumentException.class, () -> orderManager.validateOrder(individualOrder));
    }
}
