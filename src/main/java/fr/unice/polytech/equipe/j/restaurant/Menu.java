package fr.unice.polytech.equipe.j.restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private final List<MenuItem> items;

    private Menu(List<MenuItem> items) {
        this.items = new ArrayList<>(items);
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
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
