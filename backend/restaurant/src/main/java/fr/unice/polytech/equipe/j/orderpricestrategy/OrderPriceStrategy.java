package fr.unice.polytech.equipe.j.orderpricestrategy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.unice.polytech.equipe.j.dto.OrderDTO;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "strategyType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = KPercentForNOrderPriceStrategy.class, name = "kpercent"),
        @JsonSubTypes.Type(value = FreeItemFotNItemsOrderPriceStrategy.class, name = "free_item")
})
public interface OrderPriceStrategy {
    OrderPrice processOrderPrice(OrderDTO orderDTO, IRestaurant restaurant);
}
