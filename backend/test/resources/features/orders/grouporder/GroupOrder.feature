Feature: Group Order Creation

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

  Scenario: Registered user creates a group order with delivery location
    Given [GroupOrder]the user "2ed64a86-d499-4a9c-a0a1-9aba06297348" is registered
    When [GroupOrder]the user creates a group order with delivery location "Campus Main Gate"
    Then [GroupOrder]the user receives a group order identifier
    And [GroupOrder]the group order delivery location is "Campus Main Gate"

  Scenario: Registered user creates a group order with delivery time
    Given [GroupOrder]the user is registered
    When [GroupOrder]the user creates a group order with delivery location "Campus Main Gate"
    Then [GroupOrder]the user receives a group order identifier
    And [GroupOrder]the group order delivery location is "Campus Main Gate"

  Scenario: Registered user tries to create a group order without specifying a delivery location
    Given [GroupOrder]the user is registered
    When [GroupOrder]the user tries to create a group order without specifying a delivery location
    Then [GroupOrder]the user receives an error message "You must specify a delivery location"
    And [GroupOrder]the group order is not created
