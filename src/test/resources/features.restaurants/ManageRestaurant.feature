Feature: Update restaurant opening hours and menu offerings

  As a Restaurant Manager
  I want to update my opening hours and menu offerings
  So that I can inform people who are ordering about the current capacity and our opening time

  Background:
    Given "Jeanne", a restaurant manager of the "Chicken Burger" restaurant
    And the restaurant has a menu with the following items:
      | itemName | prepTime | price | capacity |
      | BigMac   | 120      | 5     | 2        |
      | Fries    | 60       | 3     | 4        |


  Scenario: Update opening hours
    Given Jeanne wants to set its restaurant opening hours
    When the restaurant manager sets the hours from "2024-10-08 13:00" to "2024-10-08 15:00"
    Then the opening hours should be "2024-10-08 13:00" to "2024-10-08 15:00"

  Scenario: Update the price of a menu item
    Given Jeanne wants to update the price of "BigMac"
    When the restaurant manager updates the price of "BigMac" to 6.0
    Then the price of "BigMac" should be 6.0

  Scenario: Update the preparation time of a menu item
    Given Jeanne wants to update the preparation time of "Fries"
    When the restaurant manager updates the preparation time of "Fries" to 45
    Then the preparation time of "Fries" should be 45 seconds

  Scenario: Update the capacity of a menu item
    Given Jeanne wants to update the capacity of "BigMac"
    When Jeanne updates the preparation time of "BigMac" to 5
    Then the capacity of "BigMac" should be 5

  Scenario: Allocate production for a mixed order of Caesar Salad and Fries
    Given Jeanne wants to set a mixed production order
    When the restaurant manager allocates 4 personnel for the time slot from "12:00 PM" to "12:30 PM"
    And the restaurant manager processes 10 portions of "Caesar Salad"
    Then the remaining production capacity for "Fries" should be 90 portions