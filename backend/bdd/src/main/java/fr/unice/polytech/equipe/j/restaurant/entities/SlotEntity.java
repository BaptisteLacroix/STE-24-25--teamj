package fr.unice.polytech.equipe.j.restaurant.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.unice.polytech.equipe.j.order.entities.OrderEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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

    @OneToMany(mappedBy = "slotEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<OrderEntity> orders = new HashSet<>();

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
                ", orders=" + orders +
                '}';
    }

}
