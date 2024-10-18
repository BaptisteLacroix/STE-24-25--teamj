package fr.unice.polytech.equipe.j.restaurant;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class RestaurantMenuSteps {

    private Restaurant currentRestaurant;
    private Clock clock;

    @Before
    public void setUp() {
        clock = Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.systemDefault());
    }

    // When step - user visits the specific restaurant
    @When("the user visits the {string} restaurant")
    public void userVisitsRestaurant(String restaurantName) {
        currentRestaurant = RestaurantServiceManager.getInstance(clock).searchByName(restaurantName).getFirst();
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
