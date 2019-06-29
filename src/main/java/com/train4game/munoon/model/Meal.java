package com.train4game.munoon.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;

public class Meal extends AbstractNamedEntity {
    @JoinColumn(name = "restaurant_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100"))
    @ManyToOne(fetch = FetchType.LAZY)
    @Nullable
    private Restaurant restaurant;

    @Column(name = "price", nullable = false)
    private int price;

    public Meal() {
    }

    public Meal(Meal m) {
        this(m.getId(), m.getName(), m.getRestaurant(), m.getPrice());
    }

    public Meal(Integer id, String name, Restaurant restaurant, int price) {
        super(id, name);
        this.restaurant = restaurant;
        this.price = price;
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
}
