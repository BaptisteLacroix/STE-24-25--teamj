Feature: Update restaurant opening hours and menu offerings

  As a Restaurant Manager, I want to update my opening hours and menu offerings
  so that I can inform people who are ordering the price and the preparation time of a meal, the availability to prepare their order and our opening time.

  Background:
    Given "Jeanne", a restaurant manager of the "Chicken Burger" restaurant
    And the restaurant has a menu with the following items:
      | itemName | prepTime | price |
      | BigMac   | 120      | 5     |
      | Fries    | 60       | 3     |


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

  Scenario: Update the number of personnel for a slot
    Given the restaurant has slots from "2024-10-08 12:00" to "2024-10-08 14:00"
      | slotStart        | currentCapacity | maxCapacity | personnel |
      | 2024-10-08 12:00 | 0               | 7200        | 4         |
      | 2024-10-08 12:30 | 0               | 7200        | 4         |
      | 2024-10-08 13:00 | 0               | 7200        | 4         |
      | 2024-10-08 13:30 | 0               | 7200        | 4         |
    And Jeanne wants to update the number of personnel for the slot starting at "2024-10-08 13:00"
    When the restaurant manager updates the personnel for this slot to 6
    Then the number of personnel for the slot starting at "2024-10-08 13:00" should be 6


Scenario: Calculate production capacity
  Given a slot of 30 minutes is created
  When Jeanne allocates 5 personnel to this slot
  Then the maximum capacity for the slot should be 9000 seconds

Scenario: Update slot capacity when adding a new item
  Given the restaurant has slots from "2024-10-08 12:00" to "2024-10-08 14:00"
    | slotStart        | currentCapacity | maxCapacity | personnel |
    | 2024-10-08 12:00 | 0               | 7200        | 4         |
    | 2024-10-08 12:30 | 0               | 7200        | 4         |
    | 2024-10-08 13:00 | 0               | 7200        | 4         |
    | 2024-10-08 13:30 | 0               | 7200        | 4         |
  And the restaurant receives an order with a "BigMac" at "2024-10-08 12:00"
  When the restaurant adds "BigMac" to this slot
  Then the new current capacity of this slot should be 120
  And the available capacity should be 7080


Scenario: Adding an item to a full slot
  Given the restaurant has slots from "2024-10-08 12:00" to "2024-10-08 13:00"
    | slotStart        | currentCapacity | maxCapacity | personnel |
    | 2024-10-08 12:00 | 7100            | 7200        | 4         |
    | 2024-10-08 12:30 | 0               | 7200        | 4         |
  And the restaurant receives an order with a "BigMac" at "2024-10-08 12:00"
  When the restaurant adds "BigMac" to this slot
  Then it would be add to the next slot at "2024-10-08 12:30" with a capacity of 120

Scenario: Adding an item with no slots available
  Given the restaurant has slots from "2024-10-08 12:00" to "2024-10-08 13:00"
    | slotStart        | currentCapacity | maxCapacity | personnel |
    | 2024-10-08 12:00 | 7200            | 7200        | 4         |
    | 2024-10-08 12:30 | 7200            | 7200        | 4         |
  And the restaurant receives an order with a "BigMac" at "2024-10-08 12:00"
  When the restaurant adds "BigMac" to this slot
  Then the item is not added by the restaurant


Scenario: Allocate personnel when the restaurant is closed
  Given the restaurant has slots from "2024-10-08 12:00" to "2024-10-08 14:00"
    | slotStart        | currentCapacity | maxCapacity | personnel |
    | 2024-10-08 12:00 | 0               | 7200        | 4         |
    | 2024-10-08 12:30 | 0               | 7200        | 4         |
    | 2024-10-08 13:00 | 0               | 7200        | 4         |
    | 2024-10-08 13:30 | 0               | 7200        | 4         |
  And Jeanne wants to allocate 4 personnel to the slot starting at "2024-10-08 14:00"
  When Jeanne tries to allocate 4 personnel to this slot
  Then Jeanne will see that it is impossible
