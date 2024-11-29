package fr.unice.polytech.equipe.j.restaurant.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
public class RestaurantEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private LocalDateTime openingTime;

    @Column
    private LocalDateTime closingTime;

    @OneToOne(cascade = CascadeType.ALL)
    private MenuEntity menuEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurantEntity", fetch = FetchType.EAGER)
    private List<SlotEntity> slotEntities = new ArrayList<>();

    @Override
    @JsonIgnore
    public String toString() {
        return "RestaurantEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                ", menuEntity=" + menuEntity +
                ", slotEntities=" + slotEntities +
                '}';
    }
}
