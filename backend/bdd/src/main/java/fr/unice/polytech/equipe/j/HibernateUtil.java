package fr.unice.polytech.equipe.j;

import fr.unice.polytech.equipe.j.order.DeliveryLocationDatabaseSeeder;
import fr.unice.polytech.equipe.j.restaurant.RestaurantDatabaseSeeder;
import fr.unice.polytech.equipe.j.user.UserDatabaseSeeder;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Get the path to the Hibernate config file based on a system property
            // Default to "hibernate-prod.cfg.xml" if no property is set
            String configFile = System.getProperty("hibernate.cfg.path", "hibernate-prod.cfg.xml");

            // Create the session factory using the selected config file
            sessionFactory = new Configuration().configure(configFile).buildSessionFactory();
            System.out.println("Using Database URL: " + sessionFactory.getProperties().get("hibernate.connection.url"));
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private HibernateUtil() {
    }

    public static void main(String[] args) {
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5003);
        server.start();
        RestaurantDatabaseSeeder.seedDatabase();
        UserDatabaseSeeder.seedDatabase();
        DeliveryLocationDatabaseSeeder.seedDatabase();
        shutdown();
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
