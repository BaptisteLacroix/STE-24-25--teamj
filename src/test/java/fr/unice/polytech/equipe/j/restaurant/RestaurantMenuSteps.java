package fr.unice.polytech.equipe.j.restaurant;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class RestaurantMenuSteps {

    private RestaurantFacade currentRestaurant;

    // When step - user visits the specific restaurant
    @When("the user visits the {string} restaurant")
    public void userVisitsRestaurant(String restaurantName) {
        currentRestaurant = RestaurantServiceManager.getInstance().searchByName(restaurantName).getFirst();
        assertNotNull(currentRestaurant);
    }

    @Then("the user should see the menu for {string} with the following items:")
    public void verifyMenu(String restaurantName, List<Map<String, String>> expectedMenuItems) {
        assertEquals(restaurantName, currentRestaurant.getRestaurantName());

        // Get the actual items from the restaurant s menu
        List<MenuItem> menuItems = currentRestaurant.getMenu().getItems();

        for (Map<String, String> expectedItem : expectedMenuItems) {
            String name = expectedItem.get("name");
            double price = Double.parseDouble(expectedItem.get("price"));

            // verification finale
            assertTrue(menuItems.stream()
                    .anyMatch(item -> item.getName().equals(name) && item.getPrice() == price));
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
                    .anyMatch(item -> item.getName().equals(name) && item.getPrice() == price));
        }
    }

}
