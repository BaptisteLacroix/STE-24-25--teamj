Feature: Place orders from campus restaurants
  As a registered campus user
  I want to place an order for menu items from campus restaurants
  So that I can receive my food delivered on time

  Background:
    Given the following users are registered:
      | username | password | accountBalance |
      | user1    | pass123  | 50.00          |
      | user2    | pass456  | 30.00          |
    And the following restaurants are available:
      | restaurantName  | openingTime | closingTime |
      | Campus Deli     | 10:00 AM    | 6:00 PM     |
      | Pizzeria Italia | 11:00 AM    | 10:00 PM    |
    And the following menu items exist for the restaurant "Campus Deli":
      | itemName | price |
      | Sandwich | 5.00  |
      | Salad    | 3.50  |
    And the following menu items exist for the restaurant "Pizzeria Italia":
      | itemName | price |
      | Pizza    | 8.00  |
      | Pasta    | 7.00  |

  Scenario: Successfully placing an order from a campus restaurant
    Given I am logged in as "user1" with password "pass123"
    When I select the restaurant "Campus Deli"
    And I add "Sandwich" to my order
    And I proceed to checkout
    Then my order is placed successfully
    And I should see the confirmation message "Your order has been placed"

  Scenario: Attempt to order from a closed restaurant
    Given I am logged in as "user1" with password "pass123"
    When I select the restaurant "Pizzeria Italia"
    And I try to place an order outside opening hours at 9:00 AM
    Then I should see the error message "Restaurant is currently closed"

  Scenario: Ordering multiple items from a restaurant
    Given I am logged in as "user1" with password "pass123"
    When I select the restaurant "Campus Deli"
    And I add "Sandwich" and "Salad" to my order
    And I proceed to checkout
    Then my order is placed successfully
    And I should see the confirmation message "Your order has been placed for multiple items"
