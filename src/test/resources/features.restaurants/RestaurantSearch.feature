Feature: Search for Restaurants

  Scenario: Search for a restaurant by name
    Given the following restaurants exist:
      | name      | menu items                                        |
      | mcdo      | "bigmac, 12.99", "nuggets, 8.99", "Burger, 10.49" |
      | subway    | "steakhouse, 12.99", "veggie, 8.99", "thon, 10.49"|
    When I search for restaurants by name "mcdo"
    Then I should see the following restaurants:
      | name      | menu items                                        |
      | mcdo      | "bigmac, 12.99", "nuggets, 8.99", "Burger, 10.49" |




  Scenario: Search for a restaurant by food
    Given the following restaurants exist:
      | name      | menu items                                        |
      | mcdo      | "bigmac, 12.99", "nuggets, 8.99", "Burger, 10.49" |
      | bk        | "Burger, 12.99", "veggie, 8.99", "thon, 10.49"|
      | subway    | "Steakhouse, 12.99", "veggie, 8.99", "thon, 10.49"|
    When I search for the food with name "Burger"
    Then I should see the following restaurants:
      | name      | menu items                                        |
      | bk        | "Burger, 12.99", "veggie, 8.99", "thon, 10.49"|
      | mcdo      | "bigmac, 12.99", "nuggets, 8.99", "Burger, 10.49" |

