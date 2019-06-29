package com.train4game.munoon.model;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

public class Vote extends AbstractBaseEntity {
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100"))
    @ManyToOne(fetch = FetchType.LAZY)
    @Null
    private User user;

    @JoinColumn(name = "restaurant_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100"))
    @ManyToOne(fetch = FetchType.LAZY)
    @Null
    private Restaurant restaurant;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
    private LocalDateTime date;

    public Vote() {
    }

    public Vote(Vote v) {
        this(v.getId(), v.getUser(), v.getRestaurant(), v.getDate());
    }

    public Vote(Integer id, User user, Restaurant restaurant, LocalDateTime time) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.date = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
