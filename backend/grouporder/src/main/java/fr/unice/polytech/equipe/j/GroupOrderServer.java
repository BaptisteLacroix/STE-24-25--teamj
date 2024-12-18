package fr.unice.polytech.equipe.j;

public class GroupOrderServer {
    public static void main(String[] args) {
        new FlexibleRestServer("fr.unice.polytech.equipe.j",5008).start();
    }
}
