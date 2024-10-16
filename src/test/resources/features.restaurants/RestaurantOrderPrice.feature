Feature: Order Price
  Background:
    Given the following restaurant exists:
      | name               | menu items                                          |
      | P.diddy restaurant | "Ariana Grande Frite, 11.99", "Lil Nuggets X, 9.99", "Will Smith Shake, 10.49", "Kanye West Burger, 12.99", "Drake Salad, 8.49", "Beyonce Pizza, 14.99", "Rihanna Taco, 7.99"  |

  Scenario: Add a n% discount every k commands
    Given The restaurant wants to give a 30% discount every 3 command for each user
    When Any user makes any multiples of 3 orders
    Then The order should see its price reduced by the corresponding percentage

  Scenario: give a free item for an order with more than n items
    Given The restaurant wants to give a free item for every order with more than 5 items
    When Any user creates an order with more that 5 items
    Then The less expensive item from the order should have a price of 0

  Scenario: don't give a free item for an order with less than n items
    Given The restaurant wants to give a free item for every order with more than 5 items
    When Any user creates an order with less than 5 items
    Then The less expensive item from the order should not have a price of 0





