package fr.unice.polytech.equipe.j.user;
import static org.junit.jupiter.api.Assertions.*;

import fr.unice.polytech.equipe.j.payment.strategy.PaymentMethod;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import fr.unice.polytech.equipe.j.payment.*;

public class CampusUserTest {

    @Test
    public void testToJson() {
        // Arrange
        CampusUser user = new CampusUser("John Doe", 150.0);
        user.setPreferredPaymentMethod(PaymentMethod.CREDIT_CARD);
        user.setDefaultPaymentMethod(PaymentMethod.CREDIT_CARD);

        // Act
        String json = user.toJson();

        // Assert
        assertTrue(json.contains("\"name\": \"John Doe\""));
        assertTrue(json.contains("\"balance\": 150.0"));
        assertTrue(json.contains("\"preferredPaymentMethod\": \"CREDIT_CARD\""));
        assertTrue(json.contains("\"defaultPaymentMethod\": \"CREDIT_CARD\""));
    }

    @Test
    public void testFromJson() {
        // Arrange
        String json = """
                {
                    "name": "John Doe",
                    "balance": 150.0,
                    "preferredPaymentMethod": "CREDIT_CARD",
                    "defaultPaymentMethod": "CREDIT_CARD",
                    "transactions": [],
                    "ordersHistory": []
                }
                """;

        // Act
        CampusUser user = CampusUser.fromJson(json);

        // Assert
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals(150.0, user.getBalance());
        assertEquals(PaymentMethod.CREDIT_CARD, user.getPreferredPaymentMethod());
        assertEquals(PaymentMethod.CREDIT_CARD, user.getDefaultPaymentMethod());
        assertTrue(user.getTransactions().isEmpty());
        assertTrue(user.getOrdersHistory().isEmpty());
    }

    @Test
    public void testToJsonAndFromJsonConsistency() {
        // Arrange
        CampusUser originalUser = new CampusUser("Jane Doe", 200.0);
        originalUser.setPreferredPaymentMethod(PaymentMethod.CREDIT_CARD);
        originalUser.setDefaultPaymentMethod(PaymentMethod.CREDIT_CARD);

        // Act
        String json = originalUser.toJson();
        CampusUser deserializedUser = CampusUser.fromJson(json);

        // Assert
        assertNotNull(deserializedUser);
        assertEquals(originalUser.getName(), deserializedUser.getName());
        assertEquals(originalUser.getBalance(), deserializedUser.getBalance());
        assertEquals(originalUser.getPreferredPaymentMethod(), deserializedUser.getPreferredPaymentMethod());
        assertEquals(originalUser.getDefaultPaymentMethod(), deserializedUser.getDefaultPaymentMethod());
    }
}
