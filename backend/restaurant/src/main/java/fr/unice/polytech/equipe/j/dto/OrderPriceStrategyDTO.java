package fr.unice.polytech.equipe.j.dto;

public enum OrderPriceStrategyDTO {
    FREE_ITEM("free_item"),
    KPERCENT("kpercent");

    private final String strategy;

    OrderPriceStrategyDTO(String strategy) {
        this.strategy = strategy;
    }
}
