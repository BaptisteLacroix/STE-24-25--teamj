package fr.unice.polytech.equipe.j.restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private final List<MenuItem> items;

    public Menu(List<MenuItem> items) {
        this.items = new ArrayList<>(items);
    }

    public void addMenuItem(MenuItem item) {
        items.add(item);
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    // Méthode pour mettre à jour le temps de préparation d'un item
    public void updateMenuItemPrepTime(String itemName, int newPrepTimeInSeconds) {
        MenuItem item = findItemByName(itemName);
        if (item != null) {
            item.setPrepTime(newPrepTimeInSeconds);
        } else {
            throw new IllegalArgumentException("Item " + itemName + " not found in menu.");
        }
    }

    // Méthode pour mettre à jour le prix d'un item
    public void updateMenuItemPrice(String itemName, double newPrice) {
        MenuItem item = findItemByName(itemName);
        if (item != null) {
            item.setPrice(newPrice);
        } else {
            throw new IllegalArgumentException("Item " + itemName + " not found in menu.");
        }
    }

    // Méthode pour supprimer un item du menu
    public void removeMenuItem(String itemName) {
        MenuItem item = findItemByName(itemName);
        if (item != null) {
            items.remove(item);
        } else {
            throw new IllegalArgumentException("Item " + itemName + " not found in menu.");
        }
    }

    public MenuItem findItemByName(String name) {
        return items.stream()
                .filter(item -> item.getName().equals(name))
                .findFirst()
                .orElse(null);
    }




    @Override
    public String toString() {
        return "Menu: " + items;
    }

    public static class MenuBuilder {
        private final List<MenuItem> items = new ArrayList<>();

        public MenuBuilder addMenuItem(MenuItem item) {
            items.add(item);
            return this;
        }
            public MenuBuilder addMenuItems(List<MenuItem> items) {
                this.items.addAll(items);
                return this;
            }
            public Menu build() {
                return new Menu(items);
            }
        }




}

