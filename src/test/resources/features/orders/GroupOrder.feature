Feature: Group Order Creation

  Background:
    Given the restaurant service manager configured the following restaurants:
      | Name             | Menu Items                                             | Opening Time     | Closing Time     |
      | Le Petit Nice    | Salade Nicoise, Bouillabaisse, Tarte Tatin             | 2021-01-01 10:00 | 2021-01-01 22:00 |
      | Le Petit Jardin  | Salade de chèvre chaud, Magret de canard, Crème brûlée | 2021-01-01 11:00 | 2021-01-01 23:00 |
      | Le Petit Chateau | Escargots, Coq au vin, Mousse au chocolat              | 2021-01-01 09:00 | 2021-01-01 21:00 |

  Scenario: Registered user creates a group order with delivery location
    Given [GroupOrder]the user is registered
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
