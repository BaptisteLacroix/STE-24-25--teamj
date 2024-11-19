package fr.unice.polytech.equipe.j.order.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.order.entities.DeliveryLocationEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DeliveryLocationDAO {
    public static List<DeliveryLocationEntity> getAllDeliveryLocations() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM DeliveryLocationEntity", DeliveryLocationEntity.class).list();
        }
    }

    public static DeliveryLocationEntity getDeliveryLocationById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(DeliveryLocationEntity.class, id);
        }
    }

    public static DeliveryLocationEntity getDeliveryLocationByLocationName(String locationName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM DeliveryLocationEntity WHERE locationName = :locationName", DeliveryLocationEntity.class)
                    .setParameter("locationName", locationName)
                    .uniqueResult();
        }
    }

    public static void save(DeliveryLocationEntity deliveryLocationEntity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(deliveryLocationEntity);
            transaction.commit();
        }
    }

    public static void delete(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            DeliveryLocationEntity deliveryLocationEntity = session.get(DeliveryLocationEntity.class, id);
            if (deliveryLocationEntity != null) {
                session.delete(deliveryLocationEntity);
            }
            transaction.commit();
        }
    }
}
