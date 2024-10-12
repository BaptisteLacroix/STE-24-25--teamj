package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.slot.Slot;

import java.time.LocalDateTime;
import java.util.List;

public class Restaurant {
    private String name;
    private String discountStrategies;
    private List<Slot> slots;
    private LocalDateTime openingHour;
    private LocalDateTime closingHour;
    private Menu menu;

    public Restaurant(String name, List<Slot> slots, String discountStrategies, LocalDateTime openingHour, LocalDateTime closingHour, Menu menu) {
        this.name = name;
        this.slots = slots;
        this.discountStrategies = discountStrategies;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.menu = menu;
    }

    // Getters et Setters
    public LocalDateTime getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(LocalDateTime openingHour) {
        this.openingHour = openingHour;
    }

    public LocalDateTime getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(LocalDateTime closingHour) {
        this.closingHour = closingHour;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu){
        this.menu = menu;
    }
}
