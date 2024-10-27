package fr.unice.polytech.equipe.j.restaurant.orderpricestrategy;
import fr.unice.polytech.equipe.j.restaurant.menu.MenuItem;

import java.util.Map;

public record OrderPrice(Map<MenuItem, Double> newPrices, double totalPrice, String description){

}
