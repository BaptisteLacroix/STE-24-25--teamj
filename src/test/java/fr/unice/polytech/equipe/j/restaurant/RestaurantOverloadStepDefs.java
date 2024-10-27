package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.slot.Slot;
import fr.unice.polytech.equipe.j.user.RestaurantManager;
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

import static org.junit.Assert.*;

public class RestaurantOverloadStepDefs {
    private RestaurantManager restaurantManager;
    private boolean bool;
    private Clock clock;
    private Restaurant restaurant;
    private Slot slot;
    private final Menu menu = new Menu(new ArrayList<>());
    private Order order;
    private OrderManager orderManager;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Before
    public void setUp() {
        clock = Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris"));
    }

    @Given("a restaurant {string} opened from {string} to {string} which has a menu with following items:")
    public void aRestaurantOpenedFromToWhichHasAMenuWithFollowingItems(String restaurantName,String opening, String closing,DataTable dataTable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        restaurant = new Restaurant(restaurantName, LocalDateTime.parse(opening,formatter), LocalDateTime.parse(closing,formatter), menu);

        List<Map<String, String>> items = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> item : items) {
            String itemName = item.get("itemName");
            int prepTime = Integer.parseInt(item.get("prepTime"));
            double price = Double.parseDouble(item.get("price"));

            MenuItem newMenuItem = new MenuItem(itemName, prepTime, price);

            restaurant.getMenu().addMenuItem(newMenuItem);
        }
    }


    @Given("the restaurant receives an order with a {string} at {string}")
    public void jeanneReceivesAnOrderWithAAt(String menuItem, String slotHours) {
        MenuItem selectedItem = restaurant.getMenu().findItemByName(menuItem);
        assertNotNull(selectedItem);
        LocalDateTime slotStart = LocalDateTime.parse(slotHours, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        slot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        assertNotNull(slot);
    }


    @When("the restaurant adds {string} to this slot")
    public void jeanneAddsToThisSlot(String menuItem) {
        // On associe un personnel au slot arbitrairement pour regarder ce que donne le test
        slot.setNumberOfPersonnel(1);
        restaurant.addMenuItemToSlot(slot, restaurant.getMenu().findItemByName(menuItem));
    }

    @Then("the new current capacity of this slot should be {int}")
    public void theNewCurrentCapacityOfThisSlotShouldBe(int expectedCurrentCapacity) {
        assertEquals(expectedCurrentCapacity, slot.getCurrentCapacity());
    }

    @And("the available capacity should be {int}")
    public void theAvailableCapacityShouldBe(int expectedAvailableCapacity) {
        assertEquals(expectedAvailableCapacity, slot.getAvailableCapacity());
    }


    @Given("the restaurant has {int} personnel for each slot")
    public void theRestaurantHasPersonnelForEachSlot(int personnel) {
        for (Slot s : restaurant.getSlots()) {
            s.setNumberOfPersonnel(personnel);
        }
    }


    @And("the restaurant has a slot with full capacity at {string}")
    public void theRestaurantHasASlotWithFullCapacityAt(String fullSlot) {
        slot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, LocalDateTime.parse(fullSlot, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        slot.setCurrentCapacity(slot.getMaxCapacity());
    }


    @When("the restaurant adds an item {string} at {string}")
    public void theRestaurantAddsAnItemAt(String item, String fullSlot) {
        MenuItem foundItem = restaurant.getMenu().findItemByName(item);
        slot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, LocalDateTime.parse(fullSlot, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        bool = restaurant.addMenuItemToSlot(slot, foundItem);
    }


    @Then("it should be add to the next slot at {string} with a capacity of {int}")
    public void itShouldBeAddToTheNextSlotAt(String expectedSlotAllocated, int itemCapacity) {
        LocalDateTime slotStart = LocalDateTime.parse(expectedSlotAllocated, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Slot newSlot = RestaurantServiceManager.getInstance().findSlotByStartTime(restaurant, slotStart);
        assertEquals(itemCapacity, newSlot.getCurrentCapacity());
    }


    @And("the restaurant is full from its opening to its closing")
    public void theRestaurantIsFullFromItsOpeningToItsClosing() {
        for (Slot s : restaurant.getSlots()) {
            s.setCurrentCapacity(s.getMaxCapacity());
        }
    }

    @Then("the item is not added by the restaurant")
    public void theItemIsNotAddedByTheRestaurant() {
        assertFalse(bool);
    }


}
