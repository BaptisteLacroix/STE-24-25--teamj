package fr.unice.polytech.equipe.j.order.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.order.entities.DeliveryLocationEntity;
import fr.unice.polytech.equipe.j.order.entities.GroupOrderEntity;
import fr.unice.polytech.equipe.j.order.entities.OrderEntity;
import fr.unice.polytech.equipe.j.restaurant.entities.RestaurantEntity;
import jakarta.persistence.criteria.Order;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.UUID;

public class GroupOrderDAO {
    public static GroupOrderEntity getGroupOrderById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(GroupOrderEntity.class, id);
        } catch (Exception e) {
            System.out.println("Error while getting groupOrder by id : " + e.getMessage());
            return null;
        }
    }


public static void save(GroupOrderEntity groupOrderEntity) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        Transaction transaction = session.beginTransaction();
        session.merge(groupOrderEntity);
        transaction.commit();
    } catch (Exception e) {
        System.out.println("Error while saving GroupOrder: " + e.getMessage());
    }
    }
}

