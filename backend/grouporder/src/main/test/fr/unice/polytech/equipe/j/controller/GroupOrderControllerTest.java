package fr.unice.polytech.equipe.j.controller;

import fr.unice.polytech.equipe.j.Application;
import fr.unice.polytech.equipe.j.FlexibleRestServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import fr.unice.polytech.equipe.j.HibernateUtil;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GroupOrderControllerTest {
    private static final String BASE_URL = "http://localhost:5008/api/group-order";
    private static final String DATABASE_HEALTH_URL = "http://localhost:5000/api/database/health";
    private static final UUID ORDER_UUID = UUID.fromString("178225f2-9f08-4a7f-add2-3783e89ffa6b"); // TEST Order
    private static final UUID USER_UUID = UUID.fromString("1aeb4480-305a-499d-885c-7d2d9f99153b"); // TEST User
    private static final UUID USER2_UUID = UUID.fromString("2ed64a86-d499-4a9c-a0a1-9aba06297348"); // TEST User
    private static UUID GROUP_ORDER_UUID = UUID.fromString("ef254832-dba4-44f8-8fa4-88720ee3e0a7");
    private static final String BASE_DATABASE_URL = "http://localhost:5000/api/database";

    @BeforeAll
    public static void startServer(){
        String userDir = System.getProperty("user.dir");
        String bddModulePath = Paths.get(System.getProperty("user.dir")).getParent().toString();
        System.setProperty("user.dir", bddModulePath + "/bdd");
        // Set the hibernate.cfg.path system property to the test one hibernate-test.cfg.xml
        System.setProperty("hibernate.cfg.path", "hibernate-test.cfg.xml");
        // wait the end of the execution of the main method before continuing
        while (!HibernateUtil.populateDatabase(new String[]{"test"})) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.setProperty("user.dir", userDir);
        String backendPath = Paths.get(System.getProperty("user.dir")).getParent().toString();
        new Thread(() -> Application.main(
                new String[]{backendPath + "/bdd/target/classes"}
        )).start();

        // Wait for the database API to be available
        waitForDatabaseToStart(DATABASE_HEALTH_URL);

        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5008);
        server.start();



    }

    // Helper method to wait until the database API is available
    private static void waitForDatabaseToStart(String databaseUrl) {
        boolean isDatabaseUp = false;
        int attempts = 0;
        int maxAttempts = 30;  // Retry for up to 30 seconds (adjust as necessary)

        while (attempts < maxAttempts && !isDatabaseUp) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(databaseUrl).openConnection();
                connection.setRequestMethod("GET");
                int statusCode = connection.getResponseCode();
                if (statusCode == 200) {
                    isDatabaseUp = true;
                    System.out.println("Database API is up and running!");
                    return;
                }
                attempts++;
                System.out.println("Database API not available yet (" + statusCode + "). Retrying... (" + attempts + ")");
                try {
                    TimeUnit.SECONDS.sleep(1);  // Wait 1 second before retrying
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                attempts++;
                System.out.println("Database API not available yet. Retrying... (" + attempts + ")");
                try {
                    TimeUnit.SECONDS.sleep(1);  // Wait 1 second before retrying
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (!isDatabaseUp) {
            throw new RuntimeException("Database API did not start in time.");
        }
    }

    @Test
    @Order(1)
    void createGroupOrder() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL+"/"+USER_UUID+"/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());;
        // Supprimez les guillemets de la réponse
        String cleanedResponse = response.body().replace("\"", "");
        GROUP_ORDER_UUID = UUID.fromString(cleanedResponse);
        System.out.println(response.statusCode());
        assertEquals(201, response.statusCode());
    }


    @Test
    @Order(2)
    void testJoinGroupOrder() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL+"/"+GROUP_ORDER_UUID+"/join/"+USER2_UUID))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        System.out.println(response.body());
        assertEquals(201, response.statusCode());
    }

    @Test
    @Order(3)
    void testJoinGroupOrderWithInvalidGroupOrder() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL+"/"+UUID.randomUUID()+"/join/"+USER_UUID))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        System.out.println(response.statusCode());
        assertEquals(500, response.statusCode());
    }

    @Test
    @Order(4)
    void testJoinGroupOrderWithInvalidUser() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL+"/"+GROUP_ORDER_UUID+"/join/"+UUID.randomUUID()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());

        System.out.println(response.statusCode());
        assertEquals(500, response.statusCode());
    }


    @Test
    @Order(6)
    void testValidateGroupOrderWithInvalidGroupOrder() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL+"/"+UUID.randomUUID()+"/"+USER_UUID+"/validate"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        System.out.println(response.statusCode());
        assertEquals(500, response.statusCode());
    }

    @Test
    @Order(7)
    void testValidateGroupOrderWithInvalidUser() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL+"/"+GROUP_ORDER_UUID+"/"+UUID.randomUUID()+"/validate"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        System.out.println(response.statusCode());
        assertEquals(500, response.statusCode());
    }

}

