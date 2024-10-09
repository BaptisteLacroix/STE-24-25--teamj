Feature: User places an order from a campus restaurant

  Scenario: Registered user places an order for multiple menu items
    Given the user is registered
    And the user has selected the restaurant "Pizza House"
    And the menu of "Pizza House" includes "Margherita Pizza" and "French Fries"
    When the user adds "Margherita Pizza" and "French Fries" to their order
    And places the order
    Then the order is placed successfully

  Scenario: Registered user places an order for a single menu item
    Given the user is registered
    And the user has selected the restaurant "Pizza House"
    And the menu of "Pizza House" includes "Margherita Pizza" and "French Fries"
    When the user adds "Margherita Pizza" to their order
    And places the order
    Then the order is placed successfully

  Scenario: Registered user tries to place an order adding a menu item that is not available
    Given the user is registered
    And the user has selected the restaurant "Pizza House"
    And the menu of "Pizza House" includes "Margherita Pizza" and "French Fries"
    When the user tries to add "Four Cheese Pizza" to their order
    Then the user get an error message "Four Cheese Pizza is not in the menu"
    And the order is not placed

  Scenario: Registered user tries to place an order without adding any menu items
    Given the user is registered
    And the user has selected the restaurant "Pizza House"
    And the menu of "Pizza House" includes "Margherita Pizza" and "French Fries"
    When the user tries to place the order without adding any menu items
    Then the user get an error message "You must add at least one item to your order"
    And the order is not placed
