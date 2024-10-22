package fr.unice.polytech.equipe.j.restaurant;
import java.util.Map;

public record OrderPrice(Map<MenuItem, Double> newPrices, double totalPrice, String description){

}
