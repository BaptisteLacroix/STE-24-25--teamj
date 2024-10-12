package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
    private Map<MenuItem, Integer> items; // Map pour stocker les MenuItems et leur quantité
    private User user;

    public Order(User u,Map<MenuItem,Integer> items) {
        this.user= u;
        this.items = items;
    }

    // Ajouter un item à la commande
    public void addItem(MenuItem item, int quantity) {
        items.put(item, quantity);
    }

    // Obtenir la liste des items
    public List<MenuItem> getMenuItems() {
        return List.copyOf(items.keySet()); // Retourner la liste des MenuItems
    }

    // Obtenir la quantité pour un MenuItem
    public int getQuantityForMenuItem(MenuItem item) {
        return items.getOrDefault(item, 0); // Retourner la quantité ou 0 si non trouvé
    }
}
