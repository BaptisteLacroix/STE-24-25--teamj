package fr.unice.polytech.equipe.j.restaurant.mapper;

import fr.unice.polytech.equipe.j.restaurant.dto.MenuDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.SlotDTO;
import fr.unice.polytech.equipe.j.restaurant.entities.MenuEntity;
import fr.unice.polytech.equipe.j.restaurant.entities.MenuItemEntity;
import fr.unice.polytech.equipe.j.restaurant.entities.RestaurantEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RestaurantMapper {
    public static RestaurantDTO toDTO(RestaurantEntity restaurantEntity) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setUuid(restaurantEntity.getMenuEntity().getId());
        menuDTO.setItems(restaurantEntity.getMenuEntity().getItems().stream().map(item -> new MenuItemDTO(
                item.getId(),
                item.getName(),
                item.getPrepTime(),
                item.getPrice()
        )).toList());

        String openingTime = restaurantEntity.getOpeningTime() != null ?
                restaurantEntity.getOpeningTime() : null;
        String closingTime = restaurantEntity.getClosingTime() != null ?
                restaurantEntity.getClosingTime() : null;

        return new RestaurantDTO(
                restaurantEntity.getId(),
                restaurantEntity.getName(),
                openingTime,
                closingTime,
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
            menuEntity.setId(dto.getMenu().getUuid());
            menuEntity.setItems(dto.getMenu().getItems().stream()
                    .map(item -> {
                        MenuItemEntity menuItemEntity = new MenuItemEntity();
                        menuItemEntity.setId(item.getUuid());
                        menuItemEntity.setName(item.getName());
                        menuItemEntity.setPrepTime(item.getPrepTime());
                        menuItemEntity.setPrice(item.getPrice());
                        menuItemEntity.setMenuEntity(menuEntity);
                        return menuItemEntity;
                    }).toList());
        } else {
            menuEntity.setId(dto.getMenu().getUuid());
            menuEntity.setItems(new ArrayList<>());
        }

        String openingTime = dto.getOpeningTime() != null ?
                dto.getOpeningTime() : null;
        String closingTime = dto.getClosingTime() != null ?
                dto.getClosingTime() : null;

        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(dto.getUuid());
        restaurantEntity.setName(dto.getRestaurantName());
        restaurantEntity.setOpeningTime(openingTime);
        restaurantEntity.setClosingTime(closingTime);
        restaurantEntity.setMenuEntity(menuEntity);

        return restaurantEntity;
    }
}
