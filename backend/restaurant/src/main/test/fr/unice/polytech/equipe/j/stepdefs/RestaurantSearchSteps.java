package fr.unice.polytech.equipe.j.stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.HttpMethod;
import fr.unice.polytech.equipe.j.JacksonConfig;
import fr.unice.polytech.equipe.j.RequestUtil;
import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.dto.MenuDTO;
import fr.unice.polytech.equipe.j.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.dto.RestaurantDTO;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

public class RestaurantSearchSteps {

    private final String root = "http://localhost:5007/api/restaurants";
    private List<RestaurantDTO> foundRestaurants;
    private final ObjectMapper mapper = JacksonConfig.configureObjectMapper();
    @Before
    public void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
    }

    @Given("the restaurant service manager configured the following restaurants:")
    public void theRestaurantServiceManagerConfiguredTheFollowingRestaurants() {

    }

    @When("the user searches for restaurants by name {string}")
    public void searchForRestaurantsByName(String restaurantName) throws JsonProcessingException {
        var request = RequestUtil.request(root, "/name/" + restaurantName, null, HttpMethod.GET, null);
        assertNotNull(request);
        this.foundRestaurants = this.mapper.readValue(request.body(), new TypeReference<>(){});
        assertNotNull(this.foundRestaurants.getFirst().getId());
    }

    @Given("the user searches for restaurants by name {string} that is not in the system")
    public void the_user_searches_for_restaurants_by_name_that_is_not_in_the_system(String string) {
        var request = RequestUtil.request(root, "/name/" + string, null, HttpMethod.GET, null);
        assertNotNull(request);
        assertEquals(request.statusCode(), 404);
    }

    @Given("the user searches for food type {string} that is not in the system")
    public void the_user_searches_for_food_type_that_is_not_in_the_system(String string) {
        var request = RequestUtil.request(root, "/name/" + string, null, HttpMethod.GET, null);
        assertNotNull(request);
        assertEquals(request.statusCode(), 404);
    }

    @When("the user searches for food with name {string}")
    public void searchForRestaurantsByFood(String foodName) throws JsonProcessingException {
        var request = RequestUtil.request(root, "/foodType/" + foodName, null, HttpMethod.GET, null);
        assertNotNull(request);
        this.foundRestaurants = this.mapper.readValue(request.body(), new TypeReference<>(){});
        assertNotNull(this.foundRestaurants.getFirst().getId());
    }

    @Then("the user should see the following restaurant\\(s):")
    public void the_user_should_see_the_following_restaurant_s(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> expectedRestaurants = dataTable.asMaps();

        for (Map<String, String> expectedRestaurant : expectedRestaurants) {
            RestaurantDTO restaurant = foundRestaurants.stream()
                    .filter(r -> r.getRestaurantName().trim().equalsIgnoreCase(expectedRestaurant.get("name").trim()))
                    .findFirst()
                    .orElse(null);
            assertNotNull(restaurant);

            MenuDTO menu = restaurant.getMenu();
            String[] menuItems = expectedRestaurant.get("menu items").split(", ");
            String[] prices = expectedRestaurant.get("price").split(", ");
            for (int i = 0; i < menuItems.length; i++) {
                int finalI = i;
                MenuItemDTO menuItem = menu.getItems().stream().filter(e-> e.getName().equals(menuItems[finalI])).findFirst().orElseThrow();
                assertNotNull(menuItem);
                assertEquals(Double.parseDouble(prices[i]), menuItem.getPrice(), 0.01);
            }
        }
    }

    @Then("the user should not see any restaurants")
    public void the_user_should_not_see_any_restaurants() {
        assertEquals(foundRestaurants, null);
    }
}
