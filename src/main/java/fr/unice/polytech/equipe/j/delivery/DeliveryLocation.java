package fr.unice.polytech.equipe.j.delivery;

public record DeliveryLocation(String locationName, String address) {

    @Override
    public String toString() {
        return "DeliveryLocation{" +
                "locationName='" + locationName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
