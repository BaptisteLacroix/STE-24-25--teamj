Feature: User places an order from a campus restaurant

  Scenario: Registered user places an order for multiple menu items
    Given [PlaceOrder]the user is registered
    And [PlaceOrder]the user has selected the restaurant "Pizza House"
    And [PlaceOrder]the menu of "Pizza House" includes "Margherita Pizza" and "French Fries"
    When [PlaceOrder]the user adds "Margherita Pizza" and "French Fries" to their order
    And [PlaceOrder]places the order
    Then [PlaceOrder]the order is placed successfully

  Scenario: Registered user places an order for a single menu item
    Given [PlaceOrder]the user is registered
    And [PlaceOrder]the user has selected the restaurant "Pizza House"
    And [PlaceOrder]the menu of "Pizza House" includes "Margherita Pizza" and "French Fries"
    When [PlaceOrder]the user adds "Margherita Pizza" to their order
    And [PlaceOrder]places the order
    Then [PlaceOrder]the order is placed successfully

  Scenario: Registered user tries to place an order adding a menu item that is not available
    Given [PlaceOrder]the user is registered
    And [PlaceOrder]the user has selected the restaurant "Pizza House"
    And [PlaceOrder]the menu of "Pizza House" includes "Margherita Pizza" and "French Fries"
    When [PlaceOrder]the user tries to add "Four Cheese Pizza" to their order
    Then [PlaceOrder]the user get an error message "Four Cheese Pizza is not in the menu"
    And [PlaceOrder]the order is not placed

  Scenario: Registered user tries to place an order without adding any menu items
    Given [PlaceOrder]the user is registered
    And [PlaceOrder]the user has selected the restaurant "Pizza House"
    And [PlaceOrder]the menu of "Pizza House" includes "Margherita Pizza" and "French Fries"
    When [PlaceOrder]the user tries to place the order without adding any menu items
    Then [PlaceOrder]the user get an error message "You must add at least one item to your order"
    And [PlaceOrder]the order is not placed
