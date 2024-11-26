package fr.unice.polytech.equipe.j.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SlotDTO {
    private UUID uuid;
    private String openingDate;
    private int availableCapacity;
    private int numberOfPersonnel;

    public SlotDTO() {
    }
}
