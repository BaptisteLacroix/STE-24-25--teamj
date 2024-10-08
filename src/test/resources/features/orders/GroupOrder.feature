Feature: Group Order Creation

  Scenario: Registered user creates a group order with delivery location
    Given the user is registered
    And the user has selected the restaurant "Pizza House"
    When the user creates a group order with delivery location "123 Campus Lane"
    Then the user receives a group order identifier
    And the group order delivery location is "123 Campus Lane"

  Scenario: Registered user creates a group order with delivery time
    Given the user is registered
    And the user has selected the restaurant "Pizza House"
    When the user creates a group order with delivery location "123 Campus Lane"
    And the user specifies a delivery time of 23:30 PM
    Then the user receives a group order identifier
    And the group order delivery location is "123 Campus Lane"
    And the group order delivery time is 23:30 PM
