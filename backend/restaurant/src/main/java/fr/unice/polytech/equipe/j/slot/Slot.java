package fr.unice.polytech.equipe.j.slot;

import fr.unice.polytech.equipe.j.menu.MenuItem;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Slot {
    private UUID uuid;
    private int currentCapacity;
    private int maxCapacity;
    private LocalDateTime openingDate;
    private Duration durationTime;
    private int numberOfPersonnel;

    /**
     * Constructor for the Slot class.
     *
     * @param openingDate       Date and time the slot opens.
     * @param numberOfPersonnel Number of personnel available in this slot.
     */
    public Slot(UUID uuid, LocalDateTime openingDate, int numberOfPersonnel) {
        this.uuid = uuid;
        this.currentCapacity = 0;
        this.openingDate = openingDate;
        this.numberOfPersonnel = numberOfPersonnel;
        this.durationTime = Duration.ofMinutes(30);
        // Initialize slot capacity based on the personnel and the average preparation time.
        this.calculateCapacityForASlot(this.getNumberOfPersonnel());
    }

    /**
     * Adds capacity to the current slot capacity when an item is prepared.
     *
     * @param newCapacity Capacity in seconds to add.
     */
    public void addCapacity(int newCapacity) {
        currentCapacity = currentCapacity + newCapacity;
    }

    /**
     * Calculates the maximum capacity of this slot based on the number of personnel.
     * Each personnel can handle 1800 seconds (30 minutes) of preparation time.
     *
     * @param numberOfPersonnel Number of personnel available during this slot.
     */
    public void calculateCapacityForASlot(int numberOfPersonnel) {
        setMaxCapacity(1800 * numberOfPersonnel);
    }

    /**
     * Updates the slot capacity when an item is added to the order.
     * Checks if there is enough space available in the slot to add the item.
     *
     * @param item The menu item that will be added to the order.
     * @return true if the item can be added (i.e., capacity is sufficient), false otherwise.
     */
    public boolean updateSlotCapacity(MenuItem item) {
        int itemPrepTime = item.getPrepTime();
        if (getCurrentCapacity() + itemPrepTime < getMaxCapacity()) {
            addCapacity(itemPrepTime);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the remaining available capacity in this slot.
     *
     * @return The available capacity in seconds.
     */
    public int getAvailableCapacity() {
        return this.maxCapacity - this.currentCapacity;
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

    public void setCurrentCapacity(int capacity){
        this.currentCapacity = capacity;
    }

    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDateTime openingDate) {
        this.openingDate = openingDate;
    }

    public Duration getDurationTime() {
        return durationTime;
    }

    public void setAvailableCapacity(int availableCapacity) {
        this.currentCapacity = availableCapacity;
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "uuid=" + uuid +
                ", currentCapacity=" + currentCapacity +
                ", maxCapacity=" + maxCapacity +
                ", openingDate=" + openingDate +
                ", durationTime=" + durationTime +
                ", numberOfPersonnel=" + numberOfPersonnel +
                '}';
    }
}
