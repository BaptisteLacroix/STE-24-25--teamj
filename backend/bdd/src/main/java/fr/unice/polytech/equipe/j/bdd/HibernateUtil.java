package fr.unice.polytech.equipe.j.bdd;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private HibernateUtil() {
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

    public static Connection connect() throws IOException {
        // Get the path to the resource folder
        // Show the content of the file in resources/bdd.db
        URL is = HibernateUtil.class.getClassLoader().getResource("bdd.db");
        if (is == null) {
            throw new RuntimeException("File not found");
        }
        String url = "jdbc:sqlite:" + is.getPath();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database: " + e.getMessage());
        }
        return conn;
    }
}
