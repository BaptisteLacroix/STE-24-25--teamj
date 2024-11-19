package fr.unice.polytech.equipe.j;

import fr.unice.polytech.equipe.j.order.DeliveryLocationDatabaseSeeder;
import fr.unice.polytech.equipe.j.order.dao.DeliveryLocationDAO;
import fr.unice.polytech.equipe.j.restaurant.RestaurantDatabaseSeeder;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.user.UserDatabaseSeeder;
import fr.unice.polytech.equipe.j.user.dao.CampusUserDAO;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private HibernateUtil() {
    }

    public static void populateDatabase() {
        RestaurantDatabaseSeeder.seedDatabase();
        UserDatabaseSeeder.seedDatabase();
        DeliveryLocationDatabaseSeeder.seedDatabase();
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

    public static void main(String[] args) {
        HibernateUtil.populateDatabase();
        HibernateUtil.shutdown();
    }
}
