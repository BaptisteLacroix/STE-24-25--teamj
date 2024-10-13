package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.util.Map;

public record GroupOrder(Map<Order, ConnectedUser> orderToConnectedUserMap) {
}
