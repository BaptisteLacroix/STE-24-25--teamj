package fr.unice.polytech.equipe.j.restaurant.mapper;

import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.entities.MenuItemEntity;

public class MenuItemMapper {

    public static MenuItemDTO toDTO(MenuItemEntity entity) {
        MenuItemDTO menuItemDTO = new MenuItemDTO();
        menuItemDTO.setId(entity.getId());
        menuItemDTO.setName(entity.getName());
        menuItemDTO.setPrice(entity.getPrice());
        menuItemDTO.setPrepTime(entity.getPrepTime());
        menuItemDTO.setType(entity.getType());
        return menuItemDTO;
    }

    public static MenuItemEntity toEntity(MenuItemDTO dto) {
        MenuItemEntity menuItemEntity = new MenuItemEntity();
        menuItemEntity.setId(dto.getId());
        menuItemEntity.setName(dto.getName());
        menuItemEntity.setPrice(dto.getPrice());
        menuItemEntity.setPrepTime(dto.getPrepTime());
        menuItemEntity.setMenuEntity(RestaurantDAO.getMenuByMenuItemId(dto.getId()));
        menuItemEntity.setType(dto.getType());
        return menuItemEntity;
    }
}
