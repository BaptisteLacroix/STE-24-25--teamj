package fr.unice.polytech.equipe.j.restaurant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class RestaurantFactory {
    public static Restaurant createRestaurant(String name, String openingTime, String closingTime, Menu menu) throws ParseException {
        Date opening = new SimpleDateFormat("HH:mm").parse(openingTime);
        Date closing = new SimpleDateFormat("HH:mm").parse(closingTime);
        LocalDateTime openingDateTime = opening.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime closingDateTime = closing.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return new Restaurant(name, openingDateTime, closingDateTime, menu);
    }

    public static MenuItem createMenuItem(String itemName, double price) {
        return new MenuItem(itemName, price);
    }
}
