Feature: 3 users are doing a complete group order, specifying delivery location and time, choosing restaurant and items.
  Scenario: The first user creates a group order with delivery location and time, and adds items to his order
    Given the first user creates a group order with delivery location "Campus Main Gate" and delivery time of 14:30 PM
    Then the group receives an identifier
    When He searches restaurants that are open and can prepare items in time
    Then He should see:
      | Name             |
      | Le Petit Nice    |
      | Le Petit Jardin  |
      | La Table Royale  |
    When He selects the restaurant "Le Petit Nice"
    Then He should see the items compatible with the group order delivery time preparation:
      | Menu Item      |
      | Salade Nicoise |
      | Bouillabaisse  |
    When The first user creates an order with the following items:
      | Menu Item     |
      | Bouillabaisse |
    And The first user validates his order
    Then The order should be validated

    Given The second user joins the group order
    And He searches restaurants that are open and can prepare items in time
    Then He should see:
      | Name             |
      | Le Petit Nice    |
      | Le Petit Jardin  |
      | La Table Royale  |
    When He selects the restaurant "Le Petit Jardin"
    Then He should see the items compatible with the group order delivery time preparation:
      | Menu Item              |
      | Salade de chèvre chaud |
      | Crème brûlée           |
    When The second user creates an order with the following items:
      | Menu Item              |
      | Salade de chèvre chaud |
      | Crème brûlée           |
    And The second user validates his order
    Then The second user's order should be validated
    Given The third user joins the group order
    When He searches restaurants that are open and can prepare items in time
    Then He should see:
      | Name             |
      | Le Petit Nice    |
      | Le Petit Jardin  |
      | La Table Royale  |
    When He selects the restaurant "Le Petit Jardin"
    Then He should see the items compatible with the group order delivery time preparation:
      | Menu Item              |
      | Salade de chèvre chaud |
      | Crème brûlée           |
    When The third user creates an order with the following items:
      | Menu Item              |
      | Salade de chèvre chaud |
      | Crème brûlée           |
    And The third user validates his order
    And The third user validates the group order
    Then The third user's order and the group order should be validated

  Scenario: The second user attempts to select a restaurant that does not match the group order delivery time
    Given the first user creates a group order with delivery location "Campus Main Gate" and delivery time of 14:30 PM
    Then the group receives an identifier
    When He searches restaurants that are open and can prepare items in time
    Then He should see:
      | Name             |
      | Le Petit Nice    |
      | Le Petit Jardin  |
      | La Table Royale  |
    When He selects the restaurant "Le Petit Nice"
    Then He should see the items compatible with the group order delivery time preparation:
      | Menu Item      |
      | Salade Nicoise |
      | Bouillabaisse  |
    When The first user creates an order with the following items:
      | Menu Item     |
      | Bouillabaisse |
    And The first user validates his order
    Then The order should be validated

    Given The second user joins the group order
    When The second user tries to bypass the restaurant selection by delivery time
    And He selects the restaurant "Le Petit Chateau" that cannot prepare items in time
    Then the system rejects the item and displays an error: "Order cannot be added, restaurant does not have the capacity to deliver in time"

  Scenario: The second user tries to add a menu item that cannot be prepared in time
    Given the first user creates a group order with delivery location "Campus Main Gate" and delivery time of 14:30 PM
    Then the group receives an identifier
    When He searches restaurants that are open and can prepare items in time
    Then He should see:
      | Name             |
      | Le Petit Nice    |
      | Le Petit Jardin  |
      | La Table Royale  |
    When He selects the restaurant "Le Petit Nice"
    Then He should see the items compatible with the group order delivery time preparation:
      | Menu Item      |
      | Salade Nicoise |
      | Bouillabaisse  |
    When The first user creates an order with the following items:
      | Menu Item     |
      | Bouillabaisse |
    And The first user validates his order
    Then The order should be validated
    Given The second user joins the group order
    Then the second user selects the restaurant "Le Petit Jardin"
    When The second user tries to create an order with the following items:
      | Menu Item        |
      | Magret de canard |
    Then the system rejects the item and displays an error: "Cannot add item to order, no slot available."

  Scenario: The third user tries to join the group order after it has been validated
    Given the first user creates a group order with delivery location "Campus Main Gate" and delivery time of 14:30 PM
    Then the group receives an identifier
    When He searches restaurants that are open and can prepare items in time
    Then He should see:
      | Name             |
      | Le Petit Nice    |
      | Le Petit Jardin  |
      | La Table Royale  |
    When He selects the restaurant "Le Petit Nice"
    Then He should see the items compatible with the group order delivery time preparation:
      | Menu Item      |
      | Salade Nicoise |
      | Bouillabaisse  |
    When The first user creates an order with the following items:
      | Menu Item     |
      | Bouillabaisse |
    And The first user validates his order
    And The first user validates the group order
    Then The first user's order and the group order should be validated

    Given The third user tries to join the group order
    Then the system rejects the third user and displays an error: "The group order has already been validated."

#  Scenario: The first user creates an individual order, specifying delivery location and time, choosing restaurant and items.
#    Given the first user creates an individual order with the restaurant "Le Petit Nice" and with delivery location "Campus Main Gate" and delivery time of 14:30 PM
#    And The first user adds the following items to his individual order:
#      | Menu Item     |
#      | Bouillabaisse |
#    Then The first user validates his individual order
