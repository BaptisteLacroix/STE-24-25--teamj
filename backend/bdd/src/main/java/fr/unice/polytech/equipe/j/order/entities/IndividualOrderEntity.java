package fr.unice.polytech.equipe.j.order.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "individual_orders")
@Getter
@Setter
public class IndividualOrderEntity extends OrderEntity {
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private DeliveryDetailsEntity deliveryDetails;


    @Override
    public String toString() {
        return "IndividualOrderEntity{" +
                super.toString() +
                ", deliveryDetails=" + deliveryDetails +
                '}';
    }
}
