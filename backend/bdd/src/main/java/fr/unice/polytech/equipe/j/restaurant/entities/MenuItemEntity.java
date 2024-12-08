package fr.unice.polytech.equipe.j.restaurant.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.unice.polytech.equipe.j.order.entities.OrderEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
public class MenuItemEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int prepTime;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuEntity menuEntity;

    @ManyToMany(mappedBy = "items")
    @JsonIgnore
    private List<OrderEntity> orders = new ArrayList<>();

    @Override
    @JsonIgnore
    public String toString() {
        return "MenuItemEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prepTime=" + prepTime +
                ", price=" + price +
                ", menuEntity=" + menuEntity +
                '}';
    }
}
