package fr.unice.polytech.equipe.j.stepdefs.backend.restaurant;

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


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ManageRestaurantStepDef {
    private RestaurantManager restaurantManager;
    private Restaurant restaurant;
    private LocalDateTime openingHour;
    private LocalDateTime closingHour;
    private final Menu menu = new Menu(new ArrayList<>());
    private final List<Slot> slots = new ArrayList<>();
    private MenuItem selectedItem;
    private Slot slot;

    private Slot findSlotByStartTime(String slotStartTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(slotStartTime, formatter);

        return slots.stream()
                .filter(slot -> slot.getOpeningDate().equals(startTime))
                .findFirst()
                .orElse(null);
    }


    @Given("{string}, a restaurant manager of the {string} restaurant")
    public void aRestaurantManagerOfTheRestaurant(String name, String restaurantName) {
        restaurant = new Restaurant(restaurantName, slots, LocalDateTime.now(), LocalDateTime.now().plusHours(8), menu);
        restaurantManager = new RestaurantManager("jeanne@example.com", "password", 100.0,name, restaurant);
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
        openingHour = LocalDateTime.parse(startTime, formatter);
        closingHour = LocalDateTime.parse(endTime, formatter);
        restaurantManager.updateHours(openingHour, closingHour);
    }

    @Then("the opening hours should be {string} to {string}")
    public void theOpeningHoursShouldBeTo(String expectedStartTime, String expectedEndTime) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String actualOpeningHours = restaurant.getOpeningTime().format(outputFormatter) + " to " + restaurant.getClosingTime().format(outputFormatter);
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
        assertEquals(price, restaurant.getMenu().findItemByName(item).getPrice(),0.01);
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
            int currentCapacity = Integer.parseInt(row.get("currentCapacity"));
            int maxCapacity = Integer.parseInt(row.get("maxCapacity"));
            int personnel = Integer.parseInt(row.get("personnel"));

            Slot slot = new Slot(currentCapacity, maxCapacity, slotStart, personnel);
            slots.add(slot);
        });

    }

    @And("Jeanne wants to update the number of personnel for the slot starting at {string}")
    public void jeanneWantsToUpdateTheNumberOfPersonnelForTheSlotStartingAt(String openingTime) {
        assertNotNull(findSlotByStartTime(openingTime));
    }

    @When("the restaurant manager updates the personnel for this slot to {int}")
    public void theRestaurantManagerUpdatesThePersonnelForThisSlotTo(int newPersonnelCount) {
        Slot slotToUpdate = findSlotByStartTime("2024-10-08 13:00");
        restaurantManager.updateNumberOfPersonnel(slotToUpdate, newPersonnelCount);
    }

    @Then("the number of personnel for the slot starting at {string} should be {int}")
    public void theNumberOfPersonnelForTheSlotStartingAtShouldBe(String slotStartTime, int expectedPersonnelCount) {
        Slot slot = findSlotByStartTime(slotStartTime);
        assertEquals(expectedPersonnelCount, slot.getNumberOfPersonnel());
    }



    @And("Jeanne wants to allocate {int} personnel to the slot starting at {string}")
    public void jeanneWantsToAllocatePersonnelToTheSlotStartingAt(int numberOfPersonnel, String slotStartingTime) {
        
    }

    @When("Jeanne tries to allocate {int} personnel to this slot")
    public void jeanneTriesToAllocatePersonnelToThisSlot(int newNumberOfPersonnel) {
        Slot slot = findSlotByStartTime("2024-10-08 14:00");
        restaurantManager.updateNumberOfPersonnel(slot, newNumberOfPersonnel);
    }

    @Then("Jeanne will see that it is impossible")
    public void jeanneWillSeeThatItIsImpossible() {

    }

    @Given("a slot of {int} minutes is created")
    public void aSlotOfMinutesIsCreated(int duration) {
        slot = new Slot(0,0,LocalDateTime.now(),0);
    }

    @When("Jeanne allocates {int} personnel to this slot")
    public void jeanneAllocatesPersonnelToThisSlot(int personnel) {
        restaurantManager.updateNumberOfPersonnel(slot,personnel);
    }

    @Then("the maximum capacity for the slot should be {int} seconds")
    public void theMaximumCapacityForTheSlotShouldBeMinutes(int duration) {
        assertEquals(slot.getMaxCapacity(),duration);
    }


    @And("the restaurant receives an order with a {string} at {string}")
    public void jeanneReceivesAnOrderWithAAt(String menuItem, String slotHours) {
        selectedItem = restaurant.getMenu().findItemByName(menuItem);
        slot = findSlotByStartTime(slotHours);
    }

    @When("the restaurant adds {string} to this slot")
    public void jeanneAddsToThisSlot(String menuItem) {

    }

    @Then("the new current capacity of this slot should be {int}")
    public void theNewCurrentCapacityOfThisSlotShouldBe(int arg0) {
    }
}