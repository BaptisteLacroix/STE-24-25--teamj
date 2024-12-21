Feature: Search for Restaurants
  Scenario: Search for a restaurant by name
    Given the user searches for restaurants by name "Le Petit Nice"
    Then the user should see the following restaurant(s):
      | name          | menu items                                 | price              |
      | Le Petit Nice | Salade Nicoise, Bouillabaisse, Tarte Tatin | 12.50, 25.00, 8.00 |

  Scenario: Search for restaurants by food
    Given the user searches for food with name "Main Course"
    Then the user should see the following restaurant(s):
      | name            | menu items    | price |
      | Le Petit Nice   | Bouillabaisse | 25.00 |
      | La Table Royale | Bouillabaisse | 25.00 |

  Scenario: The user searches for a restaurant that is not in the system
    Given the user searches for restaurants by name "Le Petit Bistro" that is not in the system
    Then the user should not see any restaurants

  Scenario: The user searches for food type that is not in the system
    Given the user searches for food type "Pizza" that is not in the system
    Then the user should not see any restaurants
