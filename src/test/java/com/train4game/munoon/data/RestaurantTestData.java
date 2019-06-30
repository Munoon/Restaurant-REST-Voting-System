package com.train4game.munoon.data;

import com.train4game.munoon.model.Restaurant;

public class RestaurantTestData {
    public static final int FIRST_RESTAURANT_ID = 103;
    public static final Restaurant FIRST_RESTAURANT = new Restaurant(FIRST_RESTAURANT_ID, "McDonalds");
    public static final Restaurant SECOND_RESTAURANT = new Restaurant(FIRST_RESTAURANT_ID + 1, "KFC");

    private RestaurantTestData() {
    }
}
