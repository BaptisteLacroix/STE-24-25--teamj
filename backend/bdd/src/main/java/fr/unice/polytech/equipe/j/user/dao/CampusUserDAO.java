package fr.unice.polytech.equipe.j.user.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.user.entities.CampusUserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CampusUserDAO {
    public static HttpResponse save(CampusUserEntity user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            return new HttpResponse(HttpCode.HTTP_201, user.getId());
        } catch (Exception e) {
            System.out.println("Error while saving user: " + e.getMessage());
            return new HttpResponse(HttpCode.HTTP_500, "Error while saving user");
        }
    }

    public static List<CampusUserEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from CampusUserEntity", CampusUserEntity.class).list();
        } catch (Exception e) {
            System.out.println("Error while getting all users: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static CampusUserEntity getUserById(UUID userUuid) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(CampusUserEntity.class, userUuid);
        } catch (Exception e) {
            System.out.println("Error while getting user by id: " + e.getMessage());
            return null;
        }
    }

    public static HttpResponse delete(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            CampusUserEntity user = session.get(CampusUserEntity.class, id);
            session.delete(user);
            tx.commit();
            return new HttpResponse(HttpCode.HTTP_200, "User deleted successfully");
        } catch (Exception e) {
            System.out.println("Error while deleting user: " + e.getMessage());
            return new HttpResponse(HttpCode.HTTP_500, "Error while deleting user");
        }
    }
}
