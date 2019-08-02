package com.train4game.munoon.to.meal;

import java.time.LocalDate;

public class MealTo extends AbstractMealTo {
    private int restaurant;

    public MealTo() {
    }

    public MealTo(Integer id, String name, int price, LocalDate date, int restaurant) {
        super(id, name, price, date);
        this.restaurant = restaurant;
    }

    public int getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(int restaurant) {
        this.restaurant = restaurant;
    }
}