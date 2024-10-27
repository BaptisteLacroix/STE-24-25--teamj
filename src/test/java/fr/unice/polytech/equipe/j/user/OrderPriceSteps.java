package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.restaurant.*;
import fr.unice.polytech.equipe.j.restaurant.orderpricestrategy.FreeItemFotNItemsOrderPriceStrategy;
import fr.unice.polytech.equipe.j.restaurant.orderpricestrategy.KPercentForNOrderPriceStrategy;
import fr.unice.polytech.equipe.j.stepdefs.backend.Utils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class OrderPriceSteps {
    Restaurant restaurant;
    private List<Order> indivOrders = new ArrayList<>();
    private double reductionPercentage;
    private int orderReductionNumber;
    private Order orderWithMoreThatNItems;
    private Order orderWithLessThatNItems;

    @Given("the following restaurant exists:")
    public void createRestaurants(DataTable dataTable) {
        List<String> row = dataTable.row(1);

        this.restaurant = new Restaurant(
                row.getFirst(),
                LocalDateTime.of(2024, 10, 1, 9, 0),
                LocalDateTime.of(2024, 10, 1, 21, 0),
                Utils.createMenuFromString(row.getLast())
        );
    }

    @Given("The restaurant wants to give a {int}% discount every {int} command for each user")
    public void setStrategy(int reductionPercentage, int orderReductionNumber) {
        this.reductionPercentage = reductionPercentage;
        this.orderReductionNumber = orderReductionNumber;
        this.restaurant.setOrderPriceStrategy(
                new KPercentForNOrderPriceStrategy(
                        this.reductionPercentage,
                        this.orderReductionNumber
                )
        );
    }

    @When("Any user makes any multiples of {int} orders")
    public void createOrder(int orderReductionNumber) {
        CampusUser user1 = new CampusUser("mail", "psw", new OrderManager());
        for (int i = 0; i < orderReductionNumber*30; i++) {
            var order = new Order(restaurant, user1);
            for (MenuItem item : restaurant.getMenu().getItems()) {
                order.addItem(item);
            }
            this.indivOrders.add(order);
        }
    }

    @Then("The order should see its price reduced by the corresponding percentage")
    public void checkPrice() {
        for (int i = 1; i <= this.indivOrders.size(); i++) {
            var order = indivOrders.get(i-1);
            // manually calculate order totalPrice and reduced price
            Map<MenuItem, Double> prices = order.getItems().stream().collect(Collectors.toMap((item)->item, MenuItem::getPrice));
            double totalPrice = prices.values().stream().mapToDouble(Double::doubleValue).sum();
            double reducedPrice = i%this.orderReductionNumber == 0 ? totalPrice - (totalPrice * this.reductionPercentage/100.0) : totalPrice;
            OrderPrice processedOrderPrice = restaurant.processOrderPrice(order);
            restaurant.addOrderToHistory(order);
            // compare cumulative price of menuItems with processed total orderPrice
            if(processedOrderPrice.totalPrice() != reducedPrice){
                System.out.println("temp");
            }
            assertEquals(processedOrderPrice.totalPrice(), reducedPrice);
        }
    }

    @Given("The restaurant wants to give a free item for every order with more than {int} items")
    public void theRestaurantWantsToGiveAFreeItemForEveryOrderWithMoreThatNItems(int n) {
        this.restaurant.setOrderPriceStrategy(new FreeItemFotNItemsOrderPriceStrategy(n));
    }

    @When("Any user creates an order with more that {int} items")
    public void anyUserCreatesAnOrderWithMoreThatNItems(int n) {
        CampusUser user1 = new CampusUser("userEmail2", "psw2", new OrderManager());
        var order = new Order(restaurant, user1);
        for (MenuItem item : restaurant.getMenu().getItems().subList(0, n+1)) {
            order.addItem(item);
        }
        this.orderWithMoreThatNItems = order;
    }

    @Then("The less expensive item from the order should have a price of {double}")
    public void theLessExpensiveItemFromTheOrderShouldHaveAPriceOf(double price) {
        // get first
        var lowestItem = this.orderWithMoreThatNItems.getItems().stream()
                .min(Comparator.comparingDouble(MenuItem::getPrice)).orElseThrow();

        OrderPrice processOrderPrice = restaurant.processOrderPrice(this.orderWithMoreThatNItems);
        assertEquals(processOrderPrice.newPrices().get(lowestItem), price);
    }

    @When("Any user creates an order with less than {int} items")
    public void anyUserCreatesAnOrderWithMoreLessItems(int n) {
        CampusUser user1 = new CampusUser("userEmail3", "psw3", new OrderManager());
        var order = new Order(restaurant, user1);
        for (MenuItem item : restaurant.getMenu().getItems().subList(0, n-1)) {
            order.addItem(item);
        }
        this.orderWithLessThatNItems = order;
    }

    @Then("The less expensive item from the order should not have a price of {int}")
    public void theLessExpensiveItemFromTheOrderShouldNotHaveAPriceOf(double price) {
        // get first
        var lowestItem = this.orderWithLessThatNItems.getItems().stream()
                .min(Comparator.comparingDouble(MenuItem::getPrice)).orElseThrow();

        OrderPrice processOrderPrice = restaurant.processOrderPrice(this.orderWithLessThatNItems);
        assertNotEquals(processOrderPrice.newPrices().get(lowestItem), price);
    }
}
