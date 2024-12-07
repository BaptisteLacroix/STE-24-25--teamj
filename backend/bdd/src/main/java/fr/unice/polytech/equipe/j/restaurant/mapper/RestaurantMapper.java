package fr.unice.polytech.equipe.j.restaurant.mapper;

import fr.unice.polytech.equipe.j.order.dao.OrderDAO;
import fr.unice.polytech.equipe.j.order.dto.OrderDTO;
import fr.unice.polytech.equipe.j.order.entities.IndividualOrderEntity;
import fr.unice.polytech.equipe.j.order.entities.OrderEntity;
import fr.unice.polytech.equipe.j.order.mapper.OrderMapper;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.SlotDTO;
import fr.unice.polytech.equipe.j.restaurant.entities.MenuEntity;
import fr.unice.polytech.equipe.j.restaurant.entities.MenuItemEntity;
import fr.unice.polytech.equipe.j.restaurant.entities.RestaurantEntity;
import fr.unice.polytech.equipe.j.restaurant.entities.SlotEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
        // Initialize pendingOrders as a LinkedHashMap to preserve the order of slots
        Map<UUID, Set<UUID>> pendingOrders = new LinkedHashMap<>();

        // Map SlotEntities to SlotDTOs and corresponding Orders
        restaurantEntity.getSlotEntities().forEach(slotEntity -> {
            // For each OrderEntity inside slotEntity, transform it to OrderDTO and add it to pendingOrders
            List<UUID> orderDTOList = slotEntity.getOrders().stream()
                    .map(OrderMapper::toDTO)
                    .map(OrderDTO::getId)
                    .toList();
            pendingOrders.put(slotEntity.getId(), Set.copyOf(orderDTOList));  // Ensure no duplicates with Set.copyOf
        });
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
                menuDTO,
                pendingOrders
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

        // Initialize pendingOrders as a LinkedHashMap to preserve the order of slots
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
                    if (!dto.getPendingOrders().isEmpty()) {
                        List<OrderEntity> orderEntities = OrderDAO.getAllOrders();
                        List<IndividualOrderEntity> individualEntities = OrderDAO.getAllIndividualOrders();
                        // CHeck if the order id is inside this list, get it and add it to the slot
                        Set<OrderEntity> orders = dto.getPendingOrders().get(slot.getUuid()).stream()
                                .map(orderId -> orderEntities.stream()
                                        .filter(order -> order.getId().toString().equals(orderId.toString()))
                                        .findFirst()
                                        .orElseGet(() -> individualEntities.stream()
                                                .filter(order -> order.getId().toString().equals(orderId.toString()))
                                                .findFirst()
                                                .orElse(null)))
                                .collect(Collectors.toSet());
                        slotEntity.setOrders(orders);
                    } else {
                        slotEntity.setOrders(new HashSet<>());
                    }
                    return slotEntity;
                }).toList());

        return restaurantEntity;
    }

    /**
     * SlotDTO to SlotEntity
     */
    public static SlotEntity toEntity(SlotDTO dto) {
        if (dto == null) {
            return null;
        }
        SlotEntity slotEntity = new SlotEntity();
        slotEntity.setId(dto.getUuid());
        slotEntity.setCurrentCapacity(dto.getCurrentCapacity());
        slotEntity.setMaxCapacity(dto.getMaxCapacity());
        slotEntity.setOpeningDate(dto.getOpeningDate());
        slotEntity.setDurationTime(dto.getDurationTime());
        slotEntity.setNumberOfPersonnel(dto.getNumberOfPersonnel());
        return slotEntity;
    }

    /**
     * SlotEntity to SlotDTO
     */
    public static SlotDTO toDTO(SlotEntity entity) {
        if (entity == null) {
            return null;
        }
        return new SlotDTO(
                entity.getId(),
                entity.getCurrentCapacity(),
                entity.getMaxCapacity(),
                entity.getOpeningDate(),
                entity.getDurationTime(),
                entity.getNumberOfPersonnel()
        );
    }
}
