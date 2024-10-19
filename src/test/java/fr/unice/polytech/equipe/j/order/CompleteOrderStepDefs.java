package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.user.CampusUser;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CompleteOrderStepDefs {
    private Clock clock;
    private CampusUser user1;
    private CampusUser user2;
    private CampusUser user3;
    private GroupOrder groupOrder;
    private Restaurant restaurant;

    @Before
    public void setUp() {
        clock = Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris"));
    }


    @Given("the first user creates a group order with delivery location {string} and delivery time of {int}:{int} PM")
    public void the_first_user_creates_a_group_order_with_delivery_location_and_delivery_time_of_pm(String string, Integer int1, Integer int2) {
        user1 = new CampusUser("user1@user", "password123", new OrderManager(clock));
        DeliveryLocation location = DeliveryLocationManager.getInstance().findLocationByName(string);
        DeliveryDetails deliveryDetails = new DeliveryDetails(location, LocalDateTime.now(clock).withHour(int1).withMinute(int2));
        user1.createGroupOrder(deliveryDetails);
        groupOrder = user1.getCurrentGroupOrder();
        assertNotNull(groupOrder);
    }

    @Given("the user receives a group order identifier")
    public void the_user_receives_a_group_order_identifier() {
        assertNotNull(groupOrder.getGroupOrderId());
    }

    @Given("He search restaurant that are open and can prepare items in time and should see:")
    public void he_search_restaurant_that_are_open_and_can_prepare_items_in_time_and_should_see(io.cucumber.datatable.DataTable dataTable) {
        List<Restaurant> restaurants = RestaurantServiceManager.getInstance().searchRestaurantByDeliveryTime(groupOrder.getDeliveryDetails().getDeliveryTime());
        int expectedSize = dataTable.height() - 1;
        assertEquals(expectedSize, restaurants.size());
    }

    @Given("He select the restaurant {string} and see the items compatible with the group order delivery time preparation:")
    public void he_select_the_restaurant_and_see_the_items_compatible_with_the_group_order_delivery_time_preparation(String string, io.cucumber.datatable.DataTable dataTable) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
        List<MenuItem> items = RestaurantServiceManager.getInstance().searchItemsByDeliveryTime(restaurant, groupOrder.getDeliveryDetails().getDeliveryTime());
        int expectedSize = dataTable.height() - 1;
        assertEquals(expectedSize, items.size());
    }

    @Given("The first user adds the following items to his order:")
    public void the_first_user_adds_the_following_items_to_his_order(io.cucumber.datatable.DataTable dataTable) {
        user1.startSubGroupOrder(restaurant);
        assertNotNull(user1.getCurrentOrder());
        for (int i = 1; i < dataTable.height(); i++) {
            String itemName = dataTable.row(i).get(0);
            MenuItem item = restaurant.getMenu().findItemByName(itemName);
            user1.addItemToOrder(restaurant, item);
        }
        assertEquals(dataTable.height() - 1, user1.getCurrentOrder().getItems().size());
    }

    @Then("The first user validates his order")
    public void the_first_user_validates_his_order() {
        user1.validateOrder();
        assertEquals(OrderStatus.VALIDATED, user1.getCurrentOrder().getStatus());
    }

    @Given("The second user joins the group order")
    public void the_second_user_joins_the_group_order() {
        user2 = new CampusUser("user2@user", "password", new OrderManager(clock));
        user2.joinGroupOrder(groupOrder);
        assertNotNull(user2.getCurrentGroupOrder());
    }

    @Given("The second user adds the following items to his order:")
    public void the_second_user_adds_the_following_items_to_his_order(io.cucumber.datatable.DataTable dataTable) {
        user2.startSubGroupOrder(restaurant);
        assertNotNull(user2.getCurrentOrder());
        for (int i = 1; i < dataTable.height(); i++) {
            String itemName = dataTable.row(i).get(0);
            MenuItem item = restaurant.getMenu().findItemByName(itemName);
            user2.addItemToOrder(restaurant, item);
        }
        assertEquals(dataTable.height() - 1, user2.getCurrentOrder().getItems().size());
    }

    @Then("The second user validates his order")
    public void the_second_user_validates_his_order() {
        user2.validateOrder();
        assertEquals(OrderStatus.VALIDATED, user2.getCurrentOrder().getStatus());
    }

    @Given("The third user joins the group order")
    public void the_third_user_joins_the_group_order() {
        user3 = new CampusUser("user3@user", "password", new OrderManager(clock));
        user3.joinGroupOrder(groupOrder);
    }

    @Given("The third user adds the following items to his order:")
    public void the_third_user_adds_the_following_items_to_his_order(io.cucumber.datatable.DataTable dataTable) {
        user3.startSubGroupOrder(restaurant);
        assertNotNull(user3.getCurrentOrder());
        for (int i = 1; i < dataTable.height(); i++) {
            String itemName = dataTable.row(i).get(0);
            MenuItem item = restaurant.getMenu().findItemByName(itemName);
            user3.addItemToOrder(restaurant, item);
        }
        assertEquals(dataTable.height() - 1, user3.getCurrentOrder().getItems().size());
    }

    @Then("The third user validates his order and validates the group order")
    public void the_third_user_validates_his_order_and_validates_the_group_order() {
        user3.validateOrderAndGroupOrder();
    }

    @When("the second user tries to bypass the restaurant selection by delivery time and selects the restaurant {string} that cannot prepare items in time")
    public void the_second_user_tries_to_bypass_the_restaurant_selection_by_delivery_time_and_selects_the_restaurant_that_cannot_prepare_items_in_time(String string) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
        user2.startSubGroupOrder(restaurant);
        assertNull(user2.getCurrentOrder());
    }

    @Given("the second user selects the restaurant {string}")
    public void the_second_user_selects_the_restaurant(String string) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
    }

    @Then("the second user tries to add the following item to their order:")
    public void the_second_user_tries_to_add_the_following_item_to_their_order(io.cucumber.datatable.DataTable dataTable) {
        user2.startSubGroupOrder(restaurant);
        for (int i = 1; i < dataTable.height(); i++) {
            String itemName = dataTable.row(i).get(0);
            MenuItem item = restaurant.getMenu().findItemByName(itemName);
            assertThrows(IllegalArgumentException.class, () -> user2.addItemToOrder(restaurant, item));
        }
    }

    @Then("the system rejects the item and displays an error: {string}")
    public void the_system_rejects_the_item_and_displays_an_error(String string) {
        System.out.println(string);
    }

    @Given("The third user tries to join the group order")
    public void the_third_user_tries_to_join_the_group_order() {
        user3 = new CampusUser("user@user", "password", new OrderManager(clock));
        assertThrows(IllegalArgumentException.class, () -> user3.joinGroupOrder(groupOrder));
    }

    @Then("the system rejects the third user and displays an error: {string}")
    public void the_system_rejects_the_third_user_and_displays_an_error(String string) {
        System.out.println(string);
    }

    @Then("The first user validates his order and validates the group order")
    public void the_first_user_validates_his_order_and_validates_the_group_order() {
        user1.validateOrderAndGroupOrder();
    }

    @Given("the first user creates an individual order with the restaurant {string} and with delivery location {string} and delivery time of {int}:{int} PM")
    public void the_first_user_creates_an_individual_order_with_the_restaurant_and_with_delivery_location_and_delivery_time_of_pm(String string, String string2, Integer int1, Integer int2) {
        user1 = new CampusUser("user1@user", "password", new OrderManager(clock));
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
        DeliveryLocation location = DeliveryLocationManager.getInstance().findLocationByName(string2);
        DeliveryDetails deliveryDetails = new DeliveryDetails(location, LocalDateTime.now(clock).withHour(int1).withMinute(int2));
        user1.startIndividualOrder(restaurant, deliveryDetails);
        assertNotNull(user1.getCurrentOrder());
        assertNull(user1.getCurrentGroupOrder());
    }

    @Given("He search restaurant that are open and can prepare items in time for it's individual order and should see:")
    public void he_search_restaurant_that_are_open_and_can_prepare_items_in_time_for_it_s_individual_order_and_should_see(io.cucumber.datatable.DataTable dataTable) {
        List<Restaurant> restaurants = RestaurantServiceManager.getInstance().searchRestaurantByDeliveryTime(((IndividualOrder) user1.getCurrentOrder()).getDeliveryDetails().getDeliveryTime());
        int expectedSize = dataTable.height() - 1;
        assertEquals(expectedSize, restaurants.size());
    }

    @Given("He select the restaurant {string} and see the items compatible with the individual order delivery time preparation:")
    public void he_select_the_restaurant_and_see_the_items_compatible_with_the_individual_order_delivery_time_preparation(String string, io.cucumber.datatable.DataTable dataTable) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
        List<MenuItem> items = RestaurantServiceManager.getInstance().searchItemsByDeliveryTime(restaurant, ((IndividualOrder) user1.getCurrentOrder()).getDeliveryDetails().getDeliveryTime());
        int expectedSize = dataTable.height() - 1;
        assertEquals(expectedSize, items.size());
    }

    @Given("The first user adds the following items to his individual order:")
    public void the_first_user_adds_the_following_items_to_his_individual_order(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 1; i < dataTable.height(); i++) {
            String itemName = dataTable.row(i).get(0);
            MenuItem item = restaurant.getMenu().findItemByName(itemName);
            user1.addItemToOrder(restaurant, item);
        }
        assertEquals(dataTable.height() - 1, user1.getCurrentOrder().getItems().size());
    }

    @Then("The first user validates his individual order")
    public void the_first_user_validates_his_individual_order() {
        user1.validateOrder();
        assertEquals(OrderStatus.VALIDATED, user1.getCurrentOrder().getStatus());
        assertEquals(0, restaurant.getPendingOrders().size());
        assertEquals(1, restaurant.getOrdersHistory().size());
    }
}
