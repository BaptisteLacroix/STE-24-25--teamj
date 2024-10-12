package features;

import fr.unice.polytech.equipe.j.restaurant.Menu;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.slot.Slot;
import fr.unice.polytech.equipe.j.user.RestaurantManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ManageRestaurantStepDef {
    private RestaurantManager restaurantManager;
    private Restaurant restaurant;
    private LocalDateTime openingHour;
    private LocalDateTime closingHour;
    private Menu menu = new Menu();
    private List<Slot> slots;
    private MenuItem selectedItem;

    @Given("{string}, a restaurant manager of the {string} restaurant")
    public void aRestaurantManagerOfTheRestaurant(String name, String restaurantName) {
        restaurant = new Restaurant(restaurantName, slots, "Aucune", LocalDateTime.now(), LocalDateTime.now().plusHours(8),menu);
        restaurantManager = new RestaurantManager(name, restaurant);
    }
    @And("the restaurant has a menu with the following items:")
    public void theRestaurantHasAMenuWithTheFollowingItems(DataTable dataTable) {
        List<Map<String, String>> items = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> item : items) {
            String itemName = item.get("itemName");
            String description = item.get("description");
            int prepTime = Integer.parseInt(item.get("prepTime"));
            int price = Integer.parseInt(item.get("price"));
            int capacity = Integer.parseInt(item.get("capacity"));

            restaurantManager.addMenuItem(itemName, description, prepTime, price, capacity);
        }
    }

    @Given("Jeanne wants to set its restaurant opening hours")
    public void jeanneWantsToSetItsRestaurantOpeningHours() {
        // Nothing here
    }


    @When("the restaurant manager sets the hours from {string} to {string}")
    public void theRestaurantManagerSetsTheHoursFromTo(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        openingHour = LocalDateTime.parse(startTime, formatter);
        closingHour = LocalDateTime.parse(endTime, formatter);
        restaurantManager.updateHours(openingHour, closingHour);
    }

    @Then("the opening hours should be {string} to {string}")
    public void theOpeningHoursShouldBeTo(String expectedStartTime, String expectedEndTime) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String actualOpeningHours = restaurant.getOpeningHour().format(outputFormatter) + " to " + restaurant.getClosingHour().format(outputFormatter);
        String expectedHours = expectedStartTime + " to " + expectedEndTime;
        assertEquals(expectedHours, actualOpeningHours);

    }


    @Given("Jeanne wants to update the price of {string}")
    public void jeanneWantsToUpdateThePriceOf(String itemName) {
        assertNotNull(restaurant.getMenu().findMenuItemByName(itemName));
    }

    @When("the restaurant manager updates the price of {string} to {int}")
    public void theRestaurantManagerUpdatesThePriceOfTo(String itemName, int price) {
        selectedItem = restaurant.getMenu().findMenuItemByName(itemName);
        selectedItem.setPrice(price);
    }

    @Then("the price of {string} should be {int}")
    public void thePriceOfShouldBe(String item, int price) {
        assertEquals(price, restaurant.getMenu().findMenuItemByName(item).getPrice());
    }


    @Given("Jeanne wants to update the preparation time of {string}")
    public void jeanneWantsToUpdateThePreparationTimeOf(String itemName) {
        assertNotNull(restaurant.getMenu().findMenuItemByName(itemName));
    }

    @When("the restaurant manager updates the preparation time of {string} to {int}")
    public void theRestaurantManagerUpdatesThePreparationTimeOfTo(String itemName, int itemPrepTime) {
        selectedItem = restaurant.getMenu().findMenuItemByName(itemName);
        selectedItem.setPrepTime(itemPrepTime);
    }

    @Then("the preparation time of {string} should be {int} seconds")
    public void thePreparationTimeOfShouldBeSeconds(String itemName, int prepTime) {
        assertEquals(prepTime, restaurant.getMenu().findMenuItemByName(itemName).getPrepTime());
    }

    @Given("Jeanne wants to update the capacity of {string}")
    public void jeanneWantsToUpdateTheCapacityOf(String itemName) {
        assertNotNull(restaurant.getMenu().findMenuItemByName(itemName));
    }

    @When("Jeanne updates the preparation time of {string} to {int}")
    public void jeanneUpdatesThePreparationTimeOfTo(String itemName, int itemCapacity) {
        selectedItem = restaurant.getMenu().findMenuItemByName(itemName);
        selectedItem.setCapacity(itemCapacity);
    }

    @Then("the capacity of {string} should be {int}")
    public void theCapacityOfShouldBe(String itemName, int itemCapacity) {
        assertEquals(itemCapacity, restaurant.getMenu().findMenuItemByName(itemName).getCapacity());
    }
}