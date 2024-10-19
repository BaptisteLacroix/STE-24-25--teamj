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
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OrderDeliveryDateStepDefs {
    private Optional<Order> order;
    private Clock clock;
    private Restaurant restaurant;
    private CampusUser user;
    private Optional<GroupOrder> groupOrder;

    @Before
    public void setup() {
        clock = Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris"));
    }

    @Given("[OrderDeliveryDate]the user is registered")
    public void order_delivery_date_the_user_is_registered() {
        user = new CampusUser("email@email", "pass", new OrderManager(clock));
    }

    @Given("[OrderDeliveryDate]the user has selected the restaurant {string}")
    public void order_delivery_date_the_user_has_selected_the_restaurant(String string) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
    }

    @Given("[OrderDeliveryDate]the user start and order by specifying the delivery location from the pre-recorded locations")
    public void order_delivery_date_the_user_start_and_order_by_specifying_the_delivery_location_from_the_pre_recorded_locations() {
        DeliveryLocation location = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        DeliveryDetails deliveryDetails = new DeliveryDetails(location, null);
        user.startIndividualOrder(restaurant, deliveryDetails);
        order = user.getCurrentOrder();
        assertTrue(order.isPresent());
        assertEquals(restaurant, order.get().getRestaurant());
        assertEquals(deliveryDetails, ((IndividualOrder) order.get()).getDeliveryDetails());
    }

    @When("[OrderDeliveryDate]the user adds {string} to their order")
    public void order_delivery_date_the_user_adds_to_their_order(String string) {
        assertTrue(order.isPresent());
        Optional<MenuItem> item = restaurant.getMenu().findItemByName(string);
        assertTrue(item.isPresent());
        order.get().addItem(item.get());
        assertEquals(1, order.get().getItems().size());
    }

    @Then("[OrderDeliveryDate]the possible delivery dates are updated to include the time it takes to prepare {string}")
    public void order_delivery_date_the_possible_delivery_dates_are_updated_to_include_the_time_it_takes_to_prepare(String string) {
        assertTrue(order.isPresent());
        assertTrue(((IndividualOrder) order.get()).getPossibleDeliveryTime().isPresent());
        Optional<MenuItem> item = restaurant.getMenu().findItemByName(string);
        assertTrue(item.isPresent());
        assertEquals(LocalDateTime.now(clock).plusSeconds(item.get().getPrepTime()), ((IndividualOrder) order.get()).getPossibleDeliveryTime().get());
    }

    @Given("[OrderDeliveryDate]the user creates a group order with delivery location {string} and delivery time of {int}:{int} PM")
    public void order_delivery_date_the_user_creates_a_group_order_with_delivery_location_and_delivery_time_of_pm(String string, Integer int1, Integer int2) {
        DeliveryLocation location = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        DeliveryDetails deliveryDetails = new DeliveryDetails(location, LocalDateTime.now(clock).withHour(int1).withMinute(int2));
        user.createGroupOrder(deliveryDetails);
        groupOrder = user.getCurrentGroupOrder();
        assertTrue(groupOrder.isPresent());
        assertEquals(deliveryDetails, groupOrder.get().getDeliveryDetails());
        assertTrue(user.getCurrentGroupOrder().isPresent());
        assertEquals(groupOrder.get().getGroupOrderId(), user.getCurrentGroupOrder().get().getGroupOrderId());
    }

    @Then("[OrderDeliveryDate]the system proposes the restaurants that are open and can prepare items in time:")
    public void order_delivery_date_the_system_proposes_the_restaurants_that_are_open_and_can_prepare_items_in_time(io.cucumber.datatable.DataTable dataTable) {
        assertTrue(groupOrder.isPresent());
        assertTrue(groupOrder.get().getDeliveryDetails().getDeliveryTime().isPresent());
        List<Restaurant> restaurantNames = RestaurantServiceManager.getInstance(clock).searchRestaurantByDeliveryTime(groupOrder.get().getDeliveryDetails().getDeliveryTime());
        assertEquals(dataTable.height() - 1, restaurantNames.size());

        List<Map<String, String>> expectedRestaurants = dataTable.asMaps();

        for (Map<String, String> expectedRestaurant : expectedRestaurants) {
            Restaurant restaurant = restaurantNames.stream()
                    .filter(r -> r.getRestaurantName().trim().equalsIgnoreCase(expectedRestaurant.get("Name").trim()))
                    .findFirst()
                    .orElse(null);
            assertNotNull("Restaurant not found: " + expectedRestaurant.get("Name"), restaurant);
        }
    }

    @Then("[OrderDeliveryDate]the user selects the restaurant {string} and sees the items compatible with the group order delivery time preparation:")
    public void order_delivery_date_the_user_selects_the_restaurant_and_sees_the_items_compatible_with_the_group_order_delivery_time_preparation(String string, io.cucumber.datatable.DataTable dataTable) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(string).getFirst();
        user.startSubGroupOrder(restaurant);
        order = user.getCurrentOrder();
        assertTrue(order.isPresent());
        assertEquals(restaurant, order.get().getRestaurant());
        List<Map<String, String>> expectedItems = dataTable.asMaps();
        for (Map<String, String> expectedItem : expectedItems) {
            assertNotNull(order.get().getRestaurant().getMenu().findItemByName(expectedItem.get("Menu Item")));
        }
    }

}
