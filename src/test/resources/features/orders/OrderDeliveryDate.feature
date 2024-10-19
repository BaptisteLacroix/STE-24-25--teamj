Feature: Each time a new item menu is added to an order, the possible delivery dates change according to preparation times.
  If a delivery time has already been selected, the system propose only menu items that can be prepared in time for the selected delivery time.

  Scenario: User add item to it's order the possible delivery dates change according to preparation times
    Given [OrderDeliveryDate]the user is registered
    And [OrderDeliveryDate]the user has selected the restaurant "Le Petit Nice"
    And [OrderDeliveryDate]the user start and order by specifying the delivery location from the pre-recorded locations
    When [OrderDeliveryDate]the user adds "Salade Nicoise" to their order
    Then [OrderDeliveryDate]the possible delivery dates are updated to include the time it takes to prepare "Salade Nicoise"

  Scenario: User add item to it's order the system propose only menu items that can be prepared in time for the selected delivery time
    Given [OrderDeliveryDate]the user is registered
    And [OrderDeliveryDate]the user creates a group order with delivery location "Campus Main Gate" and delivery time of 14:30 PM
    Then [OrderDeliveryDate]the system proposes the restaurants that are open and can prepare items in time:
      | Name            |
      | Le Petit Nice   |
      | Le Petit Jardin |
      | La Table Royale |
    And [OrderDeliveryDate]the user selects the restaurant "Le Petit Nice" and sees the items compatible with the group order delivery time preparation:
      | Menu Item      |
      | Salade Nicoise |
      | Bouillabaisse  |


