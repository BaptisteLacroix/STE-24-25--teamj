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
        } catch (Exception e) {
            System.out.println("Error while getting all restaurants: " + e.getMessage());
            return null;
        }
    }

    public static RestaurantEntity getRestaurantById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RestaurantEntity.class, id);
        } catch (Exception e) {
            System.out.println("Error while getting restaurant by id: " + e.getMessage());
            return null;
        }
    }

    public static void save(RestaurantEntity restaurantEntity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(restaurantEntity);
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Error while saving restaurant: " + e.getMessage());
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
        } catch (Exception e) {
            System.out.println("Error while deleting restaurant: " + e.getMessage());
        }
    }
}

