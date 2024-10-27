package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.GroupOrderProxy;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RestaurantSearchSteps {

    private List<IRestaurant> foundRestaurants;
    private List<MenuItem> menuItems = new ArrayList<>();
    private GroupOrderProxy groupOrder;

    @Before
    public void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
        // Initialize the list before each scenario
        foundRestaurants = new ArrayList<>();
    }

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
            IRestaurant restaurant = foundRestaurants.stream()
                    .filter(r -> r.getRestaurantName().trim().equalsIgnoreCase(expectedRestaurant.get("name").trim()))
                    .findFirst()
                    .orElse(null);
            assertNotNull("Restaurant not found: " + expectedRestaurant.get("name"), restaurant);

            Menu menu = restaurant.getMenu();
            String[] menuItems = expectedRestaurant.get("menu items").split(", ");
            String[] prices = expectedRestaurant.get("price").split(", ");
            for (int i = 0; i < menuItems.length; i++) {
                MenuItem menuItem = menu.findItemByName(menuItems[i]);
                assertNotNull("Menu item not found: " + menuItems[i], menuItem);
                assertEquals("Menu item price does not match", Double.parseDouble(prices[i]), menuItem.getPrice(), 0.01);
            }
        }
    }

    @Then("the user should not see any restaurants")
    public void the_user_should_not_see_any_restaurants() {
        assertTrue("Restaurants found when none were expected", foundRestaurants.isEmpty());
    }

    @Given("the user creates a group order with a delivery time of {int} minutes later than the current time")
    public void the_user_creates_a_group_order_with_a_delivery_time_of_minutes_later_than_the_current_time(Integer int1) {
        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, TimeUtils.getNow().plusMinutes(int1));
        groupOrder = new GroupOrderProxy(new GroupOrder(deliveryDetails));
    }

    @Given("the user searches for restaurants that can prepare the food before the delivery time")
    public void the_user_searches_for_restaurants_that_can_prepare_the_food_before_the_delivery_time() {
        foundRestaurants = RestaurantServiceManager.getInstance().searchRestaurantByDeliveryTime(groupOrder.getDeliveryDetails().getDeliveryTime());
        assertFalse(foundRestaurants.isEmpty());
    }

    @Then("the user selects the restaurant {string} and should see only the {int} menu items that can be prepared before the delivery time")
    public void the_user_select_the_restaurant_and_should_see_only_the_menu_items_that_can_be_prepared_before_the_delivery_time(String string, Integer int1) {
        IRestaurant restaurant = null;
        for (IRestaurant r : foundRestaurants) {
            if (r.getRestaurantName().equals(string)) {
                restaurant = r;
                break;
            }
        }

        menuItems = RestaurantServiceManager.getInstance().searchItemsByDeliveryTime(restaurant, groupOrder.getDeliveryDetails().getDeliveryTime());
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

