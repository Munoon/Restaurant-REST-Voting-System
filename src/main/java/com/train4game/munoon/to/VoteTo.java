package com.train4game.munoon.to;

import com.train4game.munoon.model.AbstractBaseEntity;

import java.time.LocalDate;

public class VoteTo extends AbstractBaseEntity {
    private int restaurantId;
    private LocalDate date = LocalDate.now();

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
}
