Feature: User pays their order

  Background:
    Given the restaurant manager configured the following restaurants:
      | Name             | Menu Items                                           | Opening Time      | Closing Time      |
      | Le Petit Nice    | Salade Nicoise, Bouillabaisse, Tarte Tatin            | 2021-01-01 10:00  | 2021-01-01 22:00  |
      | Le Petit Jardin  | Salade de chèvre chaud, Magret de canard, Crème brûlée| 2021-01-01 11:00  | 2021-01-01 23:00  |
      | Le Petit Chateau | Escargots, Coq au vin, Mousse au chocolat             | 2021-01-01 09:00  | 2021-01-01 21:00  |
##
  Scenario: Registered user can pay their order using Credit Card
    Given the user is registered in the system
    And the user selected the restaurant "Le Petit Nice"
    And the user order by specifying the delivery location from the pre-recorded locations
    When the user adds "Salade Nicoise" and "Bouillabaisse" to the order list
    And places their order
    And Set thir payment method to Credit Card
    Then the order is successfully placed

  Scenario: Registered user can pay their order using PayLib
    Given the user is registered in the system
    And the user selected the restaurant "Le Petit Chateau"
    And the user order by specifying the delivery location from the pre-recorded locations
    When the user adds "Escargots" and "Mousse au chocolat" to the order list
    And places their order
    And Set thir payment method to paylib
    Then the order is successfully placed

  Scenario: Registered user can pay their order using PayPal
    Given the user is registered in the system
    And the user selected the restaurant "Le Petit Nice"
    And the user order by specifying the delivery location from the pre-recorded locations
    When the user adds "Bouillabaisse" and "Bouillabaisse" to the order list
    And places their order
    And Set thir payment method to paypal
    Then the order is successfully placed


  Scenario: After validating and ppaying the user should be able to see the successful payment and transaction
    Given the user is registered in the system
    And the user selected the restaurant "Le Petit Nice"
    And the user order by specifying the delivery location from the pre-recorded locations
    When the user adds "Bouillabaisse" and "Bouillabaisse" to the order list
    And places their order
    And Set thir payment method to paypal
    Then the order is successfully placed
    And they should be able to see the payment details with the amount 50
    And payment method used paypal
    And the date corresponding to today


