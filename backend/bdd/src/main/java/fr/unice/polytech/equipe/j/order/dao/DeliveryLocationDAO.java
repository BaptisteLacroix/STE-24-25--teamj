package fr.unice.polytech.equipe.j.order.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.order.entities.DeliveryLocationEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.UUID;

public class DeliveryLocationDAO {
    public static List<DeliveryLocationEntity> getAllDeliveryLocations() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM DeliveryLocationEntity", DeliveryLocationEntity.class).list();
        } catch (Exception e) {
            System.out.println("Error while getting all delivery locations: " + e.getMessage());
            return null;
        }
    }

    public static DeliveryLocationEntity getDeliveryLocationById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(DeliveryLocationEntity.class, id);
        } catch (Exception e) {
            System.out.println("Error while getting delivery location by id: " + e.getMessage());
            return null;
        }
    }

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

    public static void save(DeliveryLocationEntity deliveryLocationEntity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(deliveryLocationEntity);
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Error while saving delivery location: " + e.getMessage());
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
        } catch (Exception e) {
            System.out.println("Error while deleting delivery location: " + e.getMessage());
        }
    }
}
