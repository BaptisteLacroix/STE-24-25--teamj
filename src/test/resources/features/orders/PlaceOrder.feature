Feature: User places an order from a restaurant

  Background:
    Given the restaurant service manager configured the following restaurants:
      | Name               | Menu Items                                             | Opening Time     | Closing Time     |
      | Le Petit Nice      | Salade Nicoise, Bouillabaisse, Tarte Tatin             | 2024-10-18 14:00 | 2024-10-18 16:00 |
      | Le Petit Jardin    | Salade de chèvre chaud, Magret de canard, Crème brûlée | 2024-10-18 14:00 | 2024-10-18 16:00 |
      | Le Petit Chateau   | Escargots, Coq au vin, Mousse au chocolat              | 2024-10-18 14:00 | 2024-10-18 16:00 |
      | Le Gourmet D'Or    | Soupe à l'oignon, Boeuf Bourguignon, Tarte Tatin       | Closed           | Closed           |
      | Bistro de la Plage | Quiche Lorraine, Ratatouille, Mousse au chocolat       | 2024-10-18 14:00 | 2024-10-18 16:00 |
      | Café de l'Aube     | No menu items available                                | 2024-10-18 14:00 | 2024-10-18 16:00 |
      | La Table Royale    | Coq au Vin, Bouillabaisse, Crêpe Suzette               | 2024-10-18 14:00 | 2024-10-18 15:00 |

  Scenario: Registered user places an order for multiple menu items
    Given the user is registered
    And the user has selected the restaurant "Le Petit Nice"
    And the user start and order by specifying the delivery location from the pre-recorded locations and specifying the delivery time as 14:30
    When the user adds "Salade Nicoise" and "Bouillabaisse" to their order
    And places the order
    Then the order is placed successfully

  Scenario: Registered user places an order for a single menu item
    Given the user is registered
    And the user has selected the restaurant "Le Petit Nice"
    And the user start and order by specifying the delivery location from the pre-recorded locations and specifying the delivery time as 14:30
    When the user adds "Salade Nicoise" to their order
    And places the order
    Then the order is placed successfully

  Scenario: Registered user tries to place an order adding a menu item that is not available
    Given the user is registered
    And the user has selected the restaurant "Le Petit Nice"
    And the user start and order by specifying the delivery location from the pre-recorded locations and specifying the delivery time as 14:30
    When the user tries to add "Spaghetti Carbonara" to their order
    Then the user gets an error message "Spaghetti Carbonara is not in the menu"
    And the item is not added to the order

  Scenario: Registered user tries to place an order without adding any menu items
    Given the user is registered
    And the user has selected the restaurant "Le Petit Nice"
    And the user start and order by specifying the delivery location from the pre-recorded locations and specifying the delivery time as 14:30
    When the user tries to place the order without adding any menu items
    Then the user gets an error message "You must add at least one item to your order"
    And the order is not placed
