package fr.unice.polytech.equipe.j.restaurant.mapper;

import fr.unice.polytech.equipe.j.restaurant.dto.MenuDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.SlotDTO;
import fr.unice.polytech.equipe.j.restaurant.entities.MenuEntity;
import fr.unice.polytech.equipe.j.restaurant.entities.MenuItemEntity;
import fr.unice.polytech.equipe.j.restaurant.entities.RestaurantEntity;
import fr.unice.polytech.equipe.j.restaurant.entities.SlotEntity;

import java.util.ArrayList;

public class RestaurantMapper {
    public static RestaurantDTO toDTO(RestaurantEntity restaurantEntity) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setUuid(restaurantEntity.getMenuEntity().getUuid());
        menuDTO.setItems(restaurantEntity.getMenuEntity().getItems().stream().map(item -> new MenuItemDTO(
                item.getId(),
                item.getName(),
                item.getPrepTime(),
                item.getPrice()
        )).toList());
        return new RestaurantDTO(
                restaurantEntity.getId(),
                restaurantEntity.getName(),
                restaurantEntity.getOpeningTime(),
                restaurantEntity.getClosingTime(),
                restaurantEntity.getSlotEntities().stream().map(slot -> new SlotDTO(
                        slot.getId(),
                        slot.getCurrentCapacity(),
                        slot.getMaxCapacity(),
                        slot.getOpeningDate(),
                        slot.getDurationTime(),
                        slot.getNumberOfPersonnel()
                )).toList(),
                menuDTO
        );
    }

    public static RestaurantEntity toEntity(RestaurantDTO dto) {
        MenuEntity menuEntity = new MenuEntity();
        if (dto.getMenu().getItems() != null) {
            menuEntity.setUuid(dto.getMenu().getUuid());
            menuEntity.setItems(dto.getMenu().getItems().stream()
                    .map(item -> {
                        MenuItemEntity menuItemEntity = new MenuItemEntity();
                        menuItemEntity.setId(item.getId());
                        menuItemEntity.setName(item.getName());
                        menuItemEntity.setPrepTime(item.getPrepTime());
                        menuItemEntity.setPrice(item.getPrice());
                        menuItemEntity.setMenuEntity(menuEntity);
                        return menuItemEntity;
                    }).toList());
        } else {
            menuEntity.setUuid(dto.getMenu().getUuid());
            menuEntity.setItems(new ArrayList<>());
        }

        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(dto.getId());
        restaurantEntity.setName(dto.getRestaurantName());
        restaurantEntity.setOpeningTime(dto.getOpeningTime());
        restaurantEntity.setClosingTime(dto.getClosingTime());
        restaurantEntity.setMenuEntity(menuEntity);
        restaurantEntity.setSlotEntities(dto.getSlots().stream()
                .map(slot -> {
                    SlotEntity slotEntity = new SlotEntity();
                    slotEntity.setId(slot.getUuid());
                    slotEntity.setCurrentCapacity(slot.getCurrentCapacity());
                    slotEntity.setMaxCapacity(slot.getMaxCapacity());
                    slotEntity.setOpeningDate(slot.getOpeningDate());
                    slotEntity.setDurationTime(slot.getDurationTime());
                    slotEntity.setNumberOfPersonnel(slot.getNumberOfPersonnel());
                    slotEntity.setRestaurantEntity(restaurantEntity);
                    return slotEntity;
                }).toList());

        return restaurantEntity;
    }
}
