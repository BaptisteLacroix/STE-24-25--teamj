package fr.unice.polytech.equipe.j.restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantServiceManager {

    // Static instance (Singleton)
    private static RestaurantServiceManager instance;

    // List of restaurants managed by the service
    private final List<Restaurant> restaurants;

    // Private constructor to prevent external instantiation
    private RestaurantServiceManager() {
        this.restaurants = new ArrayList<>();
    }

    static void resetInstance() {
        instance = null;
    }

    // Static method to get the single instance (Singleton pattern)
    public static RestaurantServiceManager getInstance() {
        if (instance == null) {
            instance = new RestaurantServiceManager();
        }
        return instance;
    }

    // Add restaurant to the manager (Package-Private)
    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    // Remove restaurant from the manager (Package-Private)
    public void removeRestaurant(Restaurant restaurant) {
        restaurants.remove(restaurant);
    }

    // --- Public Search Methods ---

    // Search restaurants by name (exact or partial match)
    public List<Restaurant> searchByName(String name) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getRestaurantName().toLowerCase().contains(name.toLowerCase()))
                .distinct()
                .collect(Collectors.toList());
    }

    // Search restaurants by type of food (based on menu items)
    public List<Restaurant> searchByTypeOfFood(String foodType) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getMenu().getItems().stream()
                        .anyMatch(item -> item.getName().contains(foodType)))
                .distinct()
                .collect(Collectors.toList());
    }

    // Search restaurants by availability (check if open at a specific time)
    public List<Restaurant> searchByAvailability(LocalDateTime time) {
        return restaurants.stream()
                .filter(restaurant -> isOpenAt(restaurant, time))
                .distinct()
                .collect(Collectors.toList());
    }

    // Check if a restaurant is open at a given time
    private boolean isOpenAt(Restaurant restaurant, LocalDateTime time) {
        return time.isAfter(restaurant.getOpeningTime()) && time.isBefore(restaurant.getClosingTime());
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
}
