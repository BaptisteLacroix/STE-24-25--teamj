package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.user.CampusUser;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Clock;
import java.time.Instant;

import java.time.ZoneId;
import java.util.List;

import static org.junit.Assert.*;

public class CompleteOrderStepDefs {
    private CampusUser user1;
    private CampusUser user2;
    private CampusUser user3;
    private GroupOrder groupOrder;
    private Restaurant restaurant;
    private List<Restaurant> foundRestaurants;
    private Order orderUser1;
    private Order orderUser2;
    private Order orderUser3;
    private IllegalStateException itemAddException;
    private IllegalStateException userJoinsValidatedGroupOrderException;
    private IllegalArgumentException itemAddSlotException;
    private IndividualOrder individualOrderUser1;

    @Before
    public void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
    }

    @Given("the first user creates a group order with delivery location {string} and delivery time of {int}:{int} PM")
    public void the_first_user_creates_a_group_order_with_delivery_location_and_delivery_time_of_pm(String string, Integer int1, Integer int2) {
        user1 = new CampusUser("user1@user", "password123", new OrderManager());
        DeliveryLocation location = DeliveryLocationManager.getInstance().findLocationByName(string);
        DeliveryDetails deliveryDetails = new DeliveryDetails(location, TimeUtils.getNow().withHour(int1).withMinute(int2));
        this.groupOrder = new GroupOrder(deliveryDetails);
        groupOrder.addUser(user1);
        user1.setCurrentGroupOrder(groupOrder);
        assertNotNull( user1.getCurrentGroupOrder());
    }

    @Then("the group receives an identifier")
    public void theGroupReceivesAnIdentifier() {
        assertNotNull(groupOrder.getGroupOrderId());
    }

    @When("He searches restaurants that are open and can prepare items in time")
    public void heSearchesRestaurantsThatAreOpenAndCanPrepareItemsInTime() {
        this.foundRestaurants = RestaurantServiceManager.getInstance().searchRestaurantByDeliveryTime(groupOrder.getDeliveryDetails().getDeliveryTime());
    }

    @Then("He should see:")
    public void he_searches_restaurants_that_are_open_and_can_prepare_items_in_time_and_should_see(DataTable dataTable) {
        int expectedSize = dataTable.height() - 1;
        assertEquals(expectedSize, this.foundRestaurants.size());
    }

    @When("He selects the restaurant {string}")
    public void heSelectsTheRestaurant(String name) {
        this.restaurant = RestaurantServiceManager.getInstance().searchByName(name).getFirst();
    }

    @Then("He should see the items compatible with the group order delivery time preparation:")
    public void heShouldSeeTheItemsCompatibleWithTheGroupOrderDeliveryTimePreparation(DataTable dataTable) {
        List<MenuItem> items = RestaurantServiceManager.getInstance().searchItemsByDeliveryTime(restaurant, groupOrder.getDeliveryDetails().getDeliveryTime());
        int expectedSize = dataTable.height() - 1;
        assertEquals(expectedSize, items.size());
    }

    @When("The first user creates an order with the following items:")
    public void the_first_user_adds_the_following_items_to_his_order(DataTable dataTable) {
        this.orderUser1 = new Order(restaurant, user1);
        this.groupOrder.addOrder(orderUser1);
        this.user1.setCurrentOrder(orderUser1);
        for (int i = 1; i < dataTable.height(); i++) {
            String itemName = dataTable.row(i).getFirst();
            MenuItem item = restaurant.getMenu().findItemByName(itemName);
            this.orderUser1.addItem(item);
        }
        assertEquals(dataTable.height() - 1, this.orderUser1.getItems().size());
    }

    @And("The first user validates his order")
    public void the_first_user_validates_his_order() {
        user1.setCurrentOrder(orderUser1);
        user1.validateOrder();
    }

    @Then("The order should be validated")
    public void theOrderShouldBeValidated() {
        assertEquals(OrderStatus.VALIDATED, orderUser1.getStatus());
        assertEquals(user1.getTransactions().getFirst().getOrder(), orderUser1);
    }

    @Given("The second user joins the group order")
    public void theSecondUserJoinsTheGroupCommand() {
        this.user2 = new CampusUser("user2@user", "password123", new OrderManager());
        this.user2.setCurrentGroupOrder(groupOrder);
        this.groupOrder.addUser(user2);
    }

    @When("The second user creates an order with the following items:")
    public void theSecondUserCreatesAnOrderWithTheFollowingItems(DataTable dataTable) {
        this.orderUser2 = new Order(restaurant, user2);
        this.groupOrder.addOrder(orderUser2);
        this.user2.setCurrentOrder(orderUser2);
        for (int i = 1; i < dataTable.height(); i++) {
            String itemName = dataTable.row(i).getFirst();
            MenuItem item = restaurant.getMenu().findItemByName(itemName);
            orderUser2.addItem(item);
        }
        assertEquals(dataTable.height() - 1, orderUser2.getItems().size());
    }

    @And("The second user validates his order")
    public void theSecondUserValidatesHisOrder() {
        user2.setCurrentOrder(orderUser2);
        user2.validateOrder();
    }

    @Then("The second user's order should be validated")
    public void theSecondUsersOrderShouldBeValidated() {
        assertEquals(orderUser2, user2.getTransactions().getFirst().getOrder());
        assertEquals(OrderStatus.VALIDATED, user2.getCurrentOrder().getStatus());
    }

    @Given("The third user joins the group order")
    public void theThirdUserJoinsTheGroupOrder() {
        user3 = new CampusUser("user3@user", "password", new OrderManager());
        groupOrder.addUser(user3);
        user3.setCurrentGroupOrder(groupOrder);
    }

    @Given("The third user creates an order with the following items:")
    public void theThirdUserAddsTheFollowingItemsToHisOrder(DataTable dataTable) {
        this.orderUser3 = new Order(restaurant, user3);
        this.groupOrder.addOrder(orderUser3);
        user3.setCurrentOrder(orderUser3);
        for (int i = 1; i < dataTable.height(); i++) {
            String itemName = dataTable.row(i).getFirst();
            MenuItem item = restaurant.getMenu().findItemByName(itemName);
            orderUser3.addItem(item);
        }
        assertEquals(dataTable.height() - 1, orderUser2.getItems().size());
        assertEquals(dataTable.height() - 1, user3.getCurrentOrder().getItems().size());
    }

    @And("The third user validates his order")
    public void theThirdUserValidatesHisOrder() {
        user3.validateOrder();
    }

    @And("The third user validates the group order")
    public void theThirdUserValidatesTheGroupOrder() {
        this.user3.getOrderManager().validateGroupOrder(groupOrder);
    }

    @Then("The third user's order and the group order should be validated")
    public void theThirdUserSOrderAndTheGroupOrderShouldBeValidated() {
        assertEquals(orderUser3, user3.getTransactions().getFirst().getOrder());
        assertEquals(OrderStatus.VALIDATED, orderUser3.getStatus());
        assertEquals(OrderStatus.VALIDATED, groupOrder.getStatus());
    }

    //////////////// SCENARIO 2 ////////////////
    @When("The second user tries to bypass the restaurant selection by delivery time")
    public void theSecondUserTriesToBypassTheRestaurantSelectionByDeliveryTime() {
    }

    @And("He selects the restaurant {string} that cannot prepare items in time")
    public void heSelectsTheRestaurantThatCannotPrepareItemsInTime(String restaurantName) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(restaurantName).getFirst();
        orderUser2 = new Order(restaurant, user2);
        try {
            this.groupOrder.addOrder(orderUser2);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
            this.itemAddException = (IllegalStateException) e;
        }
    }

    @Given("the second user selects the restaurant {string}")
    public void the_second_user_selects_the_restaurant(String string) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
    }

    @When("The second user tries to create an order with the following items:")
    public void theSecondUserTriesToCreateAnOrderWithTheFollowingItems(DataTable dataTable) {
        this.orderUser2 = new Order(restaurant, user2);
        this.groupOrder.addOrder(orderUser2);
        this.user2.setCurrentOrder(orderUser2);
        for (int i = 1; i < dataTable.height(); i++) {
            String itemName = dataTable.row(i).getFirst();
            MenuItem item = restaurant.getMenu().findItemByName(itemName);
            try {
                orderUser2.addItem(item);
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
                this.itemAddSlotException = (IllegalArgumentException) e;
            }
        }
        assertEquals(0, orderUser2.getItems().size());
    }

    @Then("the system rejects the item and displays an error: {string}")
    public void theSystemRejectsTheItemAndDisplaysAnError(String error) {
        assertEquals(error, (this.itemAddException == null ? this.itemAddSlotException : this.itemAddException).getMessage());
    }
    //////////////// SCENARIO 4 ////////////////
    @And("The first user validates the group order")
    public void theFirstUserValidatesTheGroupOrder() {
        this.user1.getOrderManager().validateGroupOrder(groupOrder);
    }


    @Then("The first user's order and the group order should be validated")
    public void theFirstUserSOrderAndTheGroupOrderShouldBeValidated() {
        assertEquals(orderUser1.getStatus(), OrderStatus.VALIDATED);
        assertEquals(groupOrder.getStatus(), OrderStatus.VALIDATED);
    }


    @Given("The third user tries to join the group order")
    public void the_third_user_tries_to_join_the_group_order() {
        user3 = new CampusUser("user@user", "password", new OrderManager());
        try {
            groupOrder.addUser(user3);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
            this.userJoinsValidatedGroupOrderException = (IllegalStateException)e;
        }
    }

    @Then("the system rejects the third user and displays an error: {string}")
    public void the_system_rejects_the_third_user_and_displays_an_error(String string) {
        assertEquals(string, this.userJoinsValidatedGroupOrderException.getMessage());
    }

    @Then("The first user validates his order and validates the group order")
    public void the_first_user_validates_his_order_and_validates_the_group_order() {
        user1.validateOrderAndGroupOrder();
    }

    @Given("He searches restaurants that are open and can prepare items in time for it's individual order and should see:")
    public void he_searches_restaurants_that_are_open_and_can_prepare_items_in_time_for_it_s_individual_order_and_should_see(io.cucumber.datatable.DataTable dataTable) {
        List<Restaurant> restaurants = RestaurantServiceManager.getInstance().searchRestaurantByDeliveryTime(((IndividualOrder) user1.getCurrentOrder()).getDeliveryDetails().getDeliveryTime());
        int expectedSize = dataTable.height() - 1;
        assertEquals(expectedSize, restaurants.size());
    }

    @Given("He selects the restaurant {string} and see the items compatible with the individual order delivery time preparation:")
    public void he_selects_the_restaurant_and_see_the_items_compatible_with_the_individual_order_delivery_time_preparation(String string, io.cucumber.datatable.DataTable dataTable) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
        List<MenuItem> items = RestaurantServiceManager.getInstance().searchItemsByDeliveryTime(restaurant, ((IndividualOrder) user1.getCurrentOrder()).getDeliveryDetails().getDeliveryTime());
        int expectedSize = dataTable.height() - 1;
        assertEquals(expectedSize, items.size());
    }

    @Given("the first user creates an individual order with the restaurant {string} and with delivery location {string} and delivery time of {int}:{int} PM")
    public void theFirstUserCreatesAnIndividualOrderWithTheRestaurantAndWithDeliveryLocationAndDeliveryTimeOfPM(String restau, String location, int hours, int minutes) {
        user1 = new CampusUser("user1@user", "password123", new OrderManager());
        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().findLocationByName(location);
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, TimeUtils.getNow().withHour(hours).withMinute(minutes));
        this.restaurant = RestaurantServiceManager.getInstance().searchByName(restau).getFirst();
        this.individualOrderUser1 = new IndividualOrder(restaurant, deliveryDetails, user1);
    }

    @Given("The first user adds the following items to his individual order:")
    public void the_first_user_adds_the_following_items_to_his_individual_order(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 1; i < dataTable.height(); i++) {
            String itemName = dataTable.row(i).getFirst();
            MenuItem item = restaurant.getMenu().findItemByName(itemName);
            individualOrderUser1.addItem(item);
        }
        user1.setCurrentOrder(individualOrderUser1);
        assertEquals(dataTable.height() - 1, individualOrderUser1.getItems().size());
    }

    @Then("The first user validates his individual order")
    public void the_first_user_validates_his_individual_order() {
        user1.validateOrder();
        assertEquals(OrderStatus.VALIDATED, individualOrderUser1.getStatus());
        assertEquals(0, restaurant.getPendingOrders().size());
        assertEquals(1, restaurant.getOrdersHistory().size());
    }
}
