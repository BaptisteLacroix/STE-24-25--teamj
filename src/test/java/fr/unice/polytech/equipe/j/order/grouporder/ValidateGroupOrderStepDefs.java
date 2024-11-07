package fr.unice.polytech.equipe.j.order.grouporder;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.grouporder.GroupOrder;
import fr.unice.polytech.equipe.j.grouporder.GroupOrderProxy;
import fr.unice.polytech.equipe.j.grouporder.GroupOrderRepository;
import fr.unice.polytech.equipe.j.grouporder.IGroupOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.restaurant.backend.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.backend.menu.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.backend.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.user.CampusUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;
import org.junit.Assert;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ValidateGroupOrderStepDefs {
    private CampusUser groupOrderCreator;
    private CampusUser groupOrderJoiner;
    private IGroupOrder groupOrder;
    private IRestaurant restaurant;
    private OrderManager orderManager;
    private Order orderUser1;
    private Order orderUser2;

    @Before
    public void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
    }

    @Given("[ValidateGroupOrder]the user is registered")
    public void validate_group_order_the_user_is_registered() {;
        groupOrderCreator = new CampusUser("test1", 0);
        groupOrderJoiner = new CampusUser("test2", 0);
    }

    @Given("[ValidateGroupOrder]the user creates a group order with delivery location {string} and delivery time of {int}:{int} PM")
    public void validate_group_order_the_user_creates_a_group_order_with_delivery_location_and_delivery_time_of_pm(String string, Integer int1, Integer int2) {
        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().findLocationByName(string);
        LocalDateTime deliveryTime = TimeUtils.getNow().withHour(int1).withMinute(int2);
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, deliveryTime);

        this.groupOrder = new GroupOrderProxy(new GroupOrder(deliveryDetails));
        this.groupOrder.addUser(this.groupOrderCreator);
        assertNotNull(groupOrder);
        assertEquals(1, groupOrder.getUsers().size());
        assertEquals(groupOrderCreator, groupOrder.getUsers().getFirst());
        GroupOrderRepository.getInstance().addGroupOrder(groupOrder);
    }

    @Given("[ValidateGroupOrder]the user receives a group order identifier")
    public void validate_group_order_the_user_receives_a_group_order_identifier() {
        assertNotNull(groupOrder.getGroupOrderId());
    }

    @Given("[ValidateGroupOrder]the user adds the following items to his order from the restaurant {string}:")
    public void validate_group_order_the_user_adds_the_following_items_to_his_order_from_the_restaurant(String string, io.cucumber.datatable.DataTable dataTable) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
        orderManager = new OrderManager(restaurant, groupOrder);
        this.orderUser1 = new Order(restaurant, groupOrderCreator);
        for (int i = 1; i < dataTable.height(); i++) {
            String itemName = dataTable.row(i).getFirst();
            MenuItem item = restaurant.getMenu().findItemByName(itemName);
            orderManager.addItemToOrder(
                    orderUser1,
                    item
            );
            assertEquals(itemName, orderUser1.getItems().get(i - 1).getName());
        }
        assertEquals(2, orderUser1.getItems().size());
    }

    @When("[ValidateGroupOrder]the user validates his order")
    public void validate_group_order_the_user_validates_his_order() {
        orderManager.validateOrder(orderUser1);
    }

    @Then("[ValidateGroupOrder]the user validate it's order and the group order")
    public void the_user_validate_its_order_and_the_group_order() {
        orderManager.validateOrder(orderUser1);
        orderManager.validateGroupOrder(groupOrderCreator);
    }

    @Given("[ValidateGroupOrder]the user creates a group order with delivery location {string}")
    public void validate_group_order_the_user_creates_a_group_order_with_delivery_location(String string) {
        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().findLocationByName(string);
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, null);

        this.groupOrder = new GroupOrderProxy(new GroupOrder(deliveryDetails));
        this.groupOrder.addUser(groupOrderCreator);
        assertNotNull(groupOrder);
        assertEquals(1, groupOrder.getUsers().size());
        assertEquals(groupOrderCreator, groupOrder.getUsers().getFirst());
        GroupOrderRepository.getInstance().addGroupOrder(groupOrder);
    }

    @Then("[ValidateGroupOrder]the user validates his order and validates the group order and specifies a delivery time of {int}:{int} PM")
    public void validate_group_order_the_user_validate_the_group_order_and_the_group_order_delivery_time_is_pm(Integer int1, Integer int2) {
        LocalDateTime deliveryTime = TimeUtils.getNow().withHour(int1).withMinute(int2);
        orderManager.validateOrder(orderUser1);
        orderManager.validateGroupOrder(groupOrderCreator, deliveryTime);
        Assert.assertEquals(OrderStatus.VALIDATED, groupOrder.getStatus());
        assertTrue(groupOrder.getDeliveryDetails().getDeliveryTime().isPresent());
        assertEquals(deliveryTime, groupOrder.getDeliveryDetails().getDeliveryTime().get());
    }

    @Then("[ValidateGroupOrder]the user validates his order and validates the group order and specifies a delivery time of {int}:{int} PM that is not compatible")
    public void validate_group_order_the_user_validate_the_group_order_and_the_group_order_delivery_time_is_pm_that_is_not_compatible(Integer int1, Integer int2) {
        LocalDateTime deliveryTime = TimeUtils.getNow().withHour(int1).withMinute(int2);
        assertThrows(UnsupportedOperationException.class, () -> orderManager.validateGroupOrder(groupOrderCreator, deliveryTime));
        assertNotEquals(OrderStatus.VALIDATED, groupOrder.getStatus());
        assertFalse(groupOrder.getDeliveryDetails().getDeliveryTime().isPresent());
    }

    @Then("[ValidateGroupOrder]the user receives an error message {string}")
    public void validate_group_order_the_user_receives_an_error_message(String string) {
        System.out.println(string);
    }

    @Then("[ValidateGroupOrder]the group order delivery time is not set")
    public void validate_group_order_the_group_order_delivery_time_is_not_set() {
        assertTrue(groupOrder.getDeliveryDetails().getDeliveryTime().isEmpty());
    }

    @When("[ValidateGroupOrder]another user joins the group order")
    public void validate_group_order_another_user_joins_the_group_order() {
        assertEquals(groupOrder, GroupOrderRepository.getInstance().findGroupOrderById(groupOrder.getGroupOrderId()));
        groupOrder.addUser(groupOrderJoiner);
    }

    @When("[ValidateGroupOrder]the other user adds the following items to his order from the restaurant {string}:")
    public void validate_group_order_the_other_user_adds_the_following_items_to_his_order(String string, io.cucumber.datatable.DataTable dataTable) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
        this.orderManager = new OrderManager(restaurant, groupOrder);
        this.orderUser2 = new Order(restaurant, groupOrderJoiner);
        for (int i = 1; i < dataTable.height(); i++) {
            MenuItem item = restaurant.getMenu().findItemByName(dataTable.row(i).get(0));
            orderManager.addItemToOrder(orderUser2, item);
            assertEquals(item.getName(), orderUser2.getItems().get(i - 1).getName());
        }
        assertEquals(1, orderUser2.getItems().size());
        assertEquals(2, groupOrder.getOrders().size());
    }

    @When("[ValidateGroupOrder]the other user validates his order and validates the group order")
    public void validate_group_order_the_other_user_validates_his_order_and_validates_the_group_order() {
        orderManager.validateOrder(orderUser2);
        orderManager.validateGroupOrder(groupOrderJoiner);
    }

    @Then("[ValidateGroupOrder]the group order is validated")
    public void validate_group_order_the_group_order_is_validated() {
        assertEquals(OrderStatus.VALIDATED, groupOrder.getStatus());
    }
}
