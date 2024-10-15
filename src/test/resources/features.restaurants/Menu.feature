Feature: Browsing Menus from Different Restaurants

  As an internet user,
  I want to browse menus from different restaurants,

  Background:
    Given the restaurant service manager configured the following restaurants:
      | Name             | Menu Items                                           | Opening Time      | Closing Time      |
      | Le Petit Nice    | Salade Nicoise, Bouillabaisse, Tarte Tatin            | 2021-01-01 10:00  | 2021-01-01 22:00  |
      | Le Petit Jardin  | Salade de chèvre chaud, Magret de canard, Crème brûlée| 2021-01-01 11:00  | 2021-01-01 23:00  |
      | Le Petit Chateau | Escargots, Coq au vin, Mousse au chocolat             | 2021-01-01 09:00  | 2021-01-01 21:00  |

  Scenario: User browses the menu of a single restaurant
    Given the user visits the "Le Petit Nice" restaurant
    Then the user should see the menu for "Le Petit Nice" with the following items:
      | name           | price |
      | Salade Nicoise | 12.50 |
      | Bouillabaisse  | 25.00 |
      | Tarte Tatin    | 8.00  |

  Scenario: User browses menus of multiple restaurants
    Given the user visits the "Le Petit Nice" restaurant
    Then the user should see the menu for "Le Petit Nice" with the following items:
      | name           | price |
      | Salade Nicoise | 12.50 |
      | Bouillabaisse  | 25.00 |
      | Tarte Tatin    | 8.00  |

    And the user visits the "Le Petit Jardin" restaurant
    Then the user should see the menu for "Le Petit Jardin" with the following items:
      | name                  | price |
      | Salade de chèvre chaud| 10.00 |
      | Magret de canard      | 20.00 |
      | Crème brûlée          | 7.00  |
