package fr.unice.polytech.equipe.j.stepdefs.backend.order;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderBuilder;
import fr.unice.polytech.equipe.j.restaurant.Menu;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantFactory;
import fr.unice.polytech.equipe.j.user.ConnectedUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.text.ParseException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class PlaceOrderStepDefs {

    private Restaurant restaurant;
    private OrderBuilder orderBuilder;
    private Order order;

    /**
     * This step definition is used to simulate a user registration.
     */
    @Given("the user is registered")
    public void the_user_is_registered() {
        new ConnectedUser("john@example.com", "password", 100.0);
    }

    /**
     * This step definition is used to simulate the user selecting a restaurant.
     * @param restaurantName the name of the restaurant
     */
    @Given("the user has selected the restaurant {string}")
    public void the_user_has_selected_the_restaurant(String restaurantName) throws ParseException {
        restaurant = RestaurantFactory.createRestaurant(restaurantName, "10:00", "22:00", null);
    }

    /**
     * This step definition is used to simulate the user viewing the menu of a restaurant.
     * @param restaurantName the name of the restaurant
     * @param item1 the first item in the menu
     * @param item2 the second item in the menu
     */
    @Given("the menu of {string} includes {string} and {string}")
    public void the_menu_of_includes_and(String restaurantName, String item1, String item2) {
        Menu menu = new Menu.MenuBuilder()
                .addMenuItem(RestaurantFactory.createMenuItem(item1, 10.0))
                .addMenuItem(RestaurantFactory.createMenuItem(item2, 10.0))
                .build();

        restaurant.changeMenu(menu);
    }

    /**
     * This step definition is used to simulate the user adding items to their order.
     * @param item1 the first item
     * @param item2 the second item
     */
    @When("the user adds {string} and {string} to their order")
    public void the_user_adds_and_to_their_order(String item1, String item2) {
        MenuItem menuItem1 = restaurant.getMenu().findItemByName(item1);
        MenuItem menuItem2 = restaurant.getMenu().findItemByName(item2);

        // Start building the order
        orderBuilder = new OrderBuilder()
                .setRestaurant(restaurant)
                .addMenuItem(menuItem1)
                .addMenuItem(menuItem2);
    }

    /**
     * This step definition is used to simulate the user setting a delivery time for their order.
     * @param hour the hour of the delivery time
     * @param minute the minute of the delivery time
     */
    @When("sets a delivery time for {int}:{int} PM today")
    public void sets_a_delivery_time_for_pm_today(Integer hour, Integer minute) {
        LocalDateTime deliveryTime = LocalDateTime.now()
                .withHour(hour)
                .withMinute(minute);

        orderBuilder.setDeliveryTime(deliveryTime);
    }

    /**
     * This step definition is used to simulate the user placing the order.
     */
    @When("places the order")
    public void places_the_order() {
        order = orderBuilder.build();
        restaurant.addOrder(order);
    }

    /**
     * This step definition is used to verify that the order is placed successfully.
     */
    @Then("the order is placed successfully")
    public void the_order_is_placed_successfully() {
        assertNotNull(order);
        assertEquals(restaurant, order.getRestaurant());
        assertTrue(restaurant.getOrders().contains(order));
    }
}
