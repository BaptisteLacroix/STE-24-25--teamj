Feature: Browsing Menus from Different Campus Restaurants

  As an internet user,
  I want to browse menus from different campas restaurants,

  Scenario: User browses the menu of a single campus restaurant
    Given the following campus restaurant exists:
      | name      | menu items                                        |
      | mcdo      | "bigmac, 12.99", "nuggets, 8.99", "Burger, 10.49" |
    When the user visits the "mcdo" restaurant
    Then the user should see the menu for "mcdo" with the following items:
      | name   | price |
      | bigmac | 12.99 |
      | nuggets| 8.99  |
      | Burger | 10.49 |
#
  Scenario: User browses menus of multiple campus restaurants
    Given the following campus restaurants exist:
      | name      | menu items                                        |
      | mcdo      | "bigmac, 12.99", "nuggets, 8.99", "Burger, 10.49" |
      | subway    | "steakhouse, 12.99", "veggie, 8.99", "thon, 10.49"|

    When the user visits the "mcdo" restaurant
    Then the user should see the menu for "mcdo" with the following items:
      | name          | price  |
      | bigmac        | 12.99  |
      | nuggets       | 8.99   |
      | Burger        | 10.49  |

    And when the user visits the "subway" restaurant
    Then the user should see the menu for "subway" with the following items:
      | name          | price  |
      | steakhouse    | 12.99  |
      | veggie        | 8.99   |
      | thon          | 10.49  |
