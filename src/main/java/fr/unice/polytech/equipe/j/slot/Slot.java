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
    private Duration durationTime;
    private int numberOfPersonnel;
    private List<Order> orders;

    public Slot(int currentCapacity, int maxCapacity, LocalDateTime openingDate, int numberOfPersonnel) {
        this.currentCapacity = currentCapacity;
        this.maxCapacity = maxCapacity;
        this.openingDate = openingDate;
        this.numberOfPersonnel = numberOfPersonnel;
        this.orders = new ArrayList<>();
        this.durationTime = Duration.ofMinutes(30);

    }

    // Getters et Setters
    public int getNumberOfPersonnel() {
        return numberOfPersonnel;
    }

    public void setNumberOfPersonnel(int numberOfPersonnel) {
        this.numberOfPersonnel = numberOfPersonnel;
        calculateCapacityForASlot(numberOfPersonnel);
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void addCapacity(int newCapacity) {
        currentCapacity = currentCapacity + newCapacity;
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

    public Duration getDurationTime(){
        return durationTime;
    }

    public void calculateCapacityForASlot(int numberOfPersonnel){
        setMaxCapacity(1800*numberOfPersonnel);
    }

    public boolean UpdateSlotCapacity(MenuItem item) {
        int itemCapacity = item.getPrepTime();
        if(getCurrentCapacity()+itemCapacity < getMaxCapacity()){
            addCapacity(itemCapacity);
            return true;
        } else {
            return false;
        }
    }

    // Méthode permettant de récupérer la capacité utilisable
    public int getAvailableCapacity(){
        return this.maxCapacity - this.currentCapacity;
    }

}