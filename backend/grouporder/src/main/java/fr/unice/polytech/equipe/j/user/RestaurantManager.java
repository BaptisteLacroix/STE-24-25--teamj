package java.fr.unice.polytech.equipe.j.user;

import java.fr.unice.polytech.equipe.j.restaurant.IRestaurant;

import fr.unice.polytech.equipe.j.slot.Slot;

public class RestaurantManager {
    private IRestaurant restaurant;
    private final String email;
    private final String password;
    private String name;

    public RestaurantManager(String email, String password, String name, IRestaurant restaurant) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.restaurant = restaurant;
    }

    public IRestaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(IRestaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getName() {
        return this.name;
    }
}
