package fr.unice.polytech.equipe.j;

public class Application {
    public static void main(String[] args) {
        if (args.length > 0) {
            FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", args[0], 5000);
            server.start();
        } else {
            FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5000);
            server.start();
        }
    }
}
