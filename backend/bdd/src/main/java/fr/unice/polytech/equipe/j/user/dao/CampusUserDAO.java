package fr.unice.polytech.equipe.j.user.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.user.entities.CampusUserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CampusUserDAO {
    public static void save(CampusUserEntity user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error while saving user: " + e.getMessage());
        }
    }

    public static List<CampusUserEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from CampusUserEntity", CampusUserEntity.class).list();
        } catch (Exception e) {
            System.out.println("Error while getting all users: " + e.getMessage());
            return null;
        }
    }
}
