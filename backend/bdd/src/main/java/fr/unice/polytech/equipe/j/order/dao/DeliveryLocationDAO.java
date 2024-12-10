package fr.unice.polytech.equipe.j.order.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.order.entities.DeliveryLocationEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.UUID;

/**
 * Data Access Object (DAO) for managing delivery location entities in the database.
 * Provides methods for CRUD operations (Create, Read, Update, Delete) on delivery locations.
 */
public class DeliveryLocationDAO {

    /**
     * Retrieves all delivery locations from the database.
     *
     * @return a list of all DeliveryLocationEntity objects from the database, or null if an error occurs.
     */
    public static List<DeliveryLocationEntity> getAllDeliveryLocations() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM DeliveryLocationEntity", DeliveryLocationEntity.class).list();
        } catch (Exception e) {
            System.out.println("Error while getting all delivery locations: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a delivery location by its unique identifier.
     *
     * @param id the UUID of the delivery location to retrieve.
     * @return the DeliveryLocationEntity associated with the provided UUID, or null if no matching location is found.
     */
    public static DeliveryLocationEntity getDeliveryLocationById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(DeliveryLocationEntity.class, id);
        } catch (Exception e) {
            System.out.println("Error while getting delivery location by id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a delivery location by its location name.
     *
     * @param locationName the name of the delivery location to retrieve.
     * @return the DeliveryLocationEntity associated with the provided location name, or null if no matching location is found.
     */
    public static DeliveryLocationEntity getDeliveryLocationByLocationName(String locationName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM DeliveryLocationEntity WHERE locationName = :locationName", DeliveryLocationEntity.class)
                    .setParameter("locationName", locationName)
                    .uniqueResult();
        } catch (Exception e) {
            System.out.println("Error while getting delivery location by location name: " + e.getMessage());
            return null;
        }
    }

    /**
     * Saves a new or updates an existing delivery location in the database.
     *
     * @param deliveryLocationEntity the DeliveryLocationEntity object to save or update.
     *                               If the entity exists, it will be merged; if not, a new entity will be created.
     */
    public static void save(DeliveryLocationEntity deliveryLocationEntity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(deliveryLocationEntity);
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Error while saving delivery location: " + e.getMessage());
        }
    }

    /**
     * Deletes a delivery location by its unique identifier.
     *
     * @param id the identifier (UUID) of the delivery location to delete.
     */
    public static void delete(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            DeliveryLocationEntity deliveryLocationEntity = session.get(DeliveryLocationEntity.class, id);
            if (deliveryLocationEntity != null) {
                session.delete(deliveryLocationEntity);
            }
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Error while deleting delivery location: " + e.getMessage());
        }
    }
}