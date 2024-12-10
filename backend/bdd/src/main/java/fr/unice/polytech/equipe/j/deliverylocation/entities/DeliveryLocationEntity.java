package fr.unice.polytech.equipe.j.deliverylocation.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "delivery_locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryLocationEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(name = "location_name", nullable = false, unique = true)
    private String locationName;

    @Column(nullable = false)
    private String address;

    @Override
    public String toString() {
        return "DeliveryLocationEntity{" +
                "id=" + id +
                ", locationName='" + locationName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

}