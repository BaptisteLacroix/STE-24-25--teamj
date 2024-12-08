package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.menu.MenuItem;
import fr.unice.polytech.equipe.j.slot.Slot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RestaurantServiceManager {

    // Static instance (Singleton)
    private static RestaurantServiceManager instance;

    // Private constructor to prevent external instantiation
    private RestaurantServiceManager() {}

    static void resetInstance() {
        instance = null;
    }

    /**
     * Retrieves the singleton instance of RestaurantServiceManager.
     *
     * @return The singleton instance.
     */
    public static RestaurantServiceManager getInstance() {
        if (instance == null) {
            instance = new RestaurantServiceManager();
        }
        return instance;
    }

    // --- Public Search Methods ---

    /**
     * Searches for restaurants by a partial or exact match of their name.
     *
     * @param restaurants List of restaurants to search from.
     * @param name The name or partial name to search for.
     * @return A list of restaurants that match the name.
     */
    public List<IRestaurant> searchByName(List<IRestaurant> restaurants, String name) {
        return searchByCriteria(restaurants, restaurant -> restaurant.getRestaurantName().toLowerCase().contains(name.toLowerCase()));
    }

    /**
     * Searches for restaurants by food type (based on menu items).
     *
     * @param restaurants List of restaurants to search from.
     * @param foodType The food type to search for.
     * @return A list of restaurants that offer the given food type.
     */
    public List<IRestaurant> searchByTypeOfFood(List<IRestaurant> restaurants, String foodType) {
        return searchByCriteria(restaurants, restaurant -> restaurant.getMenu().getItems().stream()
                .anyMatch(item -> item.getName().contains(foodType)));
    }

    /**
     * Generalizes searching restaurants based on a given criteria (predicate).
     *
     * @param restaurants List of restaurants to search from.
     * @param criteria The search criteria.
     * @return A list of restaurants that match the criteria.
     */
    private List<IRestaurant> searchByCriteria(List<IRestaurant> restaurants, java.util.function.Predicate<IRestaurant> criteria) {
        return restaurants.stream()
                .filter(criteria)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Searches for restaurants that can deliver on time given a specific delivery time.
     *
     * @param restaurants List of restaurants to check.
     * @param deliveryTime The specified delivery time.
     * @return A list of restaurants that can deliver on time.
     */
    public List<IRestaurant> searchRestaurantByDeliveryTime(List<IRestaurant> restaurants, Optional<LocalDateTime> deliveryTime) {
        return deliveryTime.map(time -> restaurants.stream()
                        .filter(restaurant -> isOpenAt(restaurant, time) && canDeliverOnTime(restaurant, time))
                        .collect(Collectors.toList()))
                .orElse(restaurants);
    }

    /**
     * Checks if a restaurant can deliver items on time based on their menu and available slots.
     *
     * @param restaurant The restaurant to check.
     * @param deliveryTime The specified delivery time.
     * @return true if the restaurant can deliver on time, false otherwise.
     */
    private boolean canDeliverOnTime(IRestaurant restaurant, LocalDateTime deliveryTime) {
        return restaurant.getMenu().getItems().stream()
                .anyMatch(item -> TimeUtils.getNow().plusSeconds(item.getPrepTime()).isBefore(deliveryTime) &&
                        restaurant.getSlots().stream().anyMatch(slot -> slot.getAvailableCapacity() >= item.getPrepTime()));
    }

    /**
     * Searches for menu items from a restaurant that can be delivered by a specified time.
     *
     * @param restaurant The restaurant to search for menu items.
     * @param time The delivery time.
     * @return A list of menu items that can be delivered on time.
     */
    public List<MenuItem> searchItemsByDeliveryTime(IRestaurant restaurant, Optional<LocalDateTime> time) {
        if (time.isEmpty() || !isOpenAt(restaurant, time.get())) {
            return new ArrayList<>();
        }
        return restaurant.getMenu().getItems().stream()
                .filter(item -> TimeUtils.getNow().plusSeconds(item.getPrepTime()).isBefore(time.get()))
                .collect(Collectors.toList());
    }

    /**
     * Checks if the restaurant is open at a specific time.
     *
     * @param restaurant The restaurant to check.
     * @param time The time to check.
     * @return true if the restaurant is open at the specified time, false otherwise.
     */
    private boolean isOpenAt(IRestaurant restaurant, LocalDateTime time) {
        return restaurant.getOpeningTime()
                .filter(openingTime -> time.isAfter(openingTime) && time.isBefore(restaurant.getClosingTime().orElseThrow()))
                .isPresent();
    }

    /**
     * Finds a slot in a restaurant that starts at a given time.
     *
     * @param restaurant The restaurant to search in.
     * @param slotStartTime The start time of the slot to find.
     * @return The slot that matches the specified start time, or null if not found.
     */
    public Slot findSlotByStartTime(IRestaurant restaurant, LocalDateTime slotStartTime) {
        return restaurant.getSlots().stream()
                .filter(slot -> slot.getOpeningDate().equals(slotStartTime))
                .findFirst()
                .orElse(null);
    }
}
