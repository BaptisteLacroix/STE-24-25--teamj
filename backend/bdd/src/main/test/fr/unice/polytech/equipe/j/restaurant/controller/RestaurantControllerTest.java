package fr.unice.polytech.equipe.j.restaurant.controller;

import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.entities.RestaurantEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantControllerTest {

    @Test
    void testGetRestaurantById() {
        RestaurantController controller = new RestaurantController();

        UUID sampleId = UUID.randomUUID();
        RestaurantEntity sampleEntity = new RestaurantEntity();
        sampleEntity.setId(sampleId);
        sampleEntity.setName("Test Restaurant");
        RestaurantDAO.saveRestaurant(sampleEntity);

        HttpResponse response = controller.getRestaurantById(sampleId);

        assertEquals(200, response.getCode());
        assertNotNull(response.getContent());
        assertEquals("Test Restaurant", ((RestaurantDTO) response.getContent()).getRestaurantName());
    }
}
