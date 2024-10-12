package fr.unice.polytech.equipe.j.slot;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Slot {
    private int currentCapacity;
    private int maxCapacity;
    private LocalDateTime openingDate;
    private Duration durationTime = Duration.ofMinutes(30);
    private int numberOfPersonnel; // Nombre de personnel pour ce slot
    private List<Order> orders;

    public Slot(int currentCapacity, int maxCapacity, LocalDateTime openingDate, int numberOfPersonnel) {
        this.currentCapacity = currentCapacity;
        this.maxCapacity = maxCapacity;
        this.openingDate = openingDate;
        this.numberOfPersonnel = numberOfPersonnel;
        this.orders = new ArrayList<>();
    }

    // Getters et Setters
    public int getNumberOfPersonnel() {
        return numberOfPersonnel;
    }

    public void setNumberOfPersonnel(int numberOfPersonnel) {
        this.numberOfPersonnel = numberOfPersonnel;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDateTime openingDate) {
        this.openingDate = openingDate;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }


    // Méthode pour calculer la capacité de production pour un item sur un slot
    public int calculateProductionCapacity(MenuItem item) {
        int prepTimeInSeconds = item.getPrepTime();
        int slotTimeInSeconds = (int) durationTime.getSeconds();
        return numberOfPersonnel * slotTimeInSeconds;

    }
}
