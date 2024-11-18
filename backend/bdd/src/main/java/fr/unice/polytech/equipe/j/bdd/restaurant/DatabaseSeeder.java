package fr.unice.polytech.equipe.j.bdd.restaurant;

import fr.unice.polytech.equipe.j.bdd.restaurant.dto.MenuDTO;
import fr.unice.polytech.equipe.j.bdd.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.bdd.restaurant.dto.RestaurantDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseSeeder {

    public static void seedDatabase() {
        // Restaurant 1
        List<MenuItemDTO> itemsRestaurant1 = List.of(
                new MenuItemDTO(UUID.randomUUID(), "Salade Nicoise", 60, 12.50),
                new MenuItemDTO(UUID.randomUUID(), "Bouillabaisse", 500, 25.00),
                new MenuItemDTO(UUID.randomUUID(), "Tarte Tatin", 1800, 8.00)
        );
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setItems(itemsRestaurant1);
        RestaurantDTO restaurantDTO = new RestaurantDTO(UUID.randomUUID(), "Le Petit Nice", null, null, new ArrayList<>(), menuDTO);


        // Restaurant 2
        List<MenuItemDTO> itemsRestaurant2 = List.of(
                new MenuItemDTO(UUID.randomUUID(), "Salade de chèvre chaud", 400, 10.00),
                new MenuItemDTO(UUID.randomUUID(), "Magret de canard", 1800, 20.00),
                new MenuItemDTO(UUID.randomUUID(), "Crème brûlée", 600, 7.00)
        );
        menuDTO = new MenuDTO();
        menuDTO.setItems(itemsRestaurant2);
        restaurantDTO = new RestaurantDTO(UUID.randomUUID(), "Le Petit Jardin", null, null, new ArrayList<>(), menuDTO);

        // Restaurant 3
        List<MenuItemDTO> itemsRestaurant3 = List.of(
                new MenuItemDTO(UUID.randomUUID(), "Escargots", 1800, 15.00),
                new MenuItemDTO(UUID.randomUUID(), "Coq au vin", 1800, 22.00),
                new MenuItemDTO(UUID.randomUUID(), "Mousse au chocolat", 1800, 6.00)
        );
        menuDTO = new MenuDTO();
        menuDTO.setItems(itemsRestaurant3);
        restaurantDTO = new RestaurantDTO(UUID.randomUUID(), "Le Petit Chateau", null, null, new ArrayList<>(), menuDTO);

        // Restaurant A (closed)
        List<MenuItemDTO> itemsRestaurantA = List.of(
                new MenuItemDTO(UUID.randomUUID(), "Soupe à l'oignon", 300, 8.50),
                new MenuItemDTO(UUID.randomUUID(), "Boeuf Bourguignon", 1500, 22.00),
                new MenuItemDTO(UUID.randomUUID(), "Tarte Tatin", 500, 6.50)
        );
        menuDTO = new MenuDTO();
        menuDTO.setItems(itemsRestaurantA);
        restaurantDTO = new RestaurantDTO(UUID.randomUUID(), "Le Gourmet D'Or", null, null, new ArrayList<>(), menuDTO);

        // Restaurant B (no personnel)
        List<MenuItemDTO> itemsRestaurantB = List.of(
                new MenuItemDTO(UUID.randomUUID(), "Quiche Lorraine", 400, 9.00),
                new MenuItemDTO(UUID.randomUUID(), "Ratatouille", 800, 12.00),
                new MenuItemDTO(UUID.randomUUID(), "Mousse au chocolat", 350, 5.00)
        );
        menuDTO = new MenuDTO();
        menuDTO.setItems(itemsRestaurantB);
        restaurantDTO = new RestaurantDTO(UUID.randomUUID(), "Bistro de la Plage", null, null, new ArrayList<>(), menuDTO);

        // Restaurant C (no menu)
        menuDTO = new MenuDTO();
        restaurantDTO = new RestaurantDTO(UUID.randomUUID(), "Café de l'Aube", null, null, new ArrayList<>(), menuDTO);

        // Restaurant D (limited personnel)
        List<MenuItemDTO> itemsRestaurantD = List.of(
                new MenuItemDTO(UUID.randomUUID(), "Coq au Vin", 1200, 18.00),
                new MenuItemDTO(UUID.randomUUID(), "Bouillabaisse", 1800, 25.00),
                new MenuItemDTO(UUID.randomUUID(), "Crêpe Suzette", 400, 7.50)
        );
        menuDTO = new MenuDTO();
        menuDTO.setItems(itemsRestaurantD);
        restaurantDTO = new RestaurantDTO(UUID.randomUUID(), "La Table Royale", null, null, new ArrayList<>(), menuDTO);
    }
}
