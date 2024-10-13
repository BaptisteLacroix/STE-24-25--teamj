package fr.unice.polytech.equipe.j.restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantServiceManager {
    private List<Restaurant> restaurants;

    public RestaurantServiceManager() {
        this.restaurants = new ArrayList<>();
    }

    // Add restaurant to the manager
    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    // Remove restaurant from the manager
    public void removeRestaurant(Restaurant restaurant) {
        restaurants.remove(restaurant);
    }

    // Search restaurants by name (exact or partial match)
    public List<Restaurant> searchByName(String name) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getRestaurantName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Search restaurants by type of food (based on menu items)
    public List<Restaurant> searchByTypeOfFood(String foodType) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getMenu().getItems().stream()
                        .anyMatch(item -> item.name().equalsIgnoreCase(foodType)))
                .collect(Collectors.toList());
    }

    // Search restaurants by availability (check if open at a specific time)
    public List<Restaurant> searchByAvailability(LocalDateTime time) {
        return restaurants.stream()
                .filter(restaurant -> isOpenAt(restaurant, time))
                .collect(Collectors.toList());
    }

    // Check if a restaurant is open at a given time
    private boolean isOpenAt(Restaurant restaurant, LocalDateTime time) {
        return time.isAfter(restaurant.getOpeningTime()) && time.isBefore(restaurant.getClosingTime());
    }

    // Search restaurants that are open now
    public List<Restaurant> searchByCurrentAvailability() {
        LocalDateTime now = LocalDateTime.now();
        return searchByAvailability(now);
    }
}
