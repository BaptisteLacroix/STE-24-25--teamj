package fr.unice.polytech.equipe.j.user.dao;

import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.user.entities.RestaurantManagerEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RestaurantManagerDAO {
    public static HttpResponse save(RestaurantManagerEntity manager) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(manager);
            tx.commit();
            return new HttpResponse(HttpCode.HTTP_201, manager.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(HttpCode.HTTP_500, "Internal server error");
        }
    }

    public static List<RestaurantManagerEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from RestaurantManagerEntity", RestaurantManagerEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static RestaurantManagerEntity getManagerById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RestaurantManagerEntity.class, id);
        } catch (Exception e) {
            System.out.println("Error while getting manager by id: " + e.getMessage());
            return null;
        }
    }

    public static HttpResponse delete(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            RestaurantManagerEntity manager = session.get(RestaurantManagerEntity.class, id);
            session.delete(manager);
            tx.commit();
            return new HttpResponse(HttpCode.HTTP_200, "Manager deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(HttpCode.HTTP_500, "Internal server error");
        }
    }
}
