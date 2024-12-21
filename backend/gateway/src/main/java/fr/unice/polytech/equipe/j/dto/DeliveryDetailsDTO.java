package fr.unice.polytech.equipe.j.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.unice.polytech.equipe.j.utils.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class DeliveryDetailsDTO {
    private UUID id;
    private DeliveryLocationDTO deliveryLocation;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime deliveryTime;

    public DeliveryDetailsDTO() {


    }

    public DeliveryDetailsDTO(UUID id, DeliveryLocationDTO deliveryLocation, LocalDateTime deliveryTime) {
        this.id = id;
        this.deliveryLocation = deliveryLocation;
        this.deliveryTime = deliveryTime;
    }

    @Override
    public String toString() {
        return "DeliveryDetailsDTO{" +
                "id=" + id +
                ", deliveryLocation=" + deliveryLocation +
                ", deliveryTime=" + deliveryTime +
                '}';
    }
}
