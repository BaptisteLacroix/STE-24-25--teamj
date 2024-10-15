package fr.unice.polytech.equipe.j.restaurant;

import java.time.LocalDateTime;
import java.util.UUID;

public class RestaurantFacade {
    private final UUID restaurantId;
    private final String restaurantName;
    private final Menu menu;
    private final LocalDateTime openingTime;
    private final LocalDateTime closingTime;

    public RestaurantFacade(Restaurant restaurant) {
        this.restaurantId = restaurant.getRestaurantId();
        this.restaurantName = restaurant.getRestaurantName();
        this.menu = restaurant.getMenu();
        this.openingTime = restaurant.getOpeningTime();
        this.closingTime = restaurant.getClosingTime();
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

    // Expose opening time
    public LocalDateTime getOpeningTime() {
        return openingTime;
    }

    // Expose closing time
    public LocalDateTime getClosingTime() {
        return closingTime;
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
