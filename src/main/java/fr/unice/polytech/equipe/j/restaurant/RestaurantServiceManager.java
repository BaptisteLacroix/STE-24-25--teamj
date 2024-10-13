package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    // Static method to get the single instance (Singleton pattern)
    public static RestaurantServiceManager getInstance() {
        if (instance == null) {
            instance = new RestaurantServiceManager();
        }
        return instance;
    }

    // --- Management Methods (Package-Private) ---

    // Add restaurant to the manager (Package-Private)
    void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    // Remove restaurant from the manager (Package-Private)
    void removeRestaurant(Restaurant restaurant) {
        restaurants.remove(restaurant);
    }

    Restaurant getRestaurant(UUID restaurantId) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getRestaurantId().equals(restaurantId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
    }

    // --- Public Search Methods ---

    // Search restaurants by name (exact or partial match)
    public List<RestaurantFacade> searchByName(String name) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getRestaurantName().toLowerCase().contains(name.toLowerCase()))
                .map(RestaurantFacade::new)
                .collect(Collectors.toList());
    }

    // Search restaurants by type of food (based on menu items)
    public List<RestaurantFacade> searchByTypeOfFood(String foodType) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getMenu().getItems().stream()
                        .anyMatch(item -> item.getName().equalsIgnoreCase(foodType)))
                .map(RestaurantFacade::new)
                .collect(Collectors.toList());
    }

    // Search restaurants by availability (check if open at a specific time)
    public List<RestaurantFacade> searchByAvailability(LocalDateTime time) {
        return restaurants.stream()
                .filter(restaurant -> isOpenAt(restaurant, time))
                .map(RestaurantFacade::new)
                .collect(Collectors.toList());
    }

    // Check if a restaurant is open at a given time
    private boolean isOpenAt(Restaurant restaurant, LocalDateTime time) {
        return time.isAfter(restaurant.getOpeningTime()) && time.isBefore(restaurant.getClosingTime());
    }

    /**
     * Calculate the price of an order from a restaurant.
     * @param restaurantId The ID of the restaurant
     * @param order The order to calculate the price for
     * @return The total price of the order
     */
    public double calculateOrderPriceFromRestaurant(UUID restaurantId, Order order) {
        return getRestaurant(restaurantId).calculatePrice(order);
    }

    public Order getOrder(UUID orderUUID) {
        return restaurants.stream()
                .flatMap(restaurant -> restaurant.getOrders().stream())
                .filter(order -> order.getOrderUUID().equals(orderUUID))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
}
