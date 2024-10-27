Feature: User pays their order

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

  Scenario: Registered user can pay their order using Credit Card
    Given the user is registered in the system
    And the user selected the restaurant "Le Petit Nice"
    And the user order by specifying the delivery location from the pre-recorded locations and the delivery time 14:30
    When the user adds "Salade Nicoise" and "Bouillabaisse" to the order list
    And places their order
    And Set their payment method to Credit Card
    Then the order is successfully placed

  Scenario: Registered user can pay their order using PayLib
    Given the user is registered in the system
    And the user selected the restaurant "Le Petit Chateau"
    And the user order by specifying the delivery location from the pre-recorded locations and the delivery time 14:30
    When the user adds "Escargots" and "Mousse au chocolat" to the order list
    And places their order
    And Set their payment method to paylib
    Then the order is successfully placed

  Scenario: Registered user can pay their order using PayPal
    Given the user is registered in the system
    And the user selected the restaurant "Le Petit Nice"
    And the user order by specifying the delivery location from the pre-recorded locations and the delivery time 14:30
    When the user adds "Bouillabaisse" and "Bouillabaisse" to the order list
    And places their order
    And Set their payment method to paypal
    Then the order is successfully placed


  Scenario: After validating and ppaying the user should be able to see the successful payment and transaction
    Given the user is registered in the system
    And the user selected the restaurant "Le Petit Nice"
    And the user order by specifying the delivery location from the pre-recorded locations and the delivery time 14:30
    When the user adds "Bouillabaisse" and "Bouillabaisse" to the order list
    And places their order
    And Set their payment method to paypal
    Then the order is successfully placed
    And they should be able to see the payment details with the amount 50
    And payment method used paypal
    And the date corresponding to today


