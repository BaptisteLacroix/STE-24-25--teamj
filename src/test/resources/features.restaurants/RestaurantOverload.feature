Feature: Restaurant capacity management

  Scenario: Order accepted when preparation can be completed within available slots
    Given the restaurant is open from "2024-10-18 14:00" to "2024-10-18 16:00"
      | slotStart        | currentCapacity | maxCapacity | personnel |
      | 2024-10-18 14:00 | 0               | 7200        | 4         |
      | 2024-10-18 14:30 | 0               | 7200        | 4         |
      | 2024-10-18 15:00 | 0               | 7200        | 4         |
      | 2024-10-18 15:30 | 0               | 7200        | 4         |
      | 2024-10-18 16:00 | 0               | 7200        | 4         |
    And the current time is "2024-10-18 14:00"
    When the user places an order for a "BigMac" with a preparation time of 60 seconds
    Then the system checks for available slots within the next 4 slots
    And the order should be accepted because preparation can be completed by "2024-10-18 14:00"
