package fr.unice.polytech.equipe.j.bdd.user.dao;

import fr.unice.polytech.equipe.j.bdd.HibernateUtil;
import fr.unice.polytech.equipe.j.bdd.user.entities.CampusUserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CampusUserDAO {
    public static void save(CampusUserEntity user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
        }
    }

    public static List<CampusUserEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from CampusUserEntity", CampusUserEntity.class).list();
        }
    }
}
