package fr.unice.polytech.equipe.j.restaurant.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "slots")
@Getter
@Setter
public class SlotEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private int currentCapacity;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private LocalDateTime openingDate;

    @Column(nullable = false)
    private Duration durationTime;

    @Column(nullable = false)
    private int numberOfPersonnel;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurantEntity;

    @Override
    @JsonIgnore
    public String toString() {
        return "SlotEntity{" +
                "id=" + id +
                ", currentCapacity=" + currentCapacity +
                ", maxCapacity=" + maxCapacity +
                ", openingDate=" + openingDate +
                ", durationTime=" + durationTime +
                ", numberOfPersonnel=" + numberOfPersonnel +
                '}';
    }

}
