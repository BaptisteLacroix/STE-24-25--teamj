package fr.unice.polytech.equipe.j;

public class GatewayApplication {
    public static void main(String[] args) {
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 8080);
        server.start();
    }
}
