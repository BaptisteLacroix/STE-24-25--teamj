package fr.unice.polytech.equipe.j.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SlotDTO {
    private UUID uuid;
    private int currentCapacity;
    private int maxCapacity;
    private LocalDateTime openingDate;
    private Duration durationTime;
    private int numberOfPersonnel;

    public SlotDTO() {
    }

    @JsonIgnore
    @Override
    public String toString() {
        return "SlotDTO{" +
                "uuid=" + uuid +
                ", currentCapacity=" + currentCapacity +
                ", maxCapacity=" + maxCapacity +
                ", openingDate=" + openingDate +
                ", durationTime=" + durationTime +
                ", numberOfPersonnel=" + numberOfPersonnel +
                '}';
    }
}
