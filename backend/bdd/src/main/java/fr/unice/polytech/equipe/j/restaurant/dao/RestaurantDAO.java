package fr.unice.polytech.equipe.j.restaurant.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.restaurant.entities.RestaurantEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.UUID;

public class RestaurantDAO {
    public static List<RestaurantEntity> getAllRestaurants() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM RestaurantEntity", RestaurantEntity.class).list();
        }
    }

    public static RestaurantEntity getRestaurantById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RestaurantEntity.class, id);
        }
    }

    public static void save(RestaurantEntity restaurantEntity) {
        System.out.println("Saving restaurant: " + restaurantEntity.getName());
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(restaurantEntity);
            transaction.commit();
        }
    }

    public static void delete(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            RestaurantEntity restaurantEntity = session.get(RestaurantEntity.class, id);
            if (restaurantEntity != null) {
                session.delete(restaurantEntity);
            }
            transaction.commit();
        }
    }
}

