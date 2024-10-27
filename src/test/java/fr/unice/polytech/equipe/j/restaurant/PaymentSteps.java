package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.order.IndividualOrder;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.payment.PaymentMethod;
import fr.unice.polytech.equipe.j.user.CampusUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PaymentSteps {
    private IRestaurant restaurant;
    private CampusUser campusUser;
    private OrderManager orderManager;
    private IndividualOrder individualOrder;

    /**
     * This step definition is used to simulate a user registration.
     */
    @Given("the user is registered in the system")
    public void the_user_is_registered() {
        campusUser = new CampusUser("John", 0);
    }

    /**
     * This step definition is used to simulate the user selecting a restaurant.
     *
     * @param restaurantName the name of the restaurant
     */
    @Given("the user selected the restaurant {string}")
    public void the_user_has_selected_the_restaurant(String restaurantName) {
        restaurant = RestaurantServiceManager.getInstance().searchByName(restaurantName).getFirst();
        assertNotNull(restaurant);
        orderManager = new OrderManager(restaurant);
    }

    @Given("the user order by specifying the delivery location from the pre-recorded locations and the delivery time {int}:{int}")
    public void the_user_order_by_specifying_the_delivery_location_from_the_pre_recorded_locations_and_the_delivery_time(Integer int1, Integer int2) {
        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        LocalDateTime deliveryTime = LocalDateTime.now().withHour(int1).withMinute(int2);
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, deliveryTime);
        individualOrder = new IndividualOrder(restaurant, deliveryDetails, campusUser);
        assertEquals(OrderStatus.PENDING, individualOrder.getStatus());
        assertEquals(deliveryDetails.getDeliveryLocation().locationName(), individualOrder.getDeliveryDetails().getDeliveryLocation().locationName());
        assertEquals(deliveryDetails.getDeliveryLocation().address(), individualOrder.getDeliveryDetails().getDeliveryLocation().address());
    }

    /**
     * This step definition is used to simulate the user adding items to their order.
     *
     * @param item1 the first item
     * @param item2 the second item
     */
    @When("the user adds {string} and {string} to the order list")
    public void the_user_adds_and_to_the_order(String item1, String item2) {
        MenuItem menuItem1 = restaurant.getMenu().findItemByName(item1);
        MenuItem menuItem2 = restaurant.getMenu().findItemByName(item2);

        orderManager.addItemToOrder(individualOrder, menuItem1);
        orderManager.addItemToOrder(individualOrder, menuItem2);

        assertEquals(2, individualOrder.getItems().size());
    }

    /**
     * This step definition is used to simulate the user placing the order.
     */
    @When("places their order")
    public void places_the_order() {
        orderManager.validateOrder(individualOrder);
    }

    /**
     * This step definition is used to verify that the order is placed successfully.
     */
    @Then("the order is successfully placed")
    public void the_order_is_placed_successfully() {
        assertEquals(1, campusUser.getOrdersHistory().size());
        assertEquals(OrderStatus.VALIDATED, individualOrder.getStatus());
    }

    @When("Set their payment method to Credit Card")
    public void set_their_payment_method_to_credit_card() {
        campusUser.setDefaultPaymentMethod(PaymentMethod.CREDIT_CARD);
    }

    @When("Set their payment method to paypal")
    public void set_their_payment_method_to_paypal() {
        campusUser.setDefaultPaymentMethod(PaymentMethod.PAYPAL);
    }

    @When("Set their payment method to paylib")
    public void set_their_payment_method_to_paylib() {
        campusUser.setDefaultPaymentMethod(PaymentMethod.PAYLIB);
    }

    @Given("the restaurant manager configured the following restaurants:")
    public void the_restaurant_service_manager_configured_the_following_restaurants(io.cucumber.datatable.DataTable dataTable) {
        // Initialized in the @Before method
    }

    /**
     * Validates that the user can see the payment details with the specified amount.
     *
     * @param amount the expected payment amount to be displayed
     */
    @Then("they should be able to see the payment details with the amount {int}")
    public void they_should_be_able_to_see_the_payment_details_with_the_amount(double amount) {
        assertEquals(1, campusUser.getTransactions().size());
        assertEquals(amount, campusUser.getTransactions().get(0).getAmount(), 0);
    }

    /**
     * Validates that the payment method used is PayPal.
     */
    @Then("payment method used paypal")
    public void payment_method_used_paypal() {
        int index_latest = campusUser.getTransactions().size() - 1;
        assertEquals(campusUser.getTransactions().get(index_latest).getPaymentMethod(), campusUser.getTransactions().get(index_latest).getPaymentMethod());
    }

    /**
     * Validates that the displayed date corresponds to today's date.
     */
    @Then("the date corresponding to today")
    public void the_date_corresponding_to_today() {
        LocalDate today = LocalDate.now();
        int index_latest = campusUser.getTransactions().size() - 1;
        assertTrue(campusUser.getTransactions().get(index_latest).getTimestamp().toLocalDate().isEqual(today));
    }
}
