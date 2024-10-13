package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;

import java.util.List;

public class Order {
    public final List<MenuItem> items;
    public final Restaurant restaurant;

    public Order(List<MenuItem> items, Restaurant restaurant) {
        this.items = items;
        this.restaurant = restaurant;
    }
}
