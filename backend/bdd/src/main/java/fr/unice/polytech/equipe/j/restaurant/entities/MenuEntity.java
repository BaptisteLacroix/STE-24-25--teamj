package fr.unice.polytech.equipe.j.restaurant.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "menus")
@Getter
@Setter
public class MenuEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID uuid;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menuEntity", fetch = FetchType.EAGER)
    private List<MenuItemEntity> items = new ArrayList<>();

}
