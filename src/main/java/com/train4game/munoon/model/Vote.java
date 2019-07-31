package com.train4game.munoon.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_votes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"}, name = "users_votes_unique_date_idx"))
public class Vote extends AbstractBaseEntity {
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100"))
    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @JoinColumn(name = "restaurant_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100"))
    @OneToOne(fetch = FetchType.EAGER)
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

    @Override
    public String toString() {
        return "Vote{" +
                "user=" + user +
                ", restaurant=" + restaurant +
                ", date=" + date +
                ", id=" + id +
                '}';
    }
}
