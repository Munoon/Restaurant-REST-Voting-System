package com.train4game.munoon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "meals", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "meal_unique_name_idx"))
public class Meal extends AbstractNamedEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Nullable
    @JsonIgnore
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
