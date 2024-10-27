package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.slot.Slot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RestaurantManager extends User {
    private Restaurant restaurant;
    private String name;

    public RestaurantManager(String email, String password, String name, Restaurant restaurant) {
        super(email, password);
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

    /**
     * Mettre à jour les heures d'ouverture et de fermeture
     */
    
    public void updateHours(LocalDateTime openingHour, LocalDateTime closingHour) {
        if (openingHour != null) {
            this.restaurant.setOpeningTime(openingHour);
        }
        if (closingHour != null) {
            this.restaurant.setClosingTime(closingHour);
        }
    }

    /**
     *
     * Ajouter un nouvel élément au menu via le restaurant
     */
    
    public void addMenuItem(String itemName, int prepTimeInSeconds, int price) {
        MenuItem newItem = new MenuItem(itemName, prepTimeInSeconds, price);
        this.restaurant.getMenu().addMenuItem(newItem);
    }

    /**
     *  Supprimer un élément du menu
     */
    
    public void removeMenuItem(String itemName) {
        this.restaurant.getMenu().removeMenuItem(itemName);
    }

    /**
     * Mettre à jour le nombre de personnel pour un slot spécifique
      */
    
    public boolean updateNumberOfPersonnel(Slot slotToUpdate, int numberOfPersonnel) {
        if (slotToUpdate == null) {
            System.out.println("The slot isn't existing.");
            return false;
        }
        this.restaurant.setNumberOfPersonnel(slotToUpdate,numberOfPersonnel);
        return true;
    }
}
