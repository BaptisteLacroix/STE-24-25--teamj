package fr.unice.polytech.equipe.j.delivery;

import java.util.ArrayList;
import java.util.List;

public class DeliveryLocationManager {
    private final List<DeliveryLocation> predefinedLocations;

    private static DeliveryLocationManager instance;

    /**
     * Create a new DeliveryLocationManager
     */
    private DeliveryLocationManager() {
        predefinedLocations = new ArrayList<>();
        predefinedLocations.add(new DeliveryLocation("Campus Main Gate", "123 Campus Street"));
        predefinedLocations.add(new DeliveryLocation("Library", "456 Library Road"));
        predefinedLocations.add(new DeliveryLocation("Dormitory", "789 Dormitory Lane"));
    }

    /**
     * Get the instance of the DeliveryLocationManager
     *
     * @return The instance of the DeliveryLocationManager
     */
    public static DeliveryLocationManager getInstance() {
        if (instance == null) {
            instance = new DeliveryLocationManager();
        }
        return instance;
    }

    /**
     * Get the predefined delivery locations
     *
     * @return The predefined delivery locations
     */
    public List<DeliveryLocation> getPredefinedLocations() {
        return predefinedLocations;
    }

    /**
     * Find a location by name
     *
     * @param locationName The name of the location
     * @return The location
     */
    public DeliveryLocation findLocationByName(String locationName) throws IllegalArgumentException {
        return predefinedLocations.stream()
                .filter(location -> location.locationName().equalsIgnoreCase(locationName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Location not found: " + locationName));
    }
}
