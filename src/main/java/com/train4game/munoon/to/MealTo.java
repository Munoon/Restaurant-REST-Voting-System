package com.train4game.munoon.to;

import com.train4game.munoon.model.AbstractNamedEntity;

import java.time.LocalDate;

public class MealTo extends AbstractBaseTo {
    private String name;
    private int price;
    private LocalDate date = LocalDate.now();
    private int restaurant;

    public MealTo() {
    }

    public MealTo(Integer id, String name, int price, LocalDate date, int restaurant) {
        super(id);
        this.name = name;
        this.price = price;
        this.date = date;
        this.restaurant = restaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(int restaurant) {
        this.restaurant = restaurant;
    }
}
