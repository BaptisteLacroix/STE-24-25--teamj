package fr.unice.polytech.equipe.j.order.entities;

import fr.unice.polytech.equipe.j.deliverylocation.entities.DeliveryLocationEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "delivery_details")
@Getter
@Setter
public class DeliveryDetailsEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    private DeliveryLocationEntity deliveryLocation;

    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;

    @Override
    public String toString() {
        return "DeliveryDetailsEntity{" +
                "id=" + id +
                ", deliveryLocation=" + deliveryLocation +
                ", deliveryTime=" + deliveryTime +
                '}';
    }
}
