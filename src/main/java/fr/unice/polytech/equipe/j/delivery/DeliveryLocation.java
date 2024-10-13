package fr.unice.polytech.equipe.j.delivery;

public class DeliveryLocation {
    private final String locationName;
    private final String address;

    public DeliveryLocation(String locationName, String address) {
        this.locationName = locationName;
        this.address = address;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "DeliveryLocation{" +
                "locationName='" + locationName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
