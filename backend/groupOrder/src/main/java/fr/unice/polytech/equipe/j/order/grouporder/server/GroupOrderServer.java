package java.fr.unice.polytech.equipe.j.order.grouporder.server;

import fr.unice.polytech.equipe.j.flexiblerestserver.FlexibleRestServer;

public class GroupOrderServer {
    public static void main(String[] args) {
        new FlexibleRestServer("fr.unice.polytech.equipe.j.order.grouporder.server",8081).start();
    }
}
