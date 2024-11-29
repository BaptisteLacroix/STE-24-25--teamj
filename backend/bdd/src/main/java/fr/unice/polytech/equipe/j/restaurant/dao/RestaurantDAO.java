package fr.unice.polytech.equipe.j.restaurant.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.restaurant.entities.MenuEntity;
import fr.unice.polytech.equipe.j.restaurant.entities.MenuItemEntity;
import fr.unice.polytech.equipe.j.restaurant.entities.RestaurantEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RestaurantDAO {
    public static List<RestaurantEntity> getAllRestaurants() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM RestaurantEntity", RestaurantEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static RestaurantEntity getRestaurantById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RestaurantEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse save(RestaurantEntity restaurantEntity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(restaurantEntity);
            transaction.commit();
            return new HttpResponse(HttpCode.HTTP_201, restaurantEntity.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(HttpCode.HTTP_500, "Internal server error");
        }
    }

    public static HttpResponse delete(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            RestaurantEntity restaurantEntity = session.get(RestaurantEntity.class, id);
            session.delete(restaurantEntity);
            transaction.commit();
            return new HttpResponse(HttpCode.HTTP_200, "Restaurant deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(HttpCode.HTTP_500, "Internal server error");
        }
    }
}

