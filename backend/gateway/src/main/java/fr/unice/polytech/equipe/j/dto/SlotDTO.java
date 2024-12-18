package fr.unice.polytech.equipe.j.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SlotDTO {
    @NonNull
    private UUID uuid;
    private int currentCapacity;
    private int maxCapacity;
    private LocalDateTime openingDate;
    private Duration durationTime;
    private int numberOfPersonnel;

    public SlotDTO() {
    }

    public SlotDTO(UUID uuid, int currentCapacity, int maxCapacity, LocalDateTime openingDate, Duration durationTime, int numberOfPersonnel) {
        this.uuid = uuid;
        this.currentCapacity = currentCapacity;
        this.maxCapacity = maxCapacity;
        this.openingDate = openingDate;
        this.durationTime = durationTime;
        this.numberOfPersonnel = numberOfPersonnel;
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
