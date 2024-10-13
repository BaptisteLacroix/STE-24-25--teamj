package fr.unice.polytech.equipe.j.stepdefs.backend.restaurant;

import fr.unice.polytech.equipe.j.restaurant.Menu;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class RestaurantMenuSteps {

    private List<Restaurant> restaurants = new ArrayList<>();
    private Restaurant currentRestaurant;


    // Given step - sets up the campus restaurant and its menu items
    @Given("the following campus restaurant exists:")
    public void givenRestaurantExists(List<Map<String, String>> restaurantData) {
        for (Map<String, String> data : restaurantData) {
            String name = data.get("name");
            String menuItems = data.get("menu items");
            Menu menu = createMenuFromString(menuItems);
            Restaurant restaurant = new Restaurant(name, null, null, menu);
            restaurants.add(restaurant);
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

    // When step - user visits the specific restaurant
    @When("the user visits the {string} restaurant")
    public void userVisitsRestaurant(String restaurantName) {
        currentRestaurant = restaurants.stream()
                .filter(restaurant -> restaurant.getRestaurantName().equals(restaurantName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        System.out.println(currentRestaurant.getRestaurantName());
    }

    @When("when the user visits the {string} restaurant")
    public void when_the_user_visits_the_restaurant(String restaurantName) {
        currentRestaurant = restaurants.stream()
                .filter(restaurant -> restaurant.getRestaurantName().equals(restaurantName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

    }

    @Then("the user should see the menu for {string} with the following items:")
    public void verifyMenu(String restaurantName, List<Map<String, String>> expectedMenuItems) {
        assertEquals(restaurantName, currentRestaurant.getRestaurantName());

        List<MenuItem> menuItems = currentRestaurant.getMenu().getItems();

        for (Map<String, String> expectedItem : expectedMenuItems) {
            String name = expectedItem.get("name");
            double price = Double.parseDouble(expectedItem.get("price"));

            // verifier  that the menuItems names' match
            assertTrue(menuItems.stream()
                    .anyMatch(item -> item.name().equals(name) ));
        }
    }

    // Verification
    @Then("the user should see the menu of {string} with the following items:")
    public void verifyMenuMulti(String restaurantName, List<Map<String, String>> expectedMenuItems) {
        assertEquals(restaurantName, currentRestaurant.getRestaurantName());

        // Get the actual items from the restaurant s menu
        List<MenuItem> menuItems = currentRestaurant.getMenu().getItems();

        for (Map<String, String> expectedItem : expectedMenuItems) {
            String name = expectedItem.get("name");
            double price = Double.parseDouble(expectedItem.get("price"));

            // verification finale
            assertTrue(menuItems.stream()
                    .anyMatch(item -> item.name().equals(name) && item.price() == price));
        }
    }

    @Given("the following campus restaurants exist:")
    public void givenMultipleRestaurantsExist(List<Map<String, String>> restaurantData) {
        for (Map<String, String> data : restaurantData) {
            String name = data.get("name");
            String menuItems = data.get("menu items");
            Menu menu = createMenuFromString(menuItems);
            Restaurant restaurant = new Restaurant(name, null, null, menu);
            restaurants.add(restaurant);
        }
    }

}
