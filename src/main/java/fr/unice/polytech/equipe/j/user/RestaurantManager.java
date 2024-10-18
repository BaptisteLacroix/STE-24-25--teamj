package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.slot.Slot;

import java.time.LocalDateTime;

public class RestaurantManager extends User {
    private Restaurant restaurant;
    private String name;

    public RestaurantManager(String email, String password, double accountBalance, String name, Restaurant restaurant) {
        super(email, password, accountBalance);
        this.name = name;
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getName() {
        return this.name;
    }

    // Mettre à jour les heures d'ouverture et de fermeture
    public void updateHours(LocalDateTime openingHour, LocalDateTime closingHour) {
        if (openingHour != null) {
            this.restaurant.setOpeningTime(openingHour);
        }
        if (closingHour != null) {
            this.restaurant.setClosingTime(closingHour);
        }
    }

    // Ajouter un nouvel élément au menu via le restaurant
    public void addMenuItem(String itemName, String description, int prepTimeInSeconds, int price, int capacity) {
        MenuItem newItem = new MenuItem(itemName, description, prepTimeInSeconds, price, capacity);
        this.restaurant.getMenu().addMenuItem(newItem);
    }

    // Mettre à jour le temps de préparation d'un élément existant
    public void updateMenuItemPrepTime(String itemName, int newPrepTimeInSeconds) {
        this.restaurant.getMenu().updateMenuItemPrepTime(itemName, newPrepTimeInSeconds);
    }

    public void updateMenuItemPrice(String itemName, int newPrice) {
        this.restaurant.getMenu().updateMenuItemPrice(itemName, newPrice);
    }

    // Supprimer un élément du menu
    public void removeMenuItem(String itemName) {
        this.restaurant.getMenu().removeMenuItem(itemName);
    }

    // Mettre à jour le nombre de personnel pour un slot spécifique
    public void updateNumberOfPersonnel(Slot slotToUpdate, int numberOfPersonnel) {
        this.restaurant.setNumberOfPersonnel(slotToUpdate,numberOfPersonnel);
    }
}
