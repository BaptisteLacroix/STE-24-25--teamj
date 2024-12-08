package fr.unice.polytech.equipe.j.user.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.user.entities.RestaurantManagerEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object (DAO) for managing RestaurantManagerEntity objects in the database.
 * Provides methods for CRUD operations (Create, Read, Update, Delete) on restaurant manager entities.
 */
public class RestaurantManagerDAO {

    /**
     * Saves or updates a RestaurantManagerEntity in the database.
     * If the manager already exists, the entity will be merged; otherwise, a new manager will be created.
     *
     * @param manager the RestaurantManagerEntity to save or update.
     * @return an HttpResponse containing the HTTP status code and the ID of the saved or updated manager.
     */
    public static HttpResponse save(RestaurantManagerEntity manager) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(manager);
            tx.commit();
            return new HttpResponse(HttpCode.HTTP_201, manager.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(HttpCode.HTTP_500, "Internal server error");
        }
    }

    /**
     * Retrieves all RestaurantManagerEntity objects from the database.
     *
     * @return a list of all RestaurantManagerEntity objects, or an empty list if an error occurs.
     */
    public static List<RestaurantManagerEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from RestaurantManagerEntity", RestaurantManagerEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a RestaurantManagerEntity by its unique identifier.
     *
     * @param id the UUID of the restaurant manager to retrieve.
     * @return the RestaurantManagerEntity associated with the provided UUID, or null if no matching manager is found or an error occurs.
     */
    public static RestaurantManagerEntity getManagerById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RestaurantManagerEntity.class, id);
        } catch (Exception e) {
            System.out.println("Error while getting manager by id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a RestaurantManagerEntity by its unique identifier.
     *
     * @param id the UUID of the manager to delete.
     * @return an HttpResponse containing the status code and a success or error message.
     */
    public static HttpResponse delete(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            RestaurantManagerEntity manager = session.get(RestaurantManagerEntity.class, id);
            if (manager != null) {
                session.delete(manager);
            }
            tx.commit();
            return new HttpResponse(HttpCode.HTTP_200, "Manager deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(HttpCode.HTTP_500, "Internal server error");
        }
    }
}
