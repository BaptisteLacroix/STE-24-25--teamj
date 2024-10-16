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

  Scenario: Registered user creates a group order with delivery location and tries to change the delivery time
    Given [GroupOrder]the user is registered
    When [GroupOrder]the user creates a group order with delivery location "Campus Main Gate" and delivery time of 23:30 PM
    And [GroupOrder]the user tries to change the delivery time to 23:30 PM
    Then [GroupOrder]the user receives an error message "You cannot change the delivery time of a group order"
    And [GroupOrder]the group order delivery location is "Campus Main Gate"

  Scenario: Registered user creates a group order with delivery location and tries to specify a delivery time in the past
    Given [GroupOrder]the user is registered
    When [GroupOrder]the user creates a group order with delivery location "Campus Main Gate"
    And [GroupOrder]the user tries to specify a delivery time in the past of 01:00 PM
    Then [GroupOrder]the user receives an error message "You cannot specify a delivery time in the past"
