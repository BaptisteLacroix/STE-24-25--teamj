Feature: User places an order from a campus restaurant

  Scenario: Registered user places an order for multiple menu items
    Given the user is registered
    And the user has selected the restaurant "Pizza House"
    And the menu of "Pizza House" includes "Margherita Pizza" and "French Fries"
    When the user adds "Margherita Pizza" and "French Fries" to their order
    And sets a delivery time for 12:00 PM today
    And places the order
    Then the order is placed successfully
