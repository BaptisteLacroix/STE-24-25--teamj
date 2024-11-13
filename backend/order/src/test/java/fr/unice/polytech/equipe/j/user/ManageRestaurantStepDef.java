package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.menu.Menu;
import fr.unice.polytech.equipe.j.restaurant.menu.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.slot.Slot;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ManageRestaurantStepDef {
    private IRestaurant restaurant;
    private final Menu menu = new Menu.MenuBuilder().build();
    private boolean bool;

    @Before
    public void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
    }

    @Given("{string}, a restaurant manager of the {string} restaurant opened from {string} to {string}")
    public void aRestaurantManagerOfTheRestaurantOpenedFromTo(String name, String restaurantName, String opening, String closing) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        restaurant = new Restaurant(restaurantName, LocalDateTime.parse(opening,formatter), LocalDateTime.parse(closing,formatter), menu);
        RestaurantManager restaurantManager = new RestaurantManager("jeanne@example.com", "password", name, restaurant);
    }

    @And("the restaurant has a menu with the following items:")
    public void theRestaurantHasAMenuWithTheFollowingItems(DataTable dataTable) {
        List<Map<String, String>> items = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> item : items) {
            String itemName = item.get("itemName");
            int prepTime = Integer.parseInt(item.get("prepTime"));
            int price = Integer.parseInt(item.get("price"));

            MenuItem newMenuItem = new MenuItem(itemName, prepTime, price);

            restaurant.getMenu().addMenuItem(newMenuItem);
        }
    }

    @Given("Jeanne wants to set its restaurant opening hours: opening at {string} and closing at {string}")
    public void jeanneWantsToSetItsRestaurantOpeningHoursOpeningAtAndClosingAt(String openingTime, String closingTime) {
        // Just the context here, nothing to add here
    }

    @When("the restaurant manager sets the hours from {string} to {string}")
    public void theRestaurantManagerSetsTheHoursFromTo(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime openingHour = LocalDateTime.parse(startTime, formatter);
        LocalDateTime closingHour = LocalDateTime.parse(endTime, formatter);
        restaurant.setOpeningTime(openingHour);
        restaurant.setClosingTime(closingHour);
    }

    @Then("the opening hours should be {string} to {string}")
    public void theOpeningHoursShouldBeTo(String expectedStartTime, String expectedEndTime) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String actualOpeningHours = restaurant.getOpeningTime().get().format(outputFormatter) + " to " + restaurant.getClosingTime().get().format(outputFormatter);
        String expectedHours = expectedStartTime + " to " + expectedEndTime;
        assertEquals(expectedHours, actualOpeningHours);
    }


    @Given("Jeanne wants to update the price of {string}")
    public void jeanneWantsToUpdateThePriceOf(String itemName) {
        assertNotNull(restaurant.getMenu().findItemByName(itemName));
    }

    @When("the restaurant manager updates the price of {string} to {double}")
    public void theRestaurantManagerUpdatesThePriceOfTo(String itemName, double price) {
        restaurant.getMenu().findItemByName(itemName).setPrice(price);
    }

    @Then("the price of {string} should be {double}")
    public void thePriceOfShouldBe(String item, double price) {
        assertEquals(price, restaurant.getMenu().findItemByName(item).getPrice(), 0);
    }


    @Given("Jeanne wants to update the preparation time of {string}")
    public void jeanneWantsToUpdateThePreparationTimeOf(String itemName) {
        assertNotNull(restaurant.getMenu().findItemByName(itemName));
    }

    @When("the restaurant manager updates the preparation time of {string} to {int}")
    public void theRestaurantManagerUpdatesThePreparationTimeOfTo(String itemName, int itemPrepTime) {
        restaurant.getMenu().findItemByName(itemName).setPrepTime(itemPrepTime);
    }

    @Then("the preparation time of {string} should be {int} seconds")
    public void thePreparationTimeOfShouldBeSeconds(String itemName, int prepTime) {
        assertEquals(prepTime, restaurant.getMenu().findItemByName(itemName).getPrepTime());
    }


    @Given("Jeanne wants to update the number of personnel for the slot starting at {string}")
    public void jeanneWantsToUpdateTheNumberOfPersonnelForTheSlotStartingAt(String openingTime) {
        LocalDateTime slotStart = LocalDateTime.parse(openingTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        assertNotNull(RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart));
    }

    @When("the restaurant manager updates the personnel for this slot to {int}")
    public void theRestaurantManagerUpdatesThePersonnelForThisSlotTo(int newPersonnelCount) {
        LocalDateTime slotStart = LocalDateTime.parse("2024-10-18 12:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Slot slotToUpdate = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        restaurant.setNumberOfPersonnel(slotToUpdate, newPersonnelCount);
    }

    @Then("the number of personnel for the slot starting at {string} should be {int}")
    public void theNumberOfPersonnelForTheSlotStartingAtShouldBe(String slotStartTime, int expectedPersonnelCount) {
        LocalDateTime slotStart = LocalDateTime.parse(slotStartTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Slot slot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        assertEquals(expectedPersonnelCount, slot.getNumberOfPersonnel());
    }


    @Given("Jeanne wants to allocate {int} personnel to the slot starting at {string}")
    public void jeanneWantsToAllocatePersonnelToTheSlotStartingAt(int numberOfPersonnel, String slotStartingTime) {
            //Nothing here
    }

    @When("Jeanne tries to allocate {int} personnel to the slot starting at {string}")
    public void jeanneTriesToAllocatePersonnelToThisSlot(int newNumberOfPersonnel, String slotStartingTime) {
        LocalDateTime slotStart = LocalDateTime.parse(slotStartingTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Slot slot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        bool = restaurant.setNumberOfPersonnel(slot, newNumberOfPersonnel);
    }

    @Then("Jeanne will see that it is impossible with starting slot at {string}")
    public void jeanneWillSeeThatItIsImpossible(String slotStartingTime) {
        assertFalse(bool);
    }

    @Given("Jeanne wants to get the maximum capacity of a slot")
    public void jeanneWantsToGetTheMaximumCapacityOfASlot() {
        // Nothing to write here
    }

    @When("Jeanne allocates {int} personnel to te slot starting at {string}")
    public void jeanneAllocatesPersonnelToTeSlotStartingAt(int personnel, String slotStartingTime) {
        LocalDateTime slotStart = LocalDateTime.parse(slotStartingTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Slot slotToUpdate = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        restaurant.setNumberOfPersonnel(slotToUpdate, personnel);
    }

    @Then("the maximum capacity for the slot should be {int} seconds")
    public void theMaximumCapacityForTheSlotShouldBeMinutes(int duration) {
        LocalDateTime slotStart = LocalDateTime.parse("2024-10-18 12:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Slot selectedSlot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        assertEquals(selectedSlot.getMaxCapacity(), duration);
    }

}
