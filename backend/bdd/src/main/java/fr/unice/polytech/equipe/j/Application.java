package fr.unice.polytech.equipe.j;

public class Application {
    public static void main(String[] args) {
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5000);
        server.start();
    }
}
