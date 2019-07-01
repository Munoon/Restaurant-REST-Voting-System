package com.train4game.munoon.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal m WHERE m.id=:id"),
        @NamedQuery(name = Meal.GET_ALL, query = "SELECT m FROM Meal m LEFT JOIN FETCH m.restaurant " +
                "WHERE m.restaurant.id=:restaurantId " +
                "ORDER BY m.name, m.date")
})
@Entity
@Table(name = "meals", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "meal_unique_name_idx"))
public class Meal extends AbstractNamedEntity {
    public static final String DELETE = "Meal.delete";
    public static final String GET_ALL = "Meal.getAll";

    @JoinColumn(name = "restaurant_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100"))
    @ManyToOne(fetch = FetchType.LAZY)
    @Nullable
    private Restaurant restaurant;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
    private LocalDateTime date;

    public Meal() {
    }

    public Meal(Meal m) {
        this(m.getId(), m.getName(), m.getRestaurant(), m.getPrice(), m.getDate());
    }

    public Meal(Integer id, String name, Restaurant restaurant, int price, LocalDateTime date) {
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
