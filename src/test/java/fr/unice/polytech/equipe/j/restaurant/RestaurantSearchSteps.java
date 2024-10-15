package fr.unice.polytech.equipe.j.restaurant;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RestaurantSearchSteps {

    private List<RestaurantFacade> foundRestaurants;  // Store search results

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
        System.out.println("Expected restaurants: " + expectedRestaurants);
        System.out.println("Found restaurants: " + foundRestaurants);
        assertEquals("Number of restaurants found does not match", expectedRestaurants.size(), foundRestaurants.size());

        for (Map<String, String> expectedRestaurant : expectedRestaurants) {
            boolean found = false;
            for (RestaurantFacade restaurant : foundRestaurants) {
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
}

