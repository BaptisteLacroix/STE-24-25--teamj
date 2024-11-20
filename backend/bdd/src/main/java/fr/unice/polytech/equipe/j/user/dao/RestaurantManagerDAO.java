package fr.unice.polytech.equipe.j.user.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.user.entities.RestaurantManagerEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RestaurantManagerDAO {
    public static void save(RestaurantManagerEntity manager) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(manager);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error while saving manager: " + e.getMessage());
        }
    }

    public static List<RestaurantManagerEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from RestaurantManagerEntity", RestaurantManagerEntity.class).list();
        } catch (Exception e) {
            System.out.println("Error while getting all managers: " + e.getMessage());
            return null;
        }
    }
}
