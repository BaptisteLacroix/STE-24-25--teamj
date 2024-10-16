package fr.unice.polytech.equipe.j.stepdefs.backend.order;

import fr.unice.polytech.equipe.j.restaurant.Menu;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;
import fr.unice.polytech.equipe.j.slot.Slot;
import fr.unice.polytech.equipe.j.user.ConnectedUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PlaceOrderStepDefs {

    private Restaurant restaurant;
    private ConnectedUser connectedUser;
    private RestaurantProxy restaurantProxy;
    private List<Slot> slots;


    /**
     * This step definition is used to simulate a user registration.
     */
    @Given("[PlaceOrder]the user is registered")
    public void the_user_is_registered() {
        connectedUser = new ConnectedUser("john@example.com", "password", 100.0);
    }

    /**
     * This step definition is used to simulate the user selecting a restaurant.
     *
     * @param restaurantName the name of the restaurant
     */
    @Given("[PlaceOrder]the user has selected the restaurant {string}")
    public void the_user_has_selected_the_restaurant(String restaurantName) {
        restaurant = new Restaurant(restaurantName, slots, LocalDateTime.now(), LocalDateTime.now(), null);
        restaurantProxy = new RestaurantProxy(List.of(restaurant));
    }

    /**
     * This step definition is used to simulate the user viewing the menu of a restaurant.
     *
     * @param restaurantName the name of the restaurant
     * @param item1          the first item in the menu
     * @param item2          the second item in the menu
     */
    @Given("[PlaceOrder]the menu of {string} includes {string} and {string}")
    public void the_menu_of_includes_and(String restaurantName, String item1, String item2) {
        Menu menu = new Menu.MenuBuilder()
                .addMenuItem(new MenuItem(item1, 10,10.0))
                .addMenuItem(new MenuItem(item2, 6,5.0))
                .build();

        restaurant.changeMenu(menu);
    }

    /**
     * This step definition is used to simulate the user adding items to their order.
     *
     * @param item1 the first item
     * @param item2 the second item
     */
    @When("[PlaceOrder]the user adds {string} and {string} to their order")
    public void the_user_adds_and_to_their_order(String item1, String item2) {
        MenuItem menuItem1 = restaurant.getMenu().findItemByName(item1);
        MenuItem menuItem2 = restaurant.getMenu().findItemByName(item2);

        connectedUser.startOrder(restaurantProxy, restaurant.getRestaurantId());
        connectedUser.addItemToOrder(restaurantProxy, restaurant.getRestaurantId(), menuItem1);
        connectedUser.addItemToOrder(restaurantProxy, restaurant.getRestaurantId(), menuItem2);
    }

    /**
     * This step definition is used to simulate the user adding item to their order.
     *
     * @param item1 the first item
     */
    @When("[PlaceOrder]the user adds {string} to their order")
    public void the_user_adds_to_their_order(String item1) {
        MenuItem menuItem1 = restaurant.getMenu().findItemByName(item1);

        connectedUser.startOrder(restaurantProxy, restaurant.getRestaurantId());
        connectedUser.addItemToOrder(restaurantProxy, restaurant.getRestaurantId(), menuItem1);
    }

    /**
     * This step definition is used to simulate the user placing the order.
     */
    @When("[PlaceOrder]places the order")
    public void places_the_order() {
        connectedUser.proceedCheckout(restaurantProxy, restaurant.getRestaurantId());
    }

    /**
     * This step definition is used to verify that the order is placed successfully.
     */
    @Then("[PlaceOrder]the order is placed successfully")
    public void the_order_is_placed_successfully() {
        assertTrue(restaurant.getOrders().contains(connectedUser.getOrdersHistory().getLast()));
    }

    @When("[PlaceOrder]the user tries to add {string} to their order")
    public void the_user_tries_to_add_to_their_order(String string) {
        assertThrows(IllegalArgumentException.class, () -> connectedUser.addItemToOrder(restaurantProxy, restaurant.getRestaurantId(), new MenuItem(string, 10,0.0)));
    }

    @Then("[PlaceOrder]the user get an error message {string}")
    public void the_user_get_an_error_message(String string) {
        System.out.println(string);
    }

    @Then("[PlaceOrder]the order is not placed")
    public void the_order_is_not_placed() {
        assertTrue(connectedUser.getOrdersHistory().isEmpty());
        assertTrue(restaurant.getOrders().isEmpty());
    }

    @When("[PlaceOrder]the user tries to place the order without adding any menu items")
    public void the_user_tries_to_place_the_order_without_adding_any_menu_items() {
        assertThrows(IllegalArgumentException.class, () -> connectedUser.proceedCheckout(restaurantProxy, restaurant.getRestaurantId()));
    }
}
