#Feature: Browsing Menus from Different Restaurants
#
#  As an internet user,
#  I want to browse menus from different restaurants,
#
#  Background:
#    Given the restaurant service manager configured the following restaurants:
#      | Name               | Menu Items                                             | Opening Time     | Closing Time     |
#      | Le Petit Nice      | Salade Nicoise, Bouillabaisse, Tarte Tatin             | 2024-10-18 14:00 | 2024-10-18 16:00 |
#      | Le Petit Jardin    | Salade de chèvre chaud, Magret de canard, Crème brûlée | 2024-10-18 14:00 | 2024-10-18 16:00 |
#      | Le Petit Chateau   | Escargots, Coq au vin, Mousse au chocolat              | 2024-10-18 14:00 | 2024-10-18 16:00 |
#      | Le Gourmet D'Or    | Soupe à l'oignon, Boeuf Bourguignon, Tarte Tatin       | Closed           | Closed           |
#      | Bistro de la Plage | Quiche Lorraine, Ratatouille, Mousse au chocolat       | 2024-10-18 14:00 | 2024-10-18 16:00 |
#      | Café de l'Aube     | No menu items available                                | 2024-10-18 14:00 | 2024-10-18 16:00 |
#      | La Table Royale    | Coq au Vin, Bouillabaisse, Crêpe Suzette               | 2024-10-18 14:00 | 2024-10-18 15:00 |
#
#  Scenario: User browses the menu of a single restaurant
#    Given the user visits the "Le Petit Nice" restaurant
#    Then the user should see the menu for "Le Petit Nice" with the following items:
#      | name           | price |
#      | Salade Nicoise | 12.50 |
#      | Bouillabaisse  | 25.00 |
#      | Tarte Tatin    | 8.00  |
#
#  Scenario: User browses menus of multiple restaurants
#    Given the user visits the "Le Petit Nice" restaurant
#    Then the user should see the menu for "Le Petit Nice" with the following items:
#      | name           | price |
#      | Salade Nicoise | 12.50 |
#      | Bouillabaisse  | 25.00 |
#      | Tarte Tatin    | 8.00  |
#
#    And the user visits the "Le Petit Jardin" restaurant
#    Then the user should see the menu for "Le Petit Jardin" with the following items:
#      | name                  | price |
#      | Salade de chèvre chaud| 10.00 |
#      | Magret de canard      | 20.00 |
#      | Crème brûlée          | 7.00  |
