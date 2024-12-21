package fr.unice.polytech.equipe.j;

public class RestaurantServiceApplication {
    public static void main(String[] args) {
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5007);
        server.start();
    }
}
