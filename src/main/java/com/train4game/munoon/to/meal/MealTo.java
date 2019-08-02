package com.train4game.munoon.to.meal;

import java.time.LocalDate;

public class MealTo extends AbstractMealTo {
    private int restaurantId;

    public MealTo() {
    }

    public MealTo(Integer id, String name, int price, LocalDate date, int restaurantId) {
        super(id, name, price, date);
        this.restaurantId = restaurantId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }
}