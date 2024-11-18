package fr.unice.polytech.equipe.j.bdd.user.dao;

import fr.unice.polytech.equipe.j.bdd.HibernateUtil;
import fr.unice.polytech.equipe.j.bdd.user.entities.RestaurantManagerEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RestaurantManagerDAO {
    public static void save(RestaurantManagerEntity manager) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(manager);
            tx.commit();
        }
    }

    public static List<RestaurantManagerEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from RestaurantManagerEntity", RestaurantManagerEntity.class).list();
        }
    }
}
