package com.train4game.munoon.to;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class VoteTo extends AbstractBaseTo {
    private int restaurantId;

    @NotNull
    private LocalDate date = LocalDate.now();

    private int userId;

    public VoteTo() {
    }

    public VoteTo(VoteTo v) {
        this(v.getId(), v.getRestaurantId(), v.getDate());
    }

    public VoteTo(Integer id, int restaurantId, LocalDate date) {
        super(id);
        this.restaurantId = restaurantId;
        this.date = date;
    }

    public VoteTo(Integer id, int restaurantId) {
        super(id);
        this.restaurantId = restaurantId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "restaurantId=" + restaurantId +
                ", date=" + date +
                ", userId=" + userId +
                ", id=" + id +
                '}';
    }
}
