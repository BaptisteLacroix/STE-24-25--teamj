package fr.unice.polytech.equipe.j.restaurant.backend;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.restaurant.backend.menu.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.backend.slot.Slot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestaurantServiceManager {

    // Static instance (Singleton)
    private static RestaurantServiceManager instance;

    // List of restaurants managed by the service
    private final List<IRestaurant> restaurants;

    // Private constructor to prevent external instantiation
    private RestaurantServiceManager() {
        this.restaurants = new ArrayList<>();
    }

    public static void resetInstance() {
        instance = null;
    }

    // Overloaded method for backward compatibility (uses the system clock by default)
    public static RestaurantServiceManager getInstance() {
        if (instance == null) {
            instance = new RestaurantServiceManager();
            return instance;
        }
        return instance;
    }

    // Add restaurant to the manager (Package-Private)
    public void addRestaurant(IRestaurant restaurant) {
        restaurants.add(restaurant);
    }

    // Remove restaurant from the manager (Package-Private)
    public void removeRestaurant(IRestaurant restaurant) {
        restaurants.remove(restaurant);
    }

    // --- Public Search Methods ---

    // Search restaurants by name (exact or partial match)
    public List<IRestaurant> searchByName(String name) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getRestaurantName().toLowerCase().contains(name.toLowerCase()))
                .distinct()
                .toList();
    }

    // Search restaurants by type of food (based on menu items)
    public List<IRestaurant> searchByTypeOfFood(String foodType) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getMenu().getItems().stream()
                        .anyMatch(item -> item.getName().contains(foodType)))
                .distinct()
                .toList();
    }

    /**
     * Search for restaurants with a menuItem which, at the end of the preparation time, is shorter than the specified delivery time.
     *
     * @param deliveryTime the delivery time to check
     * @return a list of restaurants that can deliver the order on time
     */
    public List<IRestaurant> searchRestaurantByDeliveryTime(Optional<LocalDateTime> deliveryTime) {
        return deliveryTime.map(localDateTime -> restaurants.stream()
                .filter(restaurant -> isOpenAt(restaurant, localDateTime) && restaurant.getMenu().getItems().stream()
                        .anyMatch(item -> TimeUtils.getNow().plusSeconds(item.getPrepTime()).isBefore(localDateTime) &&
                                restaurant.getSlots().stream().anyMatch(slot -> slot.getAvailableCapacity() >= item.getPrepTime())))
                .toList()).orElse(restaurants);
    }

    /**
     * Search for menu items that can be delivered at a specific time, from a specified restaurant.
     * The method checks if the restaurant is open at the specified time and if the preparation time of the menu item is shorter than the delivery time.
     *
     * @param restaurant the restaurant from which the menu items are selected
     * @param time       the delivery time
     * @return a list of menu items that can be delivered on time
     */
    public List<MenuItem> searchItemsByDeliveryTime(IRestaurant restaurant, Optional<LocalDateTime> time) {
        // if no delivery time is specified, return all items
        if (time.isEmpty()) {
            return restaurant.getMenu().getItems();
        }
        // if the restaurant is not open at the specified time, return an empty list
        if (!isOpenAt(restaurant, time.get())) {
            return new ArrayList<>();
        }
        return restaurant.getMenu().getItems().stream()
                .filter(item -> TimeUtils.getNow().plusSeconds(item.getPrepTime()).isBefore(time.get()))
                .toList();
    }

    // Check if a restaurant is open at a given time
    private boolean isOpenAt(IRestaurant restaurant, LocalDateTime time) {
        if (restaurant.getOpeningTime().isEmpty()) {
            return false;
        }
        return time.isAfter(restaurant.getOpeningTime().get()) && time.isBefore(restaurant.getClosingTime().orElseThrow());
    }

    public Slot findSlotByStartTime(IRestaurant restaurant, LocalDateTime slotStartTime) {
        // Recherche le slot correspondant dans les slots du restaurant
        return restaurant.getSlots().stream()
                .filter(slot -> slot.getOpeningDate().equals(slotStartTime))
                .findFirst()
                .orElse(null);
    }

    public List<IRestaurant> getRestaurants() {
        return restaurants;
    }
}
