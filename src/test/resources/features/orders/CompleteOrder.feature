Feature: 3 users are doing a complete group order, specifying delivery location and time, choosing restaurant and items.

  Scenario: The first user creates a group order with delivery location and time, and adds items to his order
    Given the first user creates a group order with delivery location "Campus Main Gate" and delivery time of 14:30 PM
    And the user receives a group order identifier
    And He search restaurant that are open and can prepare items in time and should see:
      | Name            |
      | Le Petit Nice   |
      | Le Petit Jardin |
    And He select the restaurant "Le Petit Nice" and see the items compatible with the group order delivery time preparation:
      | Menu Item      |
      | Salade Nicoise |
      | Bouillabaisse  |
    And The first user adds the following items to his order:
      | Menu Item     |
      | Bouillabaisse |
    Then The first user validates his order
    Given The second user joins the group order
    And He search restaurant that are open and can prepare items in time and should see:
      | Name            |
      | Le Petit Nice   |
      | Le Petit Jardin |
    And He select the restaurant "Le Petit Jardin" and see the items compatible with the group order delivery time preparation:
      | Menu Item              |
      | Salade de chèvre chaud |
      | Crème brûlée           |
    And The second user adds the following items to his order:
      | Menu Item              |
      | Salade de chèvre chaud |
      | Crème brûlée           |
    Then The second user validates his order
    Given The third user joins the group order
    And He search restaurant that are open and can prepare items in time and should see:
      | Name            |
      | Le Petit Nice   |
      | Le Petit Jardin |
    And He select the restaurant "Le Petit Jardin" and see the items compatible with the group order delivery time preparation:
      | Menu Item              |
      | Salade de chèvre chaud |
      | Crème brûlée           |
    And The third user adds the following items to his order:
      | Menu Item              |
      | Salade de chèvre chaud |
      | Crème brûlée           |
    Then The third user validates his order and validates the group order

  Scenario: The second user attempts to select a restaurant that does not match the group order delivery time
    Given the first user creates a group order with delivery location "Campus Main Gate" and delivery time of 14:30 PM
    And the user receives a group order identifier
    And He search restaurant that are open and can prepare items in time and should see:
      | Name            |
      | Le Petit Nice   |
      | Le Petit Jardin |
    And He select the restaurant "Le Petit Nice" and see the items compatible with the group order delivery time preparation:
      | Menu Item      |
      | Salade Nicoise |
      | Bouillabaisse  |
    And The first user adds the following items to his order:
      | Menu Item     |
      | Bouillabaisse |
    Then The first user validates his order
    Given The second user joins the group order
    When the second user tries to bypass the restaurant selection by delivery time and selects the restaurant "Le Petit Chateau" that cannot prepare items in time
    Then the system rejects the item and displays an error: "No item can be prepared by 14:30 PM at Le Petit Chateau."

  Scenario: The second user tries to add a menu item that cannot be prepared in time
    Given the first user creates a group order with delivery location "Campus Main Gate" and delivery time of 14:30 PM
    And the user receives a group order identifier
    And He search restaurant that are open and can prepare items in time and should see:
      | Name            |
      | Le Petit Nice   |
      | Le Petit Jardin |
    And He select the restaurant "Le Petit Nice" and see the items compatible with the group order delivery time preparation:
      | Menu Item      |
      | Salade Nicoise |
      | Bouillabaisse  |
    And The first user adds the following items to his order:
      | Menu Item     |
      | Bouillabaisse |
    Then The first user validates his order
    Given The second user joins the group order
    Then the second user selects the restaurant "Le Petit Jardin"
    When the second user tries to add the following item to their order:
      | Menu Item   |
      | Magret de canard |
    Then the system rejects the item and displays an error: "Magret de canard cannot be prepared by 14:30 PM."

  Scenario: The third user tries to join the group order after it has been validated
    Given the first user creates a group order with delivery location "Campus Main Gate" and delivery time of 14:30 PM
    And the user receives a group order identifier
    And He search restaurant that are open and can prepare items in time and should see:
      | Name            |
      | Le Petit Nice   |
      | Le Petit Jardin |
    And He select the restaurant "Le Petit Nice" and see the items compatible with the group order delivery time preparation:
      | Menu Item      |
      | Salade Nicoise |
      | Bouillabaisse  |
    And The first user adds the following items to his order:
      | Menu Item     |
      | Bouillabaisse |
    Then The first user validates his order and validates the group order
    Given The third user tries to join the group order
    Then the system rejects the third user and displays an error: "The group order has already been validated."

  Scenario: The first user create an individual order, specifying delivery location and time, choosing restaurant and items.
    Given the first user creates an individual order with the restaurant "Le Petit Nice" and with delivery location "Campus Main Gate" and delivery time of 14:30 PM
    And The first user adds the following items to his individual order:
      | Menu Item     |
      | Bouillabaisse |
    Then The first user validates his individual order
