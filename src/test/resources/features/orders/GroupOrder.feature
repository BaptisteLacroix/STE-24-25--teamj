Feature: Group Order Creation

  Scenario: Registered user creates a group order with delivery location
    Given [GroupOrder]the user is registered
    When [GroupOrder]the user creates a group order with delivery location "123 Campus Lane"
    Then [GroupOrder]the user receives a group order identifier
    And [GroupOrder]the group order delivery location is "123 Campus Lane"

  Scenario: Registered user creates a group order with delivery time
    Given [GroupOrder]the user is registered
    When [GroupOrder]the user creates a group order with delivery location "123 Campus Lane"
    And [GroupOrder]the user specifies a delivery time of 23:30 PM
    Then [GroupOrder]the user receives a group order identifier
    And [GroupOrder]the group order delivery location is "123 Campus Lane"
    And [GroupOrder]the group order delivery time is 23:30 PM

  Scenario: Registered user tries to create a group order without specifying a delivery location
    Given [GroupOrder]the user is registered
    When [GroupOrder]the user tries to create a group order without specifying a delivery location
    Then [GroupOrder]the user receives an error message "You must specify a delivery location"
    And [GroupOrder]the group order is not created

  Scenario: Registered user creates a group order with delivery location and try to change the delivery time
    Given [GroupOrder]the user is registered
    When [GroupOrder]the user creates a group order with delivery location "123 Campus Lane" and delivery time of 23:30 PM
    And [GroupOrder]the user tries to change the delivery time to 23:30 PM
    Then [GroupOrder]the user receives an error message "You cannot change the delivery time of a group order"
    And [GroupOrder]the group order delivery location is "123 Campus Lane"

  Scenario: Registered user creates a group order with delivery location and try to specify a delivery time in the past
    Given [GroupOrder]the user is registered
    When [GroupOrder]the user creates a group order with delivery location "123 Campus Lane"
    And [GroupOrder]the user tries to specify a delivery time in the past of 01:00 PM
    Then [GroupOrder]the user receives an error message "You cannot specify a delivery time in the past"
