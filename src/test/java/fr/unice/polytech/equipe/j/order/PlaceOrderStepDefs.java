package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.Menu;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantFacade;
import fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.user.ConnectedUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PlaceOrderStepDefs {

    private RestaurantFacade restaurant;
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
        connectedUser = new ConnectedUser("john@example.com", "password", 100.0);
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

        connectedUser.startIndividualOrder(restaurant.getRestaurantId());
        connectedUser.addItemToOrder(restaurant.getRestaurantId(), menuItem1);
        connectedUser.addItemToOrder(restaurant.getRestaurantId(), menuItem2);
    }

    /**
     * This step definition is used to simulate the user adding item to their order.
     *
     * @param item1 the first item
     */
    @When("the user adds {string} to their order")
    public void the_user_adds_to_their_order(String item1) {
        MenuItem menuItem1 = restaurant.getMenu().findItemByName(item1);

        connectedUser.startIndividualOrder(restaurant.getRestaurantId());
        connectedUser.addItemToOrder(restaurant.getRestaurantId(), menuItem1);
    }

    /**
     * This step definition is used to simulate the user placing the order.
     */
    @When("places the order")
    public void places_the_order() {
        connectedUser.validateIndividualOrder(restaurant.getRestaurantId());
    }

    /**
     * This step definition is used to verify that the order is placed successfully.
     */
    @Then("the order is placed successfully")
    public void the_order_is_placed_successfully() {
        assertTrue(connectedUser.getOrdersHistory().size() == 1);
        assertEquals(OrderStatus.VALIDATED, connectedUser.getCurrentOrderState(restaurant.getRestaurantId(), connectedUser.getCurrentOrder()));
    }

    @When("the user tries to add {string} to their order")
    public void the_user_tries_to_add_to_their_order(String string) {
        assertThrows(IllegalArgumentException.class, () -> connectedUser.addItemToOrder(restaurant.getRestaurantId(), new MenuItem(string, 0.0)));
    }

    @Then("the user gets an error message {string}")
    public void the_user_get_an_error_message(String string) {
        System.out.println(string);
    }

    @Then("the order is not placed")
    public void the_order_is_not_placed() {
        assertTrue(connectedUser.getOrdersHistory().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> connectedUser.getCurrentOrderState(restaurant.getRestaurantId(), connectedUser.getCurrentOrder()));
    }

    @When("the user tries to place the order without adding any menu items")
    public void the_user_tries_to_place_the_order_without_adding_any_menu_items() {
        assertThrows(IllegalArgumentException.class, () -> connectedUser.proceedIndividualOrderCheckout());
    }
}
