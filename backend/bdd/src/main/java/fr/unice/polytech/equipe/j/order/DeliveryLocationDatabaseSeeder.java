package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.order.dao.DeliveryLocationDAO;
import fr.unice.polytech.equipe.j.order.dto.DeliveryLocationDTO;
import fr.unice.polytech.equipe.j.order.mapper.DeliveryLocationMapper;

import java.util.UUID;

public class DeliveryLocationDatabaseSeeder {

    public static void seedDatabase() {
        // Location 1
        //  predefinedLocations.add(new DeliveryLocation("Campus Main Gate", "123 Campus Street"));
        //        predefinedLocations.add(new DeliveryLocation("Library", "456 Library Road"));
        //        predefinedLocations.add(new DeliveryLocation("Dormitory", "789 Dormitory Lane"));

        // Location 1
        DeliveryLocationDTO deliveryLocationDTO = new DeliveryLocationDTO(UUID.fromString("10a83413-1b4a-4184-b082-238d073e6126"),
                "Campus Main Gate",
                "123 Campus Street");
        DeliveryLocationDAO.save(DeliveryLocationMapper.toEntity(deliveryLocationDTO));
    }

    public static void main(String[] args) {
        seedDatabase();
    }
}
