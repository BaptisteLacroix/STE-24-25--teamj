package fr.unice.polytech.equipe.j.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeliveryLocationDTO {
    private UUID id;
    private String locationName;
    private String address;

    public DeliveryLocationDTO(UUID id, String locationName, String address) {
        this.id = id;
        this.locationName = locationName;
        this.address = address;
    }

    public DeliveryLocationDTO() {
    }

    @Override
    public String toString() {
        return "DeliveryLocationDTO{" +
                "id=" + id +
                ", locationName='" + locationName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
