package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.user.ConnectedUser;

public record DeliverableOrder (
    Order order,
    ConnectedUser user
){}
