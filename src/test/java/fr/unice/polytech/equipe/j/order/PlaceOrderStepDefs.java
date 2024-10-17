package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.user.ConnectedUser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PlaceOrderStepDefs {

    private Restaurant restaurant;
    private ConnectedUser connectedUser;

    @Given("the restaurant service manager configured the following restaurants:")
    public void the_restaurant_service_manager_configured_the_following_restaurants(io.cucumber.datatable.DataTable dataTable) {
        // Initialized in the @Before method
    }

    /**
     * This step definition is used to simulate a user registration.
     */
    @Given("the user is registered")
    public void the_user_is_registered() {
        connectedUser = new ConnectedUser("john@example.com", "password", 100.0, new OrderManager());
    }

    /**
     * This step definition is used to simulate the user selecting a restaurant.
     *
     * @param restaurantName the name of the restaurant
     */
    @Given("the user has selected the restaurant {string}")
    public void the_user_has_selected_the_restaurant(String restaurantName) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(restaurantName).getFirst();
    }

    @And("the user start and order by specifying the delivery location from the pre-recorded locations")
    public void the_user_start_and_order_by_specifying_the_delivery_location_from_the_pre_recorded_locations() {
        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, null);
        connectedUser.startIndividualOrder(restaurant, deliveryDetails);
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

        connectedUser.addItemToOrder(restaurant, menuItem1);
        connectedUser.addItemToOrder(restaurant, menuItem2);

        assertEquals(2, connectedUser.getCurrentOrder().getItems().size());
    }

    /**
     * This step definition is used to simulate the user adding item to their order.
     *
     * @param item1 the first item
     */
    @When("the user adds {string} to their order")
    public void the_user_adds_to_their_order(String item1) {
        MenuItem menuItem1 = restaurant.getMenu().findItemByName(item1);

        connectedUser.addItemToOrder(restaurant, menuItem1);

        assertEquals(1, connectedUser.getCurrentOrder().getItems().size());
    }

    /**
     * This step definition is used to simulate the user placing the order.
     */
    @When("places the order")
    public void places_the_order() {
        connectedUser.validateIndividualOrder(restaurant);
    }

    /**
     * This step definition is used to verify that the order is placed successfully.
     */
    @Then("the order is placed successfully")
    public void the_order_is_placed_successfully() {
        assertEquals(1, connectedUser.getOrdersHistory().size());
        assertEquals(OrderStatus.VALIDATED, connectedUser.getCurrentOrder().getStatus());
    }

    @When("the user tries to add {string} to their order")
    public void the_user_tries_to_add_to_their_order(String string) {
        assertThrows(IllegalArgumentException.class, () -> connectedUser.addItemToOrder(restaurant, new MenuItem(string, 5, 0.0)));
    }

    @Then("the user gets an error message {string}")
    public void the_user_get_an_error_message(String string) {
        System.out.println(string);
    }

    @Then("the order is not placed")
    public void the_order_is_not_placed() {
        assertTrue(connectedUser.getOrdersHistory().isEmpty());
        assertEquals(OrderStatus.PENDING, connectedUser.getCurrentOrder().getStatus());
    }

    @Then("the item is not added to the order")
    public void the_item_is_not_added_to_the_order() {
        assertEquals(0, connectedUser.getCurrentOrder().getItems().size());
    }

    @When("the user tries to place the order without adding any menu items")
    public void the_user_tries_to_place_the_order_without_adding_any_menu_items() {
        assertThrows(IllegalArgumentException.class, () -> connectedUser.validateIndividualOrder(restaurant));
    }
}
