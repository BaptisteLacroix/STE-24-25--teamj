package fr.unice.polytech.equipe.j.restaurant.backend.orderpricestrategy;
import fr.unice.polytech.equipe.j.restaurant.backend.menu.MenuItem;

import java.util.Map;

public record OrderPrice(Map<MenuItem, Double> newPrices, double totalPrice, String description){

}
