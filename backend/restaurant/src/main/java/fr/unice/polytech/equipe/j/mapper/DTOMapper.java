package fr.unice.polytech.equipe.j.mapper;

import fr.unice.polytech.equipe.j.dto.MenuDTO;
import fr.unice.polytech.equipe.j.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.dto.SlotDTO;
import fr.unice.polytech.equipe.j.menu.Menu;
import fr.unice.polytech.equipe.j.menu.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.slot.Slot;

import java.time.LocalDateTime;

public class DTOMapper {

    public static RestaurantDTO toRestaurantDTO(IRestaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setUuid(restaurant.getRestaurantUUID());
        dto.setRestaurantName(restaurant.getRestaurantName());
        dto.setMenu(toMenuDTO(restaurant.getMenu()));
        dto.setSlots(restaurant.getSlots().stream().map(DTOMapper::toSlotDTO).toList());
        dto.setOpeningTime(restaurant.getOpeningTime().map(LocalDateTime::toString).orElse(null));
        dto.setOpeningTime(restaurant.getOpeningTime().map(LocalDateTime::toString).orElse(null));
        return dto;
    }

    public static SlotDTO toSlotDTO(Slot slot) {
        SlotDTO dto = new SlotDTO();
        dto.setOpeningDate(slot.getOpeningDate().toString());
        dto.setAvailableCapacity(slot.getAvailableCapacity());
        dto.setNumberOfPersonnel(slot.getNumberOfPersonnel());
        return dto;
    }

    public static IRestaurant toRestaurant(RestaurantDTO restaurantDTO) {
        LocalDateTime openingTime = restaurantDTO.getOpeningTime() != null ?
                LocalDateTime.parse(restaurantDTO.getOpeningTime()) : null;
        LocalDateTime closingTime = restaurantDTO.getClosingTime() != null ?
                LocalDateTime.parse(restaurantDTO.getClosingTime()) : null;
        return new Restaurant(
                restaurantDTO.getUuid(),
                restaurantDTO.getRestaurantName(),
                openingTime,
                closingTime,
                toMenu(restaurantDTO.getMenu()),
                restaurantDTO.getSlots().stream().map(DTOMapper::toSlot).toList()
        );
    }


    public static MenuDTO toMenuDTO(Menu menu) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setItems(menu.getItems().stream().map(DTOMapper::toMenuItemDTO).toList());
        return menuDTO;
    }

    public static Menu toMenu(MenuDTO menuDTO) {
        return new Menu.MenuBuilder().addMenuItems(menuDTO.getItems().stream().map(DTOMapper::toMenuItem).toList()).build();
    }

    public static Slot toSlot(SlotDTO slotDTO) {
        Slot slot = new Slot(slotDTO.getUuid(), LocalDateTime.parse(slotDTO.getOpeningDate()), slotDTO.getNumberOfPersonnel());
        slot.setAvailableCapacity(slotDTO.getAvailableCapacity());
        return slot;
    }

    public static MenuItem toMenuItem(MenuItemDTO item) {
        return new MenuItem(item.getId(), item.getName(), item.getPrepTime(), item.getPrice());
    }

    public static MenuItemDTO toMenuItemDTO(MenuItem item) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(item.getUuid());
        dto.setName(item.getName());
        dto.setPrepTime(item.getPrepTime());
        dto.setPrice(item.getPrice());
        return dto;
    }
}
