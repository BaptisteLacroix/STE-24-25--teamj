Feature: Update restaurant opening hours and menu offerings

  As a Restaurant Manager, I want to update my opening hours and menu offerings
  so that I can inform people who are ordering the price and the preparation time of a meal, our capacity and our opening time.

  Background:
    Given "Jeanne", a restaurant manager of the "Chicken Burger" restaurant opened from "2024-10-18 12:00" to "2024-10-18 14:00"
    And the restaurant has a menu with the following items:
      | itemName | prepTime | price |
      | BigMac   | 120      | 5     |
      | Fries    | 60       | 3     |


  Scenario: Update opening hours
    Given Jeanne wants to set its restaurant opening hours: opening at "2024-10-18 13:00" and closing at "2024-10-18 15:00"
    When the restaurant manager sets the hours from "2024-10-18 13:00" to "2024-10-18 15:00"
    Then the opening hours should be "2024-10-18 13:00" to "2024-10-18 15:00"

  Scenario: Update the price of a menu item
    Given Jeanne wants to update the price of "BigMac"
    When the restaurant manager updates the price of "BigMac" to 6.0
    Then the price of "BigMac" should be 6.0

  Scenario: Update the preparation time of a menu item
    Given Jeanne wants to update the preparation time of "Fries"
    When the restaurant manager updates the preparation time of "Fries" to 45
    Then the preparation time of "Fries" should be 45 seconds

  Scenario: Update the number of personnel for a slot
    Given Jeanne wants to update the number of personnel for the slot starting at "2024-10-18 12:30"
    When the restaurant manager updates the personnel for this slot to 6
    Then the number of personnel for the slot starting at "2024-10-18 12:30" should be 6

  Scenario: Allocate personnel when the restaurant is closed
    Given Jeanne wants to allocate 4 personnel to the slot starting at "2024-10-18 22:00"
    When Jeanne tries to allocate 4 personnel to the slot starting at "2024-10-18 22:00"
    Then Jeanne will see that it is impossible with starting slot at "2024-10-18 22:00"

  Scenario: Calculate production capacity
    Given Jeanne wants to get the maximum capacity of a slot
    When Jeanne allocates 5 personnel to te slot starting at "2024-10-18 12:30"
    Then the maximum capacity for the slot should be 9000 seconds

