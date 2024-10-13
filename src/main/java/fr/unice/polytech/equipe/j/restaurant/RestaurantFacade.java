package fr.unice.polytech.equipe.j.restaurant;

import java.util.UUID;

public class RestaurantFacade {
    private final UUID restaurantId;
    private final String restaurantName;
    private final Menu menu;

    public RestaurantFacade(Restaurant restaurant) {
        this.restaurantId = restaurant.getRestaurantId();
        this.restaurantName = restaurant.getRestaurantName();
        this.menu = restaurant.getMenu();
    }

    // Expose restaurant name
    public String getRestaurantName() {
        return restaurantName;
    }

    // Expose restaurant ID
    public UUID getRestaurantId() {
        return restaurantId;
    }

    // Expose unmodifiable menu items
    public Menu getMenu() {
        return menu;
    }

    @Override
    public String toString() {
        return "RestaurantFacade{" +
                "restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", menu=" + menu +
                '}';
    }
}
