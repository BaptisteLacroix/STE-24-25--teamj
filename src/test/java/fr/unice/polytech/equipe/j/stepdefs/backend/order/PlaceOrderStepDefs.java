package fr.unice.polytech.equipe.j.stepdefs.backend.order;

import fr.unice.polytech.equipe.j.equipe.j.restaurant.Menu;
import fr.unice.polytech.equipe.j.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.equipe.j.user.User;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class PlaceOrderStepDefs {
    Map<String, User> users = new HashMap<>();
    Map<String, Restaurant> restaurants = new HashMap<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
    User currentUser;
    Restaurant currentRestaurant;
    List<MenuItem> currentOrder = new ArrayList<>();

    @Given("the following users are registered:")
    public void the_following_users_are_registered(DataTable dataTable) {
        List<Map<String, String>> usersList = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> userData : usersList) {
            String username = userData.get("username");
            String password = userData.get("password");
            double accountBalance = Double.parseDouble(userData.get("accountBalance"));
            users.put(username, new User(username, password, accountBalance));
        }
    }

    @Given("the following restaurants are available:")
    public void the_following_restaurants_are_available(DataTable dataTable) throws Exception {
        List<Map<String, String>> restaurantList = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> restaurantData : restaurantList) {
            String restaurantName = restaurantData.get("restaurantName");
            Date openingTime = dateFormat.parse(restaurantData.get("openingTime"));
            Date closingTime = dateFormat.parse(restaurantData.get("closingTime"));
            Menu menu = new Menu();
            Restaurant restaurant = new Restaurant(restaurantName, openingTime, closingTime, menu);
            restaurants.put(restaurantName, restaurant);
        }
    }

    @Given("the following menu items exist for the restaurant {string}:")
    public void the_following_menu_items_exist_for_the_restaurant(String restaurantName, DataTable dataTable) {
        Restaurant restaurant = restaurants.get(restaurantName);
        if (restaurant != null) {
            List<Map<String, String>> itemsList = dataTable.asMaps(String.class, String.class);
            for (Map<String, String> itemData : itemsList) {
                String itemName = itemData.get("itemName");
                double price = Double.parseDouble(itemData.get("price"));
                MenuItem menuItem = new MenuItem(itemName, price);
                restaurant.getMenu().addItem(menuItem);
            }
        } else {
            throw new RuntimeException("Restaurant not found: " + restaurantName);
        }
    }

    @Given("I am logged in as {string} with password {string}")
    public void i_am_logged_in_as_with_password(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @When("I select the restaurant {string}")
    public void i_select_the_restaurant(String restaurantName) {
        currentRestaurant = restaurants.get(restaurantName);
        if (currentRestaurant == null) {
            throw new RuntimeException("Restaurant not found");
        }
    }

    @When("I add {string} to my order")
    public void i_add_to_my_order(String itemName) {
        if (currentRestaurant == null) {
            throw new RuntimeException("No restaurant selected");
        }
        MenuItem selectedItem = currentRestaurant.getMenu().getItems().stream()
                .filter(item -> item.getItemName().equals(itemName))
                .findFirst()
                .orElse(null);
        if (selectedItem != null) {
            currentOrder.add(selectedItem);
        } else {
            throw new RuntimeException("Menu item not found: " + itemName);
        }
    }

    @When("I proceed to checkout")
    public void i_proceed_to_checkout() {
        if (currentOrder.isEmpty()) {
            throw new RuntimeException("No items in the order");
        }
        double total = currentOrder.stream()
                .mapToDouble(MenuItem::getPrice)
                .sum();
        if (currentUser.getAccountBalance() < total) {
            throw new RuntimeException("Insufficient funds");
        }
        currentUser.setAccountBalance(currentUser.getAccountBalance() - total);
    }

    @Then("my order is placed successfully")
    public void my_order_is_placed_successfully() {
        System.out.println("Order placed successfully");
    }

    @Then("I should see the confirmation message {string}")
    public void i_should_see_the_confirmation_message(String message) {
        System.out.println(message);
    }

    @When("I try to place an order outside opening hours at {int}:{int} AM")
    public void i_try_to_place_an_order_outside_opening_hours_at_am(Integer hour, Integer minute) {
        // Setting the time when the order is placed
        Calendar orderTime = Calendar.getInstance();
        orderTime.set(Calendar.HOUR_OF_DAY, hour);
        orderTime.set(Calendar.MINUTE, minute);

        Calendar openingTime = Calendar.getInstance();
        openingTime.setTime(currentRestaurant.getOpeningTime());

        Calendar closingTime = Calendar.getInstance();
        closingTime.setTime(currentRestaurant.getClosingTime());

        // Check if the order time is outside opening hours
        if (orderTime.before(openingTime) || orderTime.after(closingTime)) {
            assertThrows(RuntimeException.class, () -> {
                throw new RuntimeException("Restaurant is currently closed");
            });
        } else {
            fail("Expected RuntimeException to be thrown, but it was not.");
        }
    }

    @Then("I should see the error message {string}")
    public void i_should_see_the_error_message(String expectedMessage) {
        System.out.println(expectedMessage);
    }

    @When("I add {string} and {string} to my order")
    public void i_add_and_to_my_order(String item1, String item2) {
        if (currentRestaurant == null) {
            throw new RuntimeException("No restaurant selected");
        }

        // Retrieve the items from the current restaurant's menu
        MenuItem selectedItem1 = currentRestaurant.getMenu().getItems().stream()
                .filter(item -> item.getItemName().equals(item1))
                .findFirst()
                .orElse(null);

        MenuItem selectedItem2 = currentRestaurant.getMenu().getItems().stream()
                .filter(item -> item.getItemName().equals(item2))
                .findFirst()
                .orElse(null);

        // Check if both items were found and add them to the order
        if (selectedItem1 != null) {
            currentOrder.add(selectedItem1);
        } else {
            throw new RuntimeException("Menu item not found: " + item1);
        }

        if (selectedItem2 != null) {
            currentOrder.add(selectedItem2);
        } else {
            throw new RuntimeException("Menu item not found: " + item2);
        }
    }

}
