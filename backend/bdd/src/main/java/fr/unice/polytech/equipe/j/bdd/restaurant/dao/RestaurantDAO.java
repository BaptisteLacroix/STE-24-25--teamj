package fr.unice.polytech.equipe.j.bdd.restaurant.dao;

import fr.unice.polytech.equipe.j.bdd.HibernateUtil;
import fr.unice.polytech.equipe.j.bdd.restaurant.entities.RestaurantEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.UUID;

public class RestaurantDAO {
    public static List<RestaurantEntity> getAllRestaurants() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Restaurant", RestaurantEntity.class).list();
        }
    }

    public static RestaurantEntity getRestaurantById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RestaurantEntity.class, id);
        }
    }

    public static void saveRestaurant(RestaurantEntity restaurantEntity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(restaurantEntity);
            transaction.commit();
        }
    }

    public static void deleteRestaurant(UUID id) {
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

