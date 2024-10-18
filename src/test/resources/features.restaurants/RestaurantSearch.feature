Feature: Search for Restaurants

  Background:
    Given the restaurant service manager configured the following restaurants:
      | Name             | Menu Items                                             | Opening Time     | Closing Time     |
      | Le Petit Nice    | Salade Nicoise, Bouillabaisse, Tarte Tatin             | 2021-01-01 10:00 | 2021-01-01 22:00 |
      | Le Petit Jardin  | Salade de chèvre chaud, Magret de canard, Crème brûlée | 2021-01-01 11:00 | 2021-01-01 23:00 |
      | Le Petit Chateau | Escargots, Coq au vin, Mousse au chocolat              | 2021-01-01 09:00 | 2021-01-01 21:00 |

  Scenario: Search for a restaurant by name
    Given the user searches for restaurants by name "Le Petit Nice"
    Then the user should see the following restaurant(s):
      | name          | menu items                                 | price              |
      | Le Petit Nice | Salade Nicoise, Bouillabaisse, Tarte Tatin | 12.50, 25.00, 8.00 |

  Scenario: Search for restaurants by food
    Given the user searches for food with name "Bouillabaisse"
    Then the user should see the following restaurant(s):
      | name          | menu items    | price |
      | Le Petit Nice | Bouillabaisse | 25.00 |

  Scenario: Search for multiple restaurants serving the same food
    Given the user searches for food with name "Salade"
    Then the user should see the following restaurant(s):
      | name            | menu items             | price |
      | Le Petit Nice   | Salade Nicoise         | 12.50 |
      | Le Petit Jardin | Salade de chèvre chaud | 10.00 |

  Scenario: The user search for a restaurant that is not in the system
    Given the user searches for restaurants by name "Le Petit Bistro" that is not in the system
    Then the user should not see any restaurants

  Scenario: The user search for food that is not in the system
    Given the user searches for food with name "Pizza" that is not in the system
    Then the user should not see any restaurants

  Scenario: The user search for a restaurant that proposes a menu that contains items that have a preparation time below the group order delivery time
    Given the user creates a group order with a delivery time of 30 minutes later the current time
    And the user searches for restaurants that can prepare the food before the delivery time
    Then the user should see the following restaurant(s):
      | name            | menu items                                             | price |
      | Le Petit Nice   | Salade Nicoise, Bouillabaisse, Tarte Tatin             | 12.50, 25.00, 8.00 |
      | Le Petit Jardin | Salade de chèvre chaud, Magret de canard, Crème brûlée | 10.00, 20.00, 7.00 |
    Then the user select the restaurant "Le Petit Jardin" and should see only the 2 menu items that can be prepared before the delivery time
    And the user should see the following menu items:
      | name               | price |
      | Salade de chèvre chaud   | 10.00 |
      | Crème brûlée       |  7.00 |
