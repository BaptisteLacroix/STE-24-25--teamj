Feature: User places an order from a restaurant

  Background:
    Given the restaurant service manager configured the following restaurants:
      | Name             | Menu Items                                           | Opening Time      | Closing Time      |
      | Le Petit Nice    | Salade Nicoise, Bouillabaisse, Tarte Tatin            | 2021-01-01 10:00  | 2021-01-01 22:00  |
      | Le Petit Jardin  | Salade de chèvre chaud, Magret de canard, Crème brûlée| 2021-01-01 11:00  | 2021-01-01 23:00  |
      | Le Petit Chateau | Escargots, Coq au vin, Mousse au chocolat             | 2021-01-01 09:00  | 2021-01-01 21:00  |

  Scenario: Registered user places an order for multiple menu items
    Given the user is registered
    And the user has selected the restaurant "Le Petit Nice"
    When the user adds "Salade Nicoise" and "Bouillabaisse" to their order
    And places the order
    Then the order is placed successfully

  Scenario: Registered user places an order for a single menu item
    Given the user is registered
    And the user has selected the restaurant "Le Petit Nice"
    When the user adds "Salade Nicoise" to their order
    And places the order
    Then the order is placed successfully

  Scenario: Registered user tries to place an order adding a menu item that is not available
    Given the user is registered
    And the user has selected the restaurant "Le Petit Nice"
    When the user tries to add "Spaghetti Carbonara" to their order
    Then the user gets an error message "Spaghetti Carbonara is not in the menu"
    And the order is not placed

  Scenario: Registered user tries to place an order without adding any menu items
    Given the user is registered
    And the user has selected the restaurant "Le Petit Nice"
    When the user tries to place the order without adding any menu items
    Then the user gets an error message "You must add at least one item to your order"
    And the order is not placed
