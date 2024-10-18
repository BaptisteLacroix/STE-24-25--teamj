package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.order.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.user.ConnectedUser;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RestaurantSearchSteps {

    private List<Restaurant> foundRestaurants;  // Store search results
    private ConnectedUser user = new ConnectedUser("email", "password", 100, new OrderManager());
    private List<MenuItem> menuItems = new ArrayList<>();

    @Before
    public void setUp() {
        // Initialize the list before each scenario
        foundRestaurants = new ArrayList<>();

    }

    // When step - search for restaurants by name
    @When("the user searches for restaurants by name {string}")
    public void searchForRestaurantsByName(String restaurantName) {
        foundRestaurants = RestaurantServiceManager.getInstance().searchByName(restaurantName);
        assertFalse("No restaurant found with name: " + restaurantName, foundRestaurants.isEmpty());
    }

    @Given("the user searches for restaurants by name {string} that is not in the system")
    public void the_user_searches_for_restaurants_by_name_that_is_not_in_the_system(String string) {
        foundRestaurants = RestaurantServiceManager.getInstance().searchByName(string);
        assertTrue("Restaurants found when none were expected", foundRestaurants.isEmpty());
    }

    @Given("the user searches for food with name {string} that is not in the system")
    public void the_user_searches_for_food_with_name_that_is_not_in_the_system(String string) {
        foundRestaurants = RestaurantServiceManager.getInstance().searchByTypeOfFood(string);
        assertTrue("Restaurants found when none were expected", foundRestaurants.isEmpty());
    }

    @When("the user searches for food with name {string}")
    public void searchForRestaurantsByFood(String foodName) {
        foundRestaurants = RestaurantServiceManager.getInstance().searchByTypeOfFood(foodName);
        assertFalse("No restaurant found with food: " + foodName, foundRestaurants.isEmpty());
    }

    @Then("the user should see the following restaurant\\(s):")
    public void the_user_should_see_the_following_restaurant_s(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> expectedRestaurants = dataTable.asMaps();
        assertEquals("Number of restaurants found does not match", expectedRestaurants.size(), foundRestaurants.size());

        for (Map<String, String> expectedRestaurant : expectedRestaurants) {
            boolean found = false;
            for (Restaurant restaurant : foundRestaurants) {
                if (restaurant.getRestaurantName().equals(expectedRestaurant.get("name"))) {
                    found = true;
                    Menu menu = restaurant.getMenu();
                    String[] menuItems = expectedRestaurant.get("menu items").split(", ");
                    String[] prices = expectedRestaurant.get("price").split(", ");
                    for (int i = 0; i < menuItems.length; i++) {
                        assertTrue("Menu item not found: " + menuItems[i], menu.findItemByName(menuItems[i]) != null);
                        assertEquals("Menu item price does not match", Double.parseDouble(prices[i]), menu.findItemByName(menuItems[i]).getPrice(), 0.01);
                    }
                }
            }
            assertTrue("Restaurant not found: " + expectedRestaurant.get("name"), found);
        }
    }

    @Then("the user should not see any restaurants")
    public void the_user_should_not_see_any_restaurants() {
        assertTrue("Restaurants found when none were expected", foundRestaurants.isEmpty());
    }

    @Given("the user creates a group order with a delivery time of {int} minutes later the current time")
    public void the_user_creates_a_group_order_with_a_delivery_time_of_minutes_later_the_current_time(Integer int1) {
        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, LocalDateTime.now().plusMinutes(int1));
        user.startGroupOrder(deliveryDetails);
        assertNotNull("Group order not created", user.getCurrentGroupOrder());
    }

    @Given("the user searches for restaurants that can prepare the food before the delivery time")
    public void the_user_searches_for_restaurants_that_can_prepare_the_food_before_the_delivery_time() {
        foundRestaurants = RestaurantServiceManager.getInstance().searchRestaurantByDeliveryTime(user.getCurrentGroupOrder());
        assertFalse(foundRestaurants.isEmpty());
    }

    @Then("the user select the restaurant {string} and should see only the {int} menu items that can be prepared before the delivery time")
    public void the_user_select_the_restaurant_and_should_see_only_the_menu_items_that_can_be_prepared_before_the_delivery_time(String string, Integer int1) {
        Restaurant restaurant = null;
        for (Restaurant r : foundRestaurants) {
            if (r.getRestaurantName().equals(string)) {
                restaurant = r;
                break;
            }
        }

        menuItems = RestaurantServiceManager.getInstance().searchItemsByDeliveryTime(restaurant, user.getCurrentGroupOrder().getDeliveryDetails().getDeliveryTime().get());
        assertFalse(menuItems.isEmpty());
        assertEquals("Number of menu items found does not match", int1.intValue(), menuItems.size());
    }

    @Then("the user should see the following menu items:")
    public void the_user_should_see_the_following_menu_items(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> expectedMenuItems = dataTable.asMaps();
        assertEquals("Number of menu items found does not match", expectedMenuItems.size(), menuItems.size());

        for (Map<String, String> expectedMenuItem : expectedMenuItems) {
            boolean found = false;
            for (MenuItem menuItem : menuItems) {
                if (menuItem.getName().equals(expectedMenuItem.get("name"))) {
                    found = true;
                    assertEquals("Menu item price does not match", Double.parseDouble(expectedMenuItem.get("price")), menuItem.getPrice(), 0.01);
                }
            }
            assertTrue("Menu item not found: " + expectedMenuItem.get("name"), found);
        }
    }
}

