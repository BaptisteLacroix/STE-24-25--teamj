Feature: Any member of a group order can validate the group order at the end of it's individual order and if needed specify a delivery time

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

  Scenario: The creator of a group order can validate the group order.
    Given [ValidateGroupOrder]the user is registered
    And [ValidateGroupOrder]the user creates a group order with delivery location "Campus Main Gate" and delivery time of 14:30 PM
    And [ValidateGroupOrder]the user receives a group order identifier
    When [ValidateGroupOrder]the user adds the following items to his order from the restaurant "Le Petit Nice":
      | Menu Item      |
      | Salade Nicoise |
      | Bouillabaisse  |
    Then [ValidateGroupOrder]the user validate it's order and the group order

  Scenario: The creator of a group order can validate the group order and specify a delivery time.
    Given [ValidateGroupOrder]the user is registered
    And [ValidateGroupOrder]the user creates a group order with delivery location "Campus Main Gate"
    And [ValidateGroupOrder]the user receives a group order identifier
    When [ValidateGroupOrder]the user adds the following items to his order from the restaurant "Le Petit Jardin":
      | Menu Item              |
      | Salade de chèvre chaud |
      | Magret de canard       |
    Then [ValidateGroupOrder]the user validates his order and validates the group order and specifies a delivery time of 15:00 PM

  Scenario: The creator of a Group order create it and tries to validate it with a delivery time that is not compatible with all sub-orders
    Given [ValidateGroupOrder]the user is registered
    And [ValidateGroupOrder]the user creates a group order with delivery location "Campus Main Gate"
    And [ValidateGroupOrder]the user receives a group order identifier
    When [ValidateGroupOrder]the user adds the following items to his order from the restaurant "Le Petit Chateau":
      | Menu Item          |
      | Escargots          |
      | Mousse au chocolat |
    And [ValidateGroupOrder]the user validates his order and validates the group order and specifies a delivery time of 14:10 PM that is not compatible
    Then [ValidateGroupOrder]the user receives an error message "The delivery time is not compatible with all sub-orders"
    And [ValidateGroupOrder]the group order delivery time is not set

  Scenario: The creator of a group order add it's order to the group order and another user joins the group order and validates it
    Given [ValidateGroupOrder]the user is registered
    And [ValidateGroupOrder]the user creates a group order with delivery location "Campus Main Gate" and delivery time of 15:50 PM
    And [ValidateGroupOrder]the user receives a group order identifier
    And [ValidateGroupOrder]the user adds the following items to his order from the restaurant "Le Petit Nice":
      | Menu Item      |
      | Salade Nicoise |
      | Bouillabaisse  |
    When [ValidateGroupOrder]the user validates his order
    And [ValidateGroupOrder]another user joins the group order
    And [ValidateGroupOrder]the other user adds the following items to his order from the restaurant "Le Petit Nice":
      | Menu Item   |
      | Tarte Tatin |
    And [ValidateGroupOrder]the other user validates his order and validates the group order
    Then [ValidateGroupOrder]the group order is validated
