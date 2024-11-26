Feature: Order Details

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

  Scenario: Registered user selects a restaurant and a delivery location among pre-recorded locations for a single order
    Given [OrderDetails]the user is registered
    When [OrderDetails]the user selects the restaurant "Le Petit Nice"
    Then [OrderDetails]the user start a single order by specifying the delivery location from the pre-recorded locations and specifying the delivery date that is 30 minutes before closing time

  Scenario: Registered user selects a restaurant and tries to select a delivery location that is not recorded in the restaurant
    Given [OrderDetails]the user is registered
    When [OrderDetails]the user selects the restaurant "Le Petit Nice"
    Then [OrderDetails]the user tries to select "123 Campus Lane" as the delivery location
    And [OrderDetails]the user receives an error message "Location not found: 123 Campus Lane"
    And [OrderDetails]the user is not able to proceed with the order
