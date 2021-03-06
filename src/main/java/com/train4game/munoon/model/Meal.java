package com.train4game.munoon.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "meals", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "meal_unique_name_idx"))
@NamedEntityGraph(
        name = Meal.WITH_PARENTS,
        attributeNodes = @NamedAttributeNode(value = "restaurant", subgraph = "menu"),
        subgraphs = @NamedSubgraph(name = "menu", attributeNodes = @NamedAttributeNode("menu"))
)
public class Meal extends AbstractNamedEntity {
    public static final String WITH_PARENTS = "Meal.withParents";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Nullable
    private Restaurant restaurant;

    @Column(name = "price", nullable = false)
    @Range(min = 10, max = 50000)
    private int price;

    @Column(name = "date", nullable = false, columnDefinition = "DATE DEFAULT now()")
    @NotNull
    private LocalDate date;

    public Meal() {
    }

    public Meal(Meal m) {
        this(m.getId(), m.getName(), m.getRestaurant(), m.getPrice(), m.getDate());
    }

    public Meal(Integer id, String name, Restaurant restaurant, int price, LocalDate date) {
        super(id, name);
        this.restaurant = restaurant;
        this.price = price;
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "price=" + price +
                ", date=" + date +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
