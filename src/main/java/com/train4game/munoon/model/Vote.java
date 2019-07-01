package com.train4game.munoon.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(name = Vote.DELETE, query = "DELETE FROM Vote v WHERE v.id=:id AND v.user.id=:userId"),
        @NamedQuery(name = Vote.GET_ALL, query = "SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.date")
})
@Entity
@Table(name = "user_votes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"}, name = "users_votes_unique_date_idx"))
public class Vote extends AbstractBaseEntity {
    public static final String DELETE = "Vote.delete";
    public static final String GET_ALL = "Vote.getAll";

    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100"))
    @OneToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @Null
    private User user;

    @JoinColumn(name = "restaurant_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100"))
    @OneToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
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
