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

/**
 * Data Access Object (DAO) for managing restaurant entities in the database.
 * Provides methods for CRUD operations (Create, Read, Update, Delete) on restaurants
 * and related menu entities.
 */
public class RestaurantDAO {

    /**
     * Retrieves all restaurants from the database.
     *
     * @return a list of all RestaurantEntity objects from the database. Returns an empty list if no restaurants are found.
     */
    public static List<RestaurantEntity> getAllRestaurants() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM RestaurantEntity", RestaurantEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a restaurant by its unique identifier.
     *
     * @param id the UUID of the restaurant to retrieve.
     * @return the RestaurantEntity associated with the provided UUID, or null if no matching restaurant is found.
     */
    public static RestaurantEntity getRestaurantById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RestaurantEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saves a new or updates an existing restaurant in the database.
     *
     * @param restaurantEntity the RestaurantEntity object to save or update.
     * @return an HttpResponse containing the status code and either the ID of the newly created restaurant
     *         or an error message in case of failure.
     */
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

    /**
     * Deletes a restaurant by its unique identifier.
     *
     * @param id the UUID of the restaurant to delete.
     * @return an HttpResponse containing the status code and a success or error message.
     */
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

    /**
     * Retrieves a menu by its unique identifier.
     *
     * @param id the UUID of the menu to retrieve.
     * @return the MenuEntity associated with the provided UUID, or null if no matching menu is found.
     */
    public static MenuEntity getMenuById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(MenuEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the menu associated with a specific menu item.
     *
     * @param id the UUID of the menu item whose associated menu is to be retrieved.
     * @return the MenuEntity associated with the provided menu item UUID, or null if no matching menu is found.
     */
    public static MenuEntity getMenuByMenuItemId(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            MenuItemEntity menuItemEntity = session.get(MenuItemEntity.class, id);
            return menuItemEntity.getMenuEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
