package fr.unice.polytech.equipe.j.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.JacksonConfig;
import fr.unice.polytech.equipe.j.RequestUtil;
import fr.unice.polytech.equipe.j.dto.IndividualOrderDTO;
import fr.unice.polytech.equipe.j.dto.MenuDTO;
import fr.unice.polytech.equipe.j.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.dto.OrderDTO;
import fr.unice.polytech.equipe.j.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.dto.SlotDTO;
import fr.unice.polytech.equipe.j.menu.Menu;
import fr.unice.polytech.equipe.j.menu.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.slot.Slot;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static fr.unice.polytech.equipe.j.RequestUtil.request;

public class DTOMapper {

    public static RestaurantDTO toRestaurantDTO(IRestaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getRestaurantUUID());
        dto.setRestaurantName(restaurant.getRestaurantName());
        dto.setMenu(toMenuDTO(restaurant.getMenu()));
        dto.setSlots(restaurant.getSlots().stream().map(
                DTOMapper::toSlotDTO
        ).toList());
        dto.setClosingTime(restaurant.getClosingTime().orElse(null));
        dto.setOpeningTime(restaurant.getOpeningTime().orElse(null));
        Map<UUID, Set<UUID>> pendingOrders = restaurant.getPendingOrders().entrySet().stream()
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey().getUUID(), entry.getValue().stream().map(OrderDTO::getId).collect(Collectors.toSet())), Map::putAll);
        dto.setPendingOrders(pendingOrders);
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
        try {

        java.net.http.HttpResponse<String> response = request(
                RequestUtil.DATABASE_ORDER_SERVICE_URI,
                "/all",
                HttpMethod.GET,
                null);
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        List<OrderDTO> orderDTOList = objectMapper.readValue(response.body(), new TypeReference<List<OrderDTO>>() {
        });
        Map<UUID, OrderDTO> orderDTOMap = orderDTOList.stream().collect(Collectors.toMap(OrderDTO::getId, orderDTO -> orderDTO));

        response = request(
                RequestUtil.DATABASE_ORDER_SERVICE_URI,
                "/individual/all",
                HttpMethod.GET,
                null);
        List<IndividualOrderDTO> individualOrderDTOList = objectMapper.readValue(response.body(), new TypeReference<List<IndividualOrderDTO>>() {
        });
        Map<UUID, OrderDTO> individualOrderDTOMap = individualOrderDTOList.stream().collect(Collectors.toMap(OrderDTO::getId, orderDTO -> orderDTO));

        orderDTOMap.putAll(individualOrderDTOMap);

        Map<Slot, Set<OrderDTO>> pendingOrders = restaurantDTO.getPendingOrders().entrySet().stream()
                .collect(LinkedHashMap::new, (map, entry) -> {
                    Slot slot = restaurantDTO.getSlots().stream()
                            .filter(s -> s.getUuid().equals(entry.getKey()))
                            .map(DTOMapper::toSlot)
                            .findFirst()
                            .orElseThrow();
                    Set<OrderDTO> orders = entry.getValue().stream()
                            .map(orderDTOMap::get)
                            .collect(Collectors.toSet());
                    map.put(slot, orders);
                }, Map::putAll);
        return new Restaurant(
                restaurantDTO.getId(),
                restaurantDTO.getRestaurantName(),
                restaurantDTO.getOpeningTime(),
                restaurantDTO.getClosingTime(),
                toMenu(restaurantDTO.getMenu()),
                restaurantDTO.getSlots().stream().map(DTOMapper::toSlot).toList(),
                pendingOrders
        );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static MenuDTO toMenuDTO(Menu menu) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setUuid(menu.getUuid());
        menuDTO.setItems(menu.getItems().stream().map(DTOMapper::toMenuItemDTO).toList());
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
