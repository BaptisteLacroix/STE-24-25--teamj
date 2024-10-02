package fr.unice.polytech.equipe.j.stepdefs.backend.order;

import fr.unice.polytech.equipe.j.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.equipe.j.user.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PlaceOrderStepDefs {
    private User registerUser;
    private Restaurant restaurant;

    @Given("a register user name {string} and with an account balance of {double} with student an id")
    public void a_register_user_name_and_with_an_account_balance_of_with_student_id(String name, Double balance) {
        registerUser = new User(name, balance);
    }

    @Given("a restaurant named {string} with menu items:")
    public void a_restaurant_named_with_menu_items(String restaurantName, io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new io.cucumber.java.PendingException();
    }

    @When("{string} places an order for a {string}")
    public void places_an_order_for_a(String string, String string2) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the order total should be {double}")
    public void the_order_total_should_be(Double double1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the account balance should be {double}")
    public void the_account_balance_should_be(Double double1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}
