package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.GroupOrder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                .toList();
    }

    // Search restaurants by type of food (based on menu items)
    public List<Restaurant> searchByTypeOfFood(String foodType) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getMenu().getItems().stream()
                        .anyMatch(item -> item.getName().contains(foodType)))
                .distinct()
                .toList();
    }

    // Search restaurants by availability (check if open at a specific time)
    public List<Restaurant> searchByAvailability(LocalDateTime time) {
        return restaurants.stream()
                .filter(restaurant -> isOpenAt(restaurant, time))
                .distinct()
                .toList();
    }

    /**
     * Search for restaurants with a menuItem which, at the end of the preparation time, is shorter than the group order delivery time.
     *
     * @param groupOrder the group order for which the restaurants are searched
     * @return a list of restaurants that can deliver the order on time
     */
    public List<Restaurant> searchRestaurantByDeliveryTime(GroupOrder groupOrder) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getMenu().getItems().stream()
                        .anyMatch(item -> LocalDateTime.now().plusSeconds(item.getPrepTime()).isBefore(groupOrder.getDeliveryDetails().getDeliveryTime().get())))
                .toList();
    }

    /**
     * Search for menu items that can be delivered at a specific time, from a specified restaurant.
     * The method checks if the restaurant is open at the specified time and if the preparation time of the menu item is shorter than the delivery time.
     *
     * @param restaurant the restaurant from which the menu items are selected
     * @param time       the delivery time
     * @return a list of menu items that can be delivered on time
     */
    public List<MenuItem> searchItemsByDeliveryTime(Restaurant restaurant, LocalDateTime time) {
        // if no delivery time is specified, return all items
        if (time == null) {
            return restaurant.getMenu().getItems();
        }
        // if the restaurant is not open at the specified time, return an empty list
        if (!isOpenAt(restaurant, time)) {
            return new ArrayList<>();
        }
        return restaurant.getMenu().getItems().stream()
                .filter(item -> LocalDateTime.now().plusSeconds(item.getPrepTime()).isBefore(time))
                .toList();
    }

    // Check if a restaurant is open at a given time
    private boolean isOpenAt(Restaurant restaurant, LocalDateTime time) {
        return time.isAfter(restaurant.getOpeningTime()) && time.isBefore(restaurant.getClosingTime());
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
}
