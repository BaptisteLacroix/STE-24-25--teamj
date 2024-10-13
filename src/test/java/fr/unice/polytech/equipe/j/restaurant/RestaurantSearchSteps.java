package fr.unice.polytech.equipe.j.restaurant;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RestaurantSearchSteps {

    private final List<Restaurant> restaurants = new ArrayList<>();
    private List<RestaurantFacade> foundRestaurants = new ArrayList<>();  // Store search results

    // Given step - sets up multiple restaurants and their menu items
    @Given("the following restaurants exist:")
    public void givenMultipleRestaurantsExists(List<Map<String, String>> restaurantData) {
        for (Map<String, String> data : restaurantData) {
            String name = data.get("name");
            String menuItems = data.get("menu items");
            Menu menu = createMenuFromString(menuItems);
            Restaurant restaurant = new Restaurant(name, null, null, menu);
            restaurants.add(restaurant);
            RestaurantServiceManager.getInstance().addRestaurant(restaurant);
            System.out.println(restaurant);
        }
    }

    // Helper method to create a Menu from a string of menu items
    private Menu createMenuFromString(String menuItems) {
        Menu.MenuBuilder builder = new Menu.MenuBuilder();

        for (String item : menuItems.split("\", ")) {
            String[] parts = item.replace("\"", "").split(", ");
            builder.addMenuItem(new MenuItem(parts[0], Double.parseDouble(parts[1])));
        }
        return builder.build();
    }

    // When step - search for restaurants by name
    @When("I search for restaurants by name {string}")
    public void searchForRestaurantsByName(String restaurantName) {
        foundRestaurants = new ArrayList<>();
        foundRestaurants = RestaurantServiceManager.getInstance().searchByName(restaurantName);

        assertFalse("No restaurant found with name: " + restaurantName, foundRestaurants.isEmpty());
    }

    @Then("I should see the following restaurants:")
    public void verifySearchResults(List<Map<String, String>> expectedRestaurants) {
        // First, verify the number of restaurants matches
        assertEquals("Mismatch in number of restaurants", expectedRestaurants.size(), foundRestaurants.size());

        // Create a map for quick lookup of expected restaurant data by name
        Map<String, Map<String, String>> expectedRestaurantMap = new HashMap<>();
        for (Map<String, String> expectedRestaurant : expectedRestaurants) {
            expectedRestaurantMap.put(expectedRestaurant.get("name"), expectedRestaurant);
        }

        // Iterate through found restaurants and verify their details
        for (RestaurantFacade foundRestaurant : foundRestaurants) {
            String restaurantName = foundRestaurant.getRestaurantName();
            assertTrue("Unexpected restaurant found: " + restaurantName, expectedRestaurantMap.containsKey(restaurantName));

            // Get the expected menu data for this restaurant
            Map<String, String> expectedRestaurant = expectedRestaurantMap.get(restaurantName);
            Menu expectedMenu = createMenuFromString(expectedRestaurant.get("menu items"));
            List<MenuItem> menuItems = foundRestaurant.getMenu().getItems();

            // Verify that the found restaurant's menu matches the expected menu
            for (MenuItem expectedMenuItem : expectedMenu.getItems()) {
                assertTrue("Menu item not found or price mismatch for: " + expectedMenuItem.getName(),
                        menuItems.stream()
                                .anyMatch(item -> item.getName().equals(expectedMenuItem.getName()) && item.getPrice() == expectedMenuItem.getPrice())
                );
            }
        }
    }


    @When("I search for the food with name {string}")
    public void i_search_for_the_food_with_name(String foodName) {
        foundRestaurants = new ArrayList<>();
        foundRestaurants = RestaurantServiceManager.getInstance().searchByTypeOfFood(foodName);

        // Ensure that at least one restaurant is found offering the food
        assertFalse("No restaurant found with food: " + foodName, foundRestaurants.isEmpty());
    }

}
