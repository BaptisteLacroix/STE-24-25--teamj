package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.restaurant.Menu;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ManageRestaurantStepDef {
    private RestaurantManager restaurantManager;
    private Restaurant restaurant;
    private final Menu menu = new Menu(new ArrayList<>());
    private final List<Slot> slots = new ArrayList<>();
    private MenuItem selectedItem;
    private Slot slot;

    @Before
    public void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
    }

    @Given("{string}, a restaurant manager of the {string} restaurant")
    public void aRestaurantManagerOfTheRestaurant(String name, String restaurantName) {
        restaurant = new Restaurant(restaurantName, TimeUtils.getNow(), TimeUtils.getNow().plusHours(8), menu);
        restaurantManager = new RestaurantManager("jeanne@example.com", "password", name, restaurant);
    }

    @And("the restaurant has a menu with the following items:")
    public void theRestaurantHasAMenuWithTheFollowingItems(DataTable dataTable) {
        List<Map<String, String>> items = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> item : items) {
            String itemName = item.get("itemName");
            int prepTime = Integer.parseInt(item.get("prepTime"));
            int price = Integer.parseInt(item.get("price"));

            restaurantManager.addMenuItem(itemName, prepTime, price);
        }
    }

    @Given("Jeanne wants to set its restaurant opening hours")
    public void jeanneWantsToSetItsRestaurantOpeningHours() {
        // Nothing here
    }


    @When("the restaurant manager sets the hours from {string} to {string}")
    public void theRestaurantManagerSetsTheHoursFromTo(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime openingHour = LocalDateTime.parse(startTime, formatter);
        LocalDateTime closingHour = LocalDateTime.parse(endTime, formatter);
        restaurantManager.updateHours(openingHour, closingHour);
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
        selectedItem = restaurant.getMenu().findItemByName(itemName);
        selectedItem.setPrice(price);
    }

    @Then("the price of {string} should be {double}")
    public void thePriceOfShouldBe(String item, double price) {
        assertEquals(price, restaurant.getMenu().findItemByName(item).getPrice(), 0.01);
    }


    @Given("Jeanne wants to update the preparation time of {string}")
    public void jeanneWantsToUpdateThePreparationTimeOf(String itemName) {
        assertNotNull(restaurant.getMenu().findItemByName(itemName));
    }

    @When("the restaurant manager updates the preparation time of {string} to {int}")
    public void theRestaurantManagerUpdatesThePreparationTimeOfTo(String itemName, int itemPrepTime) {
        selectedItem = restaurant.getMenu().findItemByName(itemName);
        selectedItem.setPrepTime(itemPrepTime);
    }

    @Then("the preparation time of {string} should be {int} seconds")
    public void thePreparationTimeOfShouldBeSeconds(String itemName, int prepTime) {
        assertEquals(prepTime, restaurant.getMenu().findItemByName(itemName).getPrepTime());
    }

    @Given("the restaurant has slots from {string} to {string}")
    public void theRestaurantHasSlotsFromTo(String openingTime, String closingTime, io.cucumber.datatable.DataTable dataTable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        dataTable.asMaps().forEach(row -> {
            LocalDateTime slotStart = LocalDateTime.parse(row.get("slotStart"), formatter);
            int personnel = Integer.parseInt(row.get("personnel"));
            int currentCapacity = Integer.parseInt(row.get("currentCapacity"));

            Slot slot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
            slot.addCapacity(currentCapacity);
            slot.setNumberOfPersonnel(personnel);
            slots.add(slot);
        });

    }

    @And("Jeanne wants to update the number of personnel for the slot starting at {string}")
    public void jeanneWantsToUpdateTheNumberOfPersonnelForTheSlotStartingAt(String openingTime) {
        LocalDateTime slotStart = LocalDateTime.parse(openingTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        assertNotNull(RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart));
    }

    @When("the restaurant manager updates the personnel for this slot to {int}")
    public void theRestaurantManagerUpdatesThePersonnelForThisSlotTo(int newPersonnelCount) {
        LocalDateTime slotStart = LocalDateTime.parse("2024-10-18 14:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Slot slotToUpdate = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        restaurantManager.updateNumberOfPersonnel(slotToUpdate, newPersonnelCount);
    }

    @Then("the number of personnel for the slot starting at {string} should be {int}")
    public void theNumberOfPersonnelForTheSlotStartingAtShouldBe(String slotStartTime, int expectedPersonnelCount) {
        LocalDateTime slotStart = LocalDateTime.parse(slotStartTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Slot slot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        assertEquals(expectedPersonnelCount, slot.getNumberOfPersonnel());
    }


    @And("Jeanne wants to allocate {int} personnel to the slot starting at {string}")
    public void jeanneWantsToAllocatePersonnelToTheSlotStartingAt(int numberOfPersonnel, String slotStartingTime) {

    }

    @When("Jeanne tries to allocate {int} personnel to the slot starting at {string}")
    public void jeanneTriesToAllocatePersonnelToThisSlot(int newNumberOfPersonnel, String slotStartingTime) {
        LocalDateTime slotStart = LocalDateTime.parse(slotStartingTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Slot slot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        assertThrows(IllegalArgumentException.class, () -> restaurantManager.updateNumberOfPersonnel(slot, newNumberOfPersonnel));
    }

    @Then("Jeanne will see that it is impossible with starting slot at {string}")
    public void jeanneWillSeeThatItIsImpossible(String slotStartingTime) {
        LocalDateTime slotStart = LocalDateTime.parse(slotStartingTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Slot slot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        assertNull(slot);
    }

    @Given("a slot of {int} minutes is created")
    public void aSlotOfMinutesIsCreated(int duration) {
        slot = new Slot(TimeUtils.getNow(), 0);
    }

    @When("Jeanne allocates {int} personnel to this slot")
    public void jeanneAllocatesPersonnelToThisSlot(int personnel) {
        restaurantManager.updateNumberOfPersonnel(slot, personnel);
    }

    @Then("the maximum capacity for the slot should be {int} seconds")
    public void theMaximumCapacityForTheSlotShouldBeMinutes(int duration) {
        assertEquals(slot.getMaxCapacity(), duration);
    }


    @And("the restaurant receives an order with a {string} at {string}")
    public void jeanneReceivesAnOrderWithAAt(String menuItem, String slotHours) {
        selectedItem = restaurant.getMenu().findItemByName(menuItem);
        LocalDateTime slotStart = LocalDateTime.parse(slotHours, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        slot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        slot.setNumberOfPersonnel(1);
    }

    @When("the restaurant adds {string} to this slot")
    public void jeanneAddsToThisSlot(String menuItem) {
        restaurant.addMenuItemToSlot(slot, selectedItem);
    }

    @Then("the new current capacity of this slot should be {int}")
    public void theNewCurrentCapacityOfThisSlotShouldBe(int expectedCurrentCapacity) {
        assertEquals(expectedCurrentCapacity, slot.getCurrentCapacity());
    }

    @And("the available capacity should be {int}")
    public void theAvailableCapacityShouldBe(int expectedAvailableCapacity) {
        assertEquals(expectedAvailableCapacity, slot.getAvailableCapacity());
    }

    @Then("it would be add to the next slot at {string} with a capacity of {int}")
    public void itWouldBeAddToTheNextSlotAt(String expectedSlotAllocated, int itemCapacity) {
        LocalDateTime slotStart = LocalDateTime.parse(expectedSlotAllocated, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        slot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        assertEquals(itemCapacity, slot.getCurrentCapacity());
    }

    @Then("the item is not added by the restaurant")
    public void theItemIsNotAddedByTheRestaurant() {
        assertFalse(restaurant.addMenuItemToSlot(slot, selectedItem));
    }
}
