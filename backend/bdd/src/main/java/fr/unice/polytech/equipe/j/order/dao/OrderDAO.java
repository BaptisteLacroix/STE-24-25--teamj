package fr.unice.polytech.equipe.j.order.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.order.entities.IndividualOrderEntity;
import fr.unice.polytech.equipe.j.order.entities.OrderEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object (DAO) for managing order entities in the database.
 * Provides methods for CRUD operations (Create, Read, Update, Delete) on orders and individual orders.
 */
public class OrderDAO {

    /**
     * Retrieves all orders from the database.
     *
     * @return a list of all OrderEntity objects from the database. Returns an empty list if no orders are found or if an error occurs.
     */
    public static List<OrderEntity> getAllOrders() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM OrderEntity", OrderEntity.class).list();
        } catch (Exception e) {
            System.out.println("Error while getting all orders: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves all individual orders from the database.
     *
     * @return a list of all IndividualOrderEntity objects from the database. Returns an empty list if no individual orders are found or if an error occurs.
     */
    public static List<IndividualOrderEntity> getAllIndividualOrders() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM IndividualOrderEntity", IndividualOrderEntity.class).list();
        } catch (Exception e) {
            System.out.println("Error while getting all orders: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves an order by its unique identifier.
     *
     * @param id the UUID of the order to retrieve.
     * @return the OrderEntity associated with the provided UUID, or null if no matching order is found or if an error occurs.
     */
    public static OrderEntity getOrderById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(OrderEntity.class, id);
        } catch (Exception e) {
            System.out.println("Error while getting order by id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves an individual order by its unique identifier.
     *
     * @param id the UUID of the individual order to retrieve.
     * @return the IndividualOrderEntity associated with the provided UUID, or null if no matching order is found or if an error occurs.
     */
    public static IndividualOrderEntity getIndividualOrderById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(IndividualOrderEntity.class, id);
        } catch (Exception e) {
            System.out.println("Error while getting individual order by id: " + e.getMessage());
            return null;
        }
    }

    /**
     * Saves a new or updates an existing order in the database.
     *
     * @param orderEntity the OrderEntity object to save or update. If the entity exists, it will be merged; otherwise, a new entity will be created.
     * @return an HttpResponse containing the status code and either the ID of the newly created order or an error message in case of failure.
     */
    public static HttpResponse save(OrderEntity orderEntity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(orderEntity);
            transaction.commit();
            return new HttpResponse(HttpCode.HTTP_201, orderEntity.getId());
        } catch (Exception e) {
            System.out.println("Error while saving order: " + e.getMessage());
            return new HttpResponse(HttpCode.HTTP_500, "Error while saving order");
        }
    }

    /**
     * Deletes an order by its unique identifier.
     *
     * @param id the UUID of the order to delete.
     * @return an HttpResponse containing the status code and a success or error message.
     */
    public static HttpResponse delete(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            OrderEntity orderEntity = session.get(OrderEntity.class, id);
            if (orderEntity != null) {
                session.delete(orderEntity);
            }
            transaction.commit();
            return new HttpResponse(HttpCode.HTTP_200, "Order deleted successfully");
        } catch (Exception e) {
            System.out.println("Error while deleting order: " + e.getMessage());
            return new HttpResponse(HttpCode.HTTP_500, "Error while deleting order");
        }
    }

    /**
     * Saves a new or updates an existing individual order in the database.
     *
     * @param individualOrderEntity the IndividualOrderEntity object to save or update. If the entity exists, it will be merged; otherwise, a new entity will be created.
     * @return an HttpResponse containing the status code and either the ID of the newly created individual order or an error message in case of failure.
     */
    public static HttpResponse save(IndividualOrderEntity individualOrderEntity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(individualOrderEntity);
            transaction.commit();
            return new HttpResponse(HttpCode.HTTP_201, individualOrderEntity.getId());
        } catch (Exception e) {
            System.out.println("Error while saving individual order: " + e.getMessage());
            return new HttpResponse(HttpCode.HTTP_500, "Error while saving individual order");
        }
    }
}
