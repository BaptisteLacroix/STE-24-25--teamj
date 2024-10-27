Feature: Restaurant capacity management

  As a restaurant,  I want to deal with the the coming orders so that I can inform people if I can take their order or not.

  Background:
    Given a restaurant "Chicken Burger" opened from "2024-10-18 12:00" to "2024-10-18 14:00" which has a menu with following items:
      | itemName | prepTime | price |
      | BigMac   | 120      | 5     |
      | Fries    | 60       | 3     |

  Scenario: Update slot capacity when adding a new item
    Given the restaurant receives an order with a "BigMac" at "2024-10-18 12:00"
    When the restaurant adds "BigMac" to this slot
    Then the new current capacity of this slot should be 120
    And the available capacity should be 1680

  Scenario: Adding an item to a full slot
    Given the restaurant has 1 personnel for each slot
    And the restaurant has a slot with full capacity at "2024-10-18 12:00"
    When the restaurant adds an item "BigMac" at "2024-10-18 12:00"
    Then it should be add to the next slot at "2024-10-18 12:30" with a capacity of 120

  Scenario: Adding an item with no slots available
    Given the restaurant has 1 personnel for each slot
    And the restaurant is full from its opening to its closing
    When the restaurant adds an item "BigMac" at "2024-10-18 12:00"
    Then the item is not added by the restaurant



