package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.dto.OrderDTO;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.SlotDTO;
import fr.unice.polytech.equipe.j.restaurant.mapper.RestaurantMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RestaurantDatabaseSeeder {

    public static void seedDatabase() {
        // Restaurant 1
        List<MenuItemDTO> itemsRestaurant1 = List.of(
                new MenuItemDTO(UUID.fromString("a3cc6194-4ca8-49e4-859b-0cbff038a079"), "Salade Nicoise", 60, 12.50, "Appetizer"),
                new MenuItemDTO(UUID.fromString("52945451-330c-4547-ac12-d8fc6d41f816"), "Bouillabaisse", 500, 25.00, "Main Course"),
                new MenuItemDTO(UUID.fromString("9e80486b-b011-4f81-9994-a85cce57272c"), "Tarte Tatin", 1800, 8.00, "Dessert")
        );
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setUuid(UUID.fromString("0c73b70f-dab9-467a-b4a2-fcdc3f1b8e91"));
        menuDTO.setItems(itemsRestaurant1);
        RestaurantDTO restaurantDTO = new RestaurantDTO(UUID.fromString("1a66eb77-3b5f-40e8-ae72-1ea2e5602f01"), "Le Petit Nice", LocalDateTime.now(), LocalDateTime.now().plusHours(4), new ArrayList<>(), menuDTO, new LinkedHashMap<>());
        generateSlots(restaurantDTO, restaurantDTO.getOpeningTime(), restaurantDTO.getClosingTime());
        RestaurantDAO.save(RestaurantMapper.toEntity(restaurantDTO));

        // Restaurant 2
        List<MenuItemDTO> itemsRestaurant2 = List.of(
                new MenuItemDTO(UUID.fromString("69097803-0ada-4046-8a48-04cc167e5f20"), "Salade de chèvre chaud", 400, 10.00, "Appetizer"),
                new MenuItemDTO(UUID.fromString("f24c0804-1940-4fab-885f-264cc933facf"), "Magret de canard", 1800, 20.00, "Main Course"),
                new MenuItemDTO(UUID.fromString("5301a9f0-1c71-4398-a3fb-d0dc98aba441"), "Crème brûlée", 600, 7.00, "Dessert")
        );
        menuDTO = new MenuDTO();
        menuDTO.setUuid(UUID.fromString("26dd780b-2d0e-4fbd-8422-2776efb2263f"));
        menuDTO.setItems(itemsRestaurant2);
        restaurantDTO = new RestaurantDTO(UUID.fromString("df59855f-8572-4435-9bdf-6b36241cc5b6"), "Le Petit Jardin", null, null, new ArrayList<>(), menuDTO, new LinkedHashMap<>());

        RestaurantDAO.save(RestaurantMapper.toEntity(restaurantDTO));

        // Restaurant 3
        List<MenuItemDTO> itemsRestaurant3 = List.of(
                new MenuItemDTO(UUID.fromString("ab645a4d-b3ac-4a2a-9a49-29e2786b04a2"), "Escargots", 1800, 15.00, "Appetizer"),
                new MenuItemDTO(UUID.fromString("2fb48cbe-6808-4fb7-b2ef-f4d54b15e571"), "Coq au vin", 1800, 22.00, "Main Course"),
                new MenuItemDTO(UUID.fromString("c0e98ff2-d25e-434e-94ed-4045ef1653e0"), "Mousse au chocolat", 1800, 6.00, "Dessert")
        );
        menuDTO = new MenuDTO();
        menuDTO.setUuid(UUID.fromString("d273b66b-5f22-435f-8ccb-a63edf95dda8"));
        menuDTO.setItems(itemsRestaurant3);
        restaurantDTO = new RestaurantDTO(UUID.fromString("c961543e-c1a1-4e6c-b71a-6b8f56647ca1"), "Le Petit Chateau", LocalDateTime.now(), LocalDateTime.now().plusHours(1), new ArrayList<>(), menuDTO, new LinkedHashMap<>());
        generateSlots(restaurantDTO, restaurantDTO.getOpeningTime(), restaurantDTO.getClosingTime());
        RestaurantDAO.save(RestaurantMapper.toEntity(restaurantDTO));

        // Restaurant A (closed)
        List<MenuItemDTO> itemsRestaurantA = List.of(
                new MenuItemDTO(UUID.fromString("341eeaac-c0bf-4552-8fae-2d7fe14fdf07"), "Soupe à l'oignon", 300, 8.50, "Appetizer"),
                new MenuItemDTO(UUID.fromString("5bf9af6b-4da2-4fa9-9830-6ef34d21556a"), "Boeuf Bourguignon", 1500, 22.00, "Main Course"),
                new MenuItemDTO(UUID.fromString("912db788-37ac-4077-918b-4c4608b67c3a"), "Tarte Tatin", 500, 6.50, "Dessert")
        );
        menuDTO = new MenuDTO();
        menuDTO.setUuid(UUID.fromString("3c0ca0d1-fe96-4cfb-85c4-ce0a719fcfe6"));
        menuDTO.setItems(itemsRestaurantA);
        restaurantDTO = new RestaurantDTO(UUID.fromString("43938b33-a4ed-4521-858c-5fa79bb5e598"), "Le Gourmet D'Or", null, null, new ArrayList<>(), menuDTO, new LinkedHashMap<>());
        RestaurantDAO.save(RestaurantMapper.toEntity(restaurantDTO));

        // Restaurant B (no personnel)
        List<MenuItemDTO> itemsRestaurantB = List.of(
                new MenuItemDTO(UUID.fromString("b6324333-0397-4261-a6ba-518a81ebb0e2"), "Quiche Lorraine", 400, 9.00, "Main Course"),
                new MenuItemDTO(UUID.fromString("ee0a81d3-b59c-483e-869a-9bef148e1f52"), "Ratatouille", 800, 12.00, "Main Course"),
                new MenuItemDTO(UUID.fromString("2e15e4c0-127d-41f5-9d0c-9c5ed519803a"), "Mousse au chocolat", 350, 5.00, "Dessert")
        );
        menuDTO = new MenuDTO();
        menuDTO.setUuid(UUID.fromString("37d02f1f-6936-4889-9174-75582c3fb922"));
        menuDTO.setItems(itemsRestaurantB);
        restaurantDTO = new RestaurantDTO(UUID.fromString("84b00a65-20b9-40f0-88d8-d159fe66de97"), "Bistro de la Plage", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new ArrayList<>(), menuDTO, new LinkedHashMap<>());
        generateSlots(restaurantDTO, restaurantDTO.getOpeningTime(), restaurantDTO.getClosingTime());
        RestaurantDAO.save(RestaurantMapper.toEntity(restaurantDTO));

        // Restaurant C (no menu)
        menuDTO = new MenuDTO();
        menuDTO.setUuid(UUID.fromString("1474c27f-0511-4c06-99ab-5dc2c7037309"));
        restaurantDTO = new RestaurantDTO(UUID.fromString("a66f77b2-aa53-4372-9514-12cd36550b41"), "Café de l'Aube", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new ArrayList<>(), menuDTO, new LinkedHashMap<>());
        generateSlots(restaurantDTO, restaurantDTO.getOpeningTime(), restaurantDTO.getClosingTime());
        RestaurantDAO.save(RestaurantMapper.toEntity(restaurantDTO));

        // Restaurant D (limited personnel)
        List<MenuItemDTO> itemsRestaurantD = List.of(
                new MenuItemDTO(UUID.fromString("cdca818d-4332-473c-8f3d-672c66fb3dda"), "Coq au Vin", 1200, 18.00, "Main Course"),
                new MenuItemDTO(UUID.fromString("c7ed56a7-7010-4413-bf6e-68703ba4f802"), "Bouillabaisse", 1800, 25.00, "Main Course"),
                new MenuItemDTO(UUID.fromString("b02d1126-8516-4e93-9d76-7020a8a62790"), "Crêpe Suzette", 400, 7.50, "Dessert")
        );
        menuDTO = new MenuDTO();
        menuDTO.setUuid(UUID.fromString("f9a61250-5ad2-4e1a-9a61-e89c7951fb81"));
        menuDTO.setItems(itemsRestaurantD);
        restaurantDTO = new RestaurantDTO(UUID.fromString("7091cbc7-4910-4e52-895b-7c87df747931"), "La Table Royale", LocalDateTime.now(), LocalDateTime.now().plusHours(6), new ArrayList<>(), menuDTO, new LinkedHashMap<>());
        generateSlots(restaurantDTO, restaurantDTO.getOpeningTime(), restaurantDTO.getClosingTime());
        RestaurantDAO.save(RestaurantMapper.toEntity(restaurantDTO));
    }

    /**
     * For every 30 minutes, generate a slot with a duration of 30 minutes. The number of slots is calculated based on the opening and closing time.
     */
    private static void generateSlots(RestaurantDTO restaurantDTO, LocalDateTime openingTime, LocalDateTime closingTime) {
        List<SlotDTO> slots = new ArrayList<>();
        LocalDateTime currentTime = openingTime;
        Map<SlotDTO, Set<OrderDTO>> pendingOrders = new LinkedHashMap<>();
        while (currentTime.isBefore(closingTime)) {
            int numberOfPersonnel = (int) (Math.random() * 10);
            SlotDTO slot = new SlotDTO(UUID.randomUUID(), 0, numberOfPersonnel * 1800, currentTime, Duration.ofMinutes(30), numberOfPersonnel);
            slots.add(slot);
            pendingOrders.put(slot, new LinkedHashSet<>());
            currentTime = currentTime.plusMinutes(30);
        }
        restaurantDTO.setSlots(slots);
    }

    public static void main(String[] args) {
        // Clear database
        seedDatabase();
    }
}
