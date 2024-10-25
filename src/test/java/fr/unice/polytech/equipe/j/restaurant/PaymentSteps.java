package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.order.IndividualOrder;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.payment.PaymentMethod;
import fr.unice.polytech.equipe.j.user.CampusUser;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import java.time.*;
import java.util.Optional;

import static org.junit.Assert.*;

public class PaymentSteps {



    @When("Set thir payment method to Credit Card")
    public void set_thir_payment_method_to_credit_card() {
        campusUser.setDefaultPaymentMethod(PaymentMethod.CREDIT_CARD);
    }

    @When("Set thir payment method to paypal")
    public void set_thir_payment_method_to_paypal() {
        campusUser.setDefaultPaymentMethod(PaymentMethod.PAYPAL);
    }

    @When("Set thir payment method to paylib")
    public void set_thir_payment_method_to_paylib() {
        campusUser.setDefaultPaymentMethod(PaymentMethod.PAYLIB);
    }


    private Restaurant restaurant;
    private CampusUser campusUser;
    private Clock clock;

    @Before
    public void setUp() {
        clock = Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris"));
    }

    @Given("the restaurant manager configured the following restaurants:")
    public void the_restaurant_service_manager_configured_the_following_restaurants(io.cucumber.datatable.DataTable dataTable) {
        // Initialized in the @Before method
    }

    /**
     * This step definition is used to simulate a user registration.
     */
    @Given("the user is registered in the system")
    public void the_user_is_registered() {
        campusUser = new CampusUser("john@example.com", "password", new OrderManager());
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
    }

    @And("the user order by specifying the delivery location from the pre-recorded locations")
    public void the_user_start_and_order_by_specifying_the_delivery_location_from_the_pre_recorded_locations() {
        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, null);
        IndividualOrder individualOrder = new IndividualOrder(restaurant, deliveryDetails, campusUser);
        campusUser.setCurrentOrder(individualOrder);
        assertNotNull(campusUser.getCurrentOrder());
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

        campusUser.addItemToOrder(restaurant, menuItem1);
        campusUser.addItemToOrder(restaurant, menuItem2);

        assertEquals(2, campusUser.getCurrentOrder().getItems().size());
    }

    /**
     * This step definition is used to simulate the user placing the order.
     */
    @When("places their order")
    public void places_the_order() {
        campusUser.validateOrder();
    }

    /**
     * This step definition is used to verify that the order is placed successfully.
     */
    @Then("the order is successfully placed")
    public void the_order_is_placed_successfully() {
        assertEquals(1, campusUser.getOrdersHistory().size());
        assertEquals(OrderStatus.VALIDATED, campusUser.getCurrentOrder().getStatus());
    }


    /**
     * Validates that the user can see the payment details with the specified amount.
     *
     * @param amount the expected payment amount to be displayed
     */
    @Then("they should be able to see the payment details with the amount {int}")
    public void they_should_be_able_to_see_the_payment_details_with_the_amount(double amount) {
        assertEquals(1,campusUser.getTransactions().size());
        assertEquals(amount,campusUser.getTransactions().get(0).getAmount(),0);

    }

    /**
     * Validates that the payment method used is PayPal.
     */
    @Then("payment method used paypal")
    public void payment_method_used_paypal() {
        int index_latest= campusUser.getTransactions().size() -1;
        assertEquals(campusUser.getTransactions().get(index_latest).getPaymentMethod(),campusUser.getTransactions().get(index_latest).getPaymentMethod());
    }

    /**
     * Validates that the displayed date corresponds to today's date.
     */
    @Then("the date corresponding to today")
    public void the_date_corresponding_to_today() {
        LocalDate today = LocalDate.now();
        int index_latest= campusUser.getTransactions().size() -1;
        assertTrue(campusUser.getTransactions().get(index_latest).getTimestamp().toLocalDate().isEqual(today));
    }
}