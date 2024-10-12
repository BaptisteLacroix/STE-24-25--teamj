Feature: Order Details

  Scenario: Registered user select a restaurant and a delivery location among pre-recorded restaurant for single order
    Given [OrderDetails]the user is registered
    When [OrderDetails]the user selects the restaurant "Pizza House"
    Then [OrderDetails]the user start a single order by specifying the delivery location from the pre-recorded location
    And [OrderDetails]choose and delivery date within the restaurant's preparation capabilities.

  Scenario: Registered user select a restaurant and try to select a delivery location that is not recorded in the restaurant
    Given [OrderDetails]the user is registered
    When [OrderDetails]the user selects the restaurant "Pizza House"
    Then [OrderDetails]the user tries to select "123 Campus Lane" as delivery location
    And [OrderDetails]the user receives an error message "123 Campus Lane is not a valid delivery location for Pizza House"
    And [OrderDetails]the user is not able to proceed with the order

