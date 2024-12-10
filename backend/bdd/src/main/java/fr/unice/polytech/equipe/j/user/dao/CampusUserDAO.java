package fr.unice.polytech.equipe.j.user.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.user.entities.CampusUserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object (DAO) for managing CampusUserEntity objects in the database.
 * Provides methods for CRUD operations (Create, Read, Update, Delete) on campus user entities.
 */
public class CampusUserDAO {

    /**
     * Saves or updates a CampusUserEntity in the database.
     * If the user already exists, the entity will be merged; otherwise, a new user will be created.
     *
     * @param user the CampusUserEntity to save or update.
     * @return an HttpResponse containing the HTTP status code and the ID of the saved or updated user.
     */
    public static HttpResponse save(CampusUserEntity user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            return new HttpResponse(HttpCode.HTTP_201, user.getId());
        } catch (Exception e) {
            System.out.println("Error while saving user: " + e.getMessage());
            return new HttpResponse(HttpCode.HTTP_500, "Error while saving user");
        }
    }

    /**
     * Retrieves all CampusUserEntity objects from the database.
     *
     * @return a list of all CampusUserEntity objects, or an empty list if an error occurs.
     */
    public static List<CampusUserEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from CampusUserEntity", CampusUserEntity.class).list();
        } catch (Exception e) {
            System.out.println("Error while getting all users: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a CampusUserEntity by its unique identifier.
     *
     * @param userUuid the UUID of the user to retrieve.
     * @return the CampusUserEntity associated with the provided UUID, or null if no matching user is found or an error occurs.
     */
    public static CampusUserEntity getUserById(UUID userUuid) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(CampusUserEntity.class, userUuid);
        } catch (Exception e) {
            System.out.println("Error while getting user by id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a CampusUserEntity by its unique identifier.
     *
     * @param id the UUID of the user to delete.
     * @return an HttpResponse containing the status code and a success or error message.
     */
    public static HttpResponse delete(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            CampusUserEntity user = session.get(CampusUserEntity.class, id);
            if (user != null) {
                session.delete(user);
            }
            tx.commit();
            return new HttpResponse(HttpCode.HTTP_200, "User deleted successfully");
        } catch (Exception e) {
            System.out.println("Error while deleting user: " + e.getMessage());
            return new HttpResponse(HttpCode.HTTP_500, "Error while deleting user");
        }
    }
}