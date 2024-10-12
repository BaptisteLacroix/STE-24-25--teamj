Feature: Update restaurant opening hours and menu offerings

  As a Restaurant Manager
  I want to update my opening hours and menu offerings
  So that I can inform people who are ordering about the current capacity and our opening time

  Background:
    Given "Jeanne", a restaurant manager of the "Chicken Burger" restaurant
    And the restaurant has a menu with the following items:
      | itemName | description  | prepTime | price | capacity |
      | BigMac   | Burger       | 120      | 5     | 2        |
      | Fries    | Crispy fries | 60       | 3     | 4        |


  Scenario: Update opening hours
    Given Jeanne wants to set its restaurant opening hours
    When the restaurant manager sets the hours from "2024-10-08 13:00" to "2024-10-08 15:00"
    Then the opening hours should be "2024-10-08 13:00" to "2024-10-08 15:00"

  Scenario: Update the price of a menu item
    Given Jeanne wants to update the price of "BigMac"
    When the restaurant manager updates the price of "BigMac" to 6
    Then the price of "BigMac" should be 6

  Scenario: Update the preparation time of a menu item
    Given Jeanne wants to update the preparation time of "Fries"
    When the restaurant manager updates the preparation time of "Fries" to 45
    Then the preparation time of "Fries" should be 45 seconds

  Scenario: Update the capacity of a menu item
    Given Jeanne wants to update the capacity of "BigMac"
    When Jeanne updates the preparation time of "BigMac" to 5
    Then the capacity of "BigMac" should be 5