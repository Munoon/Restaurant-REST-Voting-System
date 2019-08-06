package com.train4game.munoon.to.meal;

import com.train4game.munoon.to.AbstractBaseTo;

import java.time.LocalDate;

public class MealToWithRestaurant extends AbstractMealTo {
    private RestaurantForMealTo restaurant;

    public MealToWithRestaurant() {
    }

    public MealToWithRestaurant(Integer id, String name, int price, LocalDate date, RestaurantForMealTo restaurant) {
        super(id, name, price, date);
        this.restaurant = restaurant;
    }

    public RestaurantForMealTo getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantForMealTo restaurant) {
        this.restaurant = restaurant;
    }
}

class RestaurantForMealTo extends AbstractBaseTo {
    String name;

    public RestaurantForMealTo() {
    }

    public RestaurantForMealTo(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RestaurantForMealTo{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}