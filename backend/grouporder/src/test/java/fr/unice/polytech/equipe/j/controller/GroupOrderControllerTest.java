package fr.unice.polytech.equipe.j.controller;

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
    private static final String BASE_URL = "http://localhost:5008/grouporder";



    @BeforeAll
    public static void startServer(){
        String dir = System.getProperty("user.dir");
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

        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5003);
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

}

