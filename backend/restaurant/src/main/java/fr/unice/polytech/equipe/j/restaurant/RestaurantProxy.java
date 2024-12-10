package fr.unice.polytech.equipe.j.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.CustomHttpResponse;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.JacksonConfig;
import fr.unice.polytech.equipe.j.RequestUtil;
import fr.unice.polytech.equipe.j.dto.OrderDTO;
import fr.unice.polytech.equipe.j.dto.OrderStatus;
import fr.unice.polytech.equipe.j.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.mapper.DTOMapper;
import fr.unice.polytech.equipe.j.menu.Menu;
import fr.unice.polytech.equipe.j.menu.MenuItem;
import fr.unice.polytech.equipe.j.orderpricestrategy.OrderPrice;
import fr.unice.polytech.equipe.j.orderpricestrategy.OrderPriceStrategy;
import fr.unice.polytech.equipe.j.slot.Slot;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static fr.unice.polytech.equipe.j.RequestUtil.request;

public class RestaurantProxy implements IRestaurant {
    private final IRestaurant restaurant;

    public RestaurantProxy(IRestaurant restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * Add an item to an order, either individual or group.
     *
     * @param orderDTO        the order to which the item is added
     * @param menuItem     the menu item being added
     * @param deliveryTime the delivery time for the order (optional)
     */
    @Override
    public HttpResponse<String> addItemToOrder(OrderDTO orderDTO, MenuItem menuItem, LocalDateTime deliveryTime) {
        HttpResponse<String> response = ((Restaurant) restaurant).canAddItemToOrder(orderDTO, menuItem, deliveryTime);
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            return response;
        }
        return restaurant.addItemToOrder(orderDTO, menuItem, deliveryTime);
    }

    @Override
    public HttpResponse<String> cancelOrder(OrderDTO orderDTO, LocalDateTime deliveryTime) {
        // If the delivery time is not half an hour before the order, the order is cancelled
        if (deliveryTime.isBefore(LocalDateTime.now().minusMinutes(30))) {
            return restaurant.cancelOrder(orderDTO, deliveryTime);
        }
        return new CustomHttpResponse(400, "The order cannot be cancelled less than 30 minutes before the delivery time");
    }

    @Override
    public boolean isOrderValid(OrderDTO orderDTO) {
        return getRestaurant().isOrderValid(orderDTO);
    }

    @Override
    public boolean canAccommodateDeliveryTime(List<MenuItem> items, LocalDateTime deliveryTime) {
        return getRestaurant().canAccommodateDeliveryTime(items, deliveryTime);
    }

    @Override
    public double getTotalPrice(OrderDTO orderDTO) {
        return getRestaurant().getTotalPrice(orderDTO);
    }

    @Override
    public Menu getMenu() {
        return getRestaurant().getMenu();
    }

    @Override
    public Optional<LocalDateTime> getOpeningTime() {
        return getRestaurant().getOpeningTime();
    }

    @Override
    public Optional<LocalDateTime> getClosingTime() {
        return getRestaurant().getClosingTime();
    }

    @Override
    public boolean isSlotCapacityAvailable() {
        return getRestaurant().isSlotCapacityAvailable();
    }

    @Override
    public HttpResponse<String> onOrderPaid(OrderDTO orderDTO) {
        if (!orderDTO.getStatus().equals(OrderStatus.PENDING)) {
            return new CustomHttpResponse(400, "The order is already cancelled or delivered or validated");
        }
        if (orderDTO.getItems().isEmpty()) {
            return new CustomHttpResponse(400, "The order is empty");
        }
        return getRestaurant().onOrderPaid(orderDTO);
    }

    // for Mock testing
    public IRestaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public boolean canPrepareItemInTime(LocalDateTime deliveryTime) {
        return restaurant.canPrepareItemInTime(deliveryTime);
    }

    @Override
    public UUID getRestaurantUUID() {
        return getRestaurant().getRestaurantUUID();
    }

    @Override
    public void setOrderPriceStrategy(OrderPriceStrategy orderPriceStrategy) {
        restaurant.setOrderPriceStrategy(orderPriceStrategy);
    }

    @Override
    public OrderPrice processOrderPrice(OrderDTO orderDTO) {
        return restaurant.processOrderPrice(orderDTO);
    }

    @Override
    public String getRestaurantName() {
        return getRestaurant().getRestaurantName();
    }

    @Override
    public List<Slot> getSlots() {
        return getRestaurant().getSlots();
    }

    @Override
    public boolean setNumberOfPersonnel(Slot slotToUpdate, int numberOfPersonnel) {
        return getRestaurant().setNumberOfPersonnel(slotToUpdate, numberOfPersonnel);
    }

    @Override
    public void addOrderToHistory(OrderDTO orderDTO) {
        getRestaurant().addOrderToHistory(orderDTO);
    }

    @Override
    public boolean addMenuItemToSlot(Slot slot, MenuItem selectedItem) {
        return getRestaurant().addMenuItemToSlot(slot, selectedItem);
    }

    @Override
    public List<OrderDTO> getOrdersHistory() {
        return getRestaurant().getOrdersHistory();
    }

    @Override
    public Map<Slot, Set<OrderDTO>> getPendingOrders() {
        return getRestaurant().getPendingOrders();
    }

    @Override
    public void changeMenu(Menu testMenu) {
        getRestaurant().changeMenu(testMenu);
    }

    @Override
    public HttpResponse<String> changeHours(UUID managerId, LocalDateTime openingHour, LocalDateTime closingHour) {
        // Check if the manager is authorized to change the hours
        // if the manager have this restaurantId in his list of restaurants, then he is authorized
        HttpResponse<String> response = request(
                RequestUtil.DATABASE_MANAGER_SERVICE_URI,
                "/" + managerId + "/restaurant/" + getRestaurantUUID(),
                null,
                HttpMethod.GET,
                null
        );
        if (response.statusCode() != 200) {
            return response;
        }
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        try {
            RestaurantDTO restaurantDTO = objectMapper.readValue(response.body(), RestaurantDTO.class);
            IRestaurant restaurant = DTOMapper.toRestaurant(restaurantDTO);
            if (!restaurant.getRestaurantUUID().equals(getRestaurantUUID())) {
                return new CustomHttpResponse(403, "The manager is not authorized to change the hours of this restaurant");
            }
        } catch (Exception e) {
            return new CustomHttpResponse(500, "Error while parsing the response body of the managers list of restaurants");
        }
        return getRestaurant().changeHours(managerId, openingHour, closingHour);
    }

    public HttpResponse<String> changeNumberOfEmployees(UUID managerId, UUID slotId, int numberOfEmployees) {
        // Check that the manager is authorized to change the number of employees
        // if the manager have this restaurantId in his list of restaurants, then he is authorized
        HttpResponse<String> response = request(
                RequestUtil.DATABASE_MANAGER_SERVICE_URI,
                "/" + managerId + "/restaurant/" + getRestaurantUUID(),
                null,
                HttpMethod.GET,
                null
        );
        if (response.statusCode() != 200) {
            return response;
        }
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        try {
            RestaurantDTO restaurantDTO = objectMapper.readValue(response.body(), RestaurantDTO.class);
            IRestaurant restaurant = DTOMapper.toRestaurant(restaurantDTO);
            if (!restaurant.getRestaurantUUID().equals(getRestaurantUUID())) {
                return new CustomHttpResponse(403, "The manager is not authorized to change the number of employees of this restaurant");
            }
        } catch (Exception e) {
            return new CustomHttpResponse(500, "Error while parsing the response body of the managers list of restaurants");
        }
        return getRestaurant().changeNumberOfEmployees(managerId, slotId, numberOfEmployees);
    }
}
