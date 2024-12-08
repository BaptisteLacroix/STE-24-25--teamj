package fr.unice.polytech.equipe.j;

import java.nio.file.Paths;

public class Application {
    public static void main(String[] args) {
        if (args.length > 0) {
            String userDir = System.getProperty("user.dir");
            String bddModulePath = Paths.get(System.getProperty("user.dir")).getParent().getParent().toString();
            System.setProperty("user.dir", bddModulePath);

            // Set the hibernate.cfg.path system property to the test one hibernate-test.cfg.xml
            System.setProperty("hibernate.cfg.path", "hibernate-test.cfg.xml");

            FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", args[0], 5000);
            server.start();
            System.setProperty("user.dir", userDir);
        } else {
            FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5000);
            server.start();
        }
    }
}
