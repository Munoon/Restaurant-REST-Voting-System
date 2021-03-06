package com.train4game.munoon.model;

import com.train4game.munoon.View;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "user_votes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"}, name = "users_votes_unique_date_idx"))
@NamedEntityGraph(
        name = Vote.WITH_PARENTS,
        attributeNodes = {
                @NamedAttributeNode(value = "user", subgraph = "roles"),
                @NamedAttributeNode(value = "restaurant", subgraph = "menu")
        },
        subgraphs = {
                @NamedSubgraph(name = "roles", attributeNodes = @NamedAttributeNode("roles")),
                @NamedSubgraph(name = "menu", attributeNodes = @NamedAttributeNode("menu"))
        }
)
public class Vote extends AbstractBaseEntity {
    public static final String WITH_PARENTS = "Vote.withParents";

    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100"))
    @OneToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private User user;

    @JoinColumn(name = "restaurant_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100"))
    @OneToOne(fetch = FetchType.EAGER)
    private Restaurant restaurant;

    @Column(name = "date", nullable = false, columnDefinition = "DATE DEFAULT now()")
    @NotNull
    private LocalDate date;

    public Vote() {
    }

    public Vote(Vote v) {
        this(v.getId(), v.getUser(), v.getRestaurant(), v.getDate());
    }

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate time) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
