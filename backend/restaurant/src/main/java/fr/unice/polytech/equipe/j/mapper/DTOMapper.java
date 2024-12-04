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

import java.util.List;

public class DTOMapper {

    public static RestaurantDTO toRestaurantDTO(IRestaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getRestaurantUUID());
        dto.setRestaurantName(restaurant.getRestaurantName());
        dto.setMenu(toMenuDTO(restaurant.getMenu()));
        dto.setSlots(restaurant.getSlots().stream().map(DTOMapper::toSlotDTO).toList());
        dto.setClosingTime(restaurant.getClosingTime().orElse(null));
        dto.setOpeningTime(restaurant.getOpeningTime().orElse(null));
        return dto;
    }

    public static SlotDTO toSlotDTO(Slot slot) {
        SlotDTO dto = new SlotDTO();
        dto.setUuid(slot.getUUID());
        dto.setOpeningDate(slot.getOpeningDate());
        dto.setCurrentCapacity(slot.getCurrentCapacity());
        dto.setNumberOfPersonnel(slot.getNumberOfPersonnel());
        dto.setMaxCapacity(slot.getMaxCapacity());
        dto.setDurationTime(slot.getDurationTime());
        return dto;
    }

    public static IRestaurant toRestaurant(RestaurantDTO restaurantDTO) {
        System.out.println("Restaurant Menu to Restaurant : " + restaurantDTO.getMenu());
        return new Restaurant(
                restaurantDTO.getId(),
                restaurantDTO.getRestaurantName(),
                restaurantDTO.getOpeningTime(),
                restaurantDTO.getClosingTime(),
                toMenu(restaurantDTO.getMenu()),
                restaurantDTO.getSlots().stream().map(DTOMapper::toSlot).toList()
        );
    }


    public static MenuDTO toMenuDTO(Menu menu) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setUuid(menu.getUuid());
        List<MenuItemDTO> items = menu.getItems().stream().map(DTOMapper::toMenuItemDTO).toList();
        menuDTO.setItems(items);
        // Set back reference
        items.forEach(item -> item.setMenuDTO(menuDTO));
        return menuDTO;
    }

    public static Menu toMenu(MenuDTO menuDTO) {
        Menu menu = new Menu.MenuBuilder().addMenuItems(menuDTO.getItems().stream().map(DTOMapper::toMenuItem).toList()).build();
        menu.setUuid(menuDTO.getUuid());
        return menu;
    }

    public static Slot toSlot(SlotDTO slotDTO) {
        Slot slot = new Slot(slotDTO.getUuid(), slotDTO.getOpeningDate(), slotDTO.getNumberOfPersonnel());
        slot.setCurrentCapacity(slotDTO.getCurrentCapacity());
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
