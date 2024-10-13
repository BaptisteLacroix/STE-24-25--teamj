package fr.unice.polytech.equipe.j.restaurant;

public record MenuItem(String name, double price) {
    @Override
    public String toString() {
        return this.name + " - " + this.price + " EUR";
    }
}
