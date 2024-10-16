package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderBuilder;
import fr.unice.polytech.equipe.j.slot.Slot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Restaurant {
    private final UUID restaurantId = UUID.randomUUID();
    private final String restaurantName;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private List<Slot> slots;
    private Menu menu;
    private final List<Order> orders = new ArrayList<>();
    private int numberOfPersonnel;

    public Restaurant(String name, List<Slot> slots, LocalDateTime openingTime, LocalDateTime closingTime, Menu menu) {
        this.restaurantName = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.menu = menu;
        this.slots = slots;
    }

    public void changeMenu(Menu newMenu) {
        this.menu = newMenu;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu){
        this.menu = menu;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public LocalDateTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalDateTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalDateTime closingTime) {
        this.closingTime = closingTime;
    }

    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Create and return an OrderBuilder for the restaurant
     *
     * @return The OrderBuilder instance
     */
    public OrderBuilder createOrderBuilder() {
        return new OrderBuilder().setRestaurant(this);
    }

    public double calculatePrice(Order order) {
        return order.getItems().stream()
                .mapToDouble(MenuItem::getPrice)
                .sum();
    }

    public boolean isItemAvailable(MenuItem item) {
        return menu.getItems().contains(item);
    }

    public void addItemToOrder(OrderBuilder orderBuilder, MenuItem item) {
        if (orderBuilder.getRestaurant() != this) {
            throw new IllegalArgumentException("OrderBuilder is not for this restaurant");
        }
        if (isItemAvailable(item)) {
            orderBuilder.addMenuItem(item);
        } else {
            throw new IllegalArgumentException("MenuItem " + item.getName() + " is not available.");
        }
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void setNumberOfPersonnel(Slot slotToUpdate, int numberOfPersonnel) {
         if (slotToUpdate != null) {
            slotToUpdate.setNumberOfPersonnel(numberOfPersonnel);
        }
    }

    public int getNumberOfPersonnel() {
        return numberOfPersonnel;
    }
}
