package fr.unice.polytech.equipe.j.orderpricestrategy;
import fr.unice.polytech.equipe.j.menu.MenuItem;

import java.util.Map;

public record OrderPrice(Map<MenuItem, Double> newPrices, double totalPrice, String description){

}
