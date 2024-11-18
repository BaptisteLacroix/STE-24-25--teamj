package fr.unice.polytech.equipe.j.bdd.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SlotDTO {
    private UUID id;
    private int currentCapacity;
    private int maxCapacity;
    private LocalDateTime openingDate;
    private Duration durationTime;
    private int numberOfPersonnel;

    // Constructors, Getters, and Setters
    public SlotDTO() {
    }

    public SlotDTO(UUID id, int currentCapacity, int maxCapacity, LocalDateTime openingDate, Duration durationTime, int numberOfPersonnel) {
        this.id = id;
        this.currentCapacity = currentCapacity;
        this.maxCapacity = maxCapacity;
        this.openingDate = openingDate;
        this.durationTime = durationTime;
        this.numberOfPersonnel = numberOfPersonnel;
    }
}
