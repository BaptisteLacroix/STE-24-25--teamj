Feature: A registered user can place orders for menu items from restaurants located on the campus.

  Background:
    Given a register user name "Baptiste" and with an account balance of 100.00 with student an id
    And a restaurant named "The Campus Cafe" with a menu that includes a "Cheeseburger" for 5.00 and "Fries" for 2.00 and "Soda" for 1.00

  Scenario: A user can place an order for a single menu item
    When "Baptiste" places an order for a "Cheeseburger"
    Then the order total should be 5.00
    And the account balance should be 95.00

  Scenario: A user can place an order for multiple menu items
    When "Baptiste" places an order for a "Cheeseburger" and a "Fries"
    Then the order total should be 7.00
    And the account balance should be 93.00
