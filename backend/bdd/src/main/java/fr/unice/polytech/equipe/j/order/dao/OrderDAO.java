package fr.unice.polytech.equipe.j.order.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.order.entities.OrderEntity;
import java.util.Collections;
import fr.unice.polytech.equipe.j.order.entities.IndividualOrderEntity;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.UUID;

public class OrderDAO {

    // Fetch all orders
    public static List<OrderEntity> getAllOrders() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<OrderEntity> orderEntities = session.createQuery("FROM OrderEntity", OrderEntity.class).list();
            System.out.println("Order entities: " + orderEntities);
            return orderEntities;
        } catch (Exception e) {
            System.out.println("Error while getting all orders: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public static List<IndividualOrderEntity> getAllIndividualOrders() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM IndividualOrderEntity", IndividualOrderEntity.class).list();
        } catch (Exception e) {
            System.out.println("Error while getting all orders: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Fetch an order by its ID
    public static OrderEntity getOrderById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(OrderEntity.class, id);
        } catch (Exception e) {
            System.out.println("Error while getting order by id: " + e.getMessage());
            return null;
        }
    }

    // Fetch an individual order by its ID
    public static IndividualOrderEntity getIndividualOrderById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(IndividualOrderEntity.class, id);
        } catch (Exception e) {
            System.out.println("Error while getting individual order by id: " + e.getMessage());
            return null;
        }
    }

    // Save or update an order
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

    // Delete an order by its ID
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

    // Save or update an individual order (extends OrderEntity)
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
