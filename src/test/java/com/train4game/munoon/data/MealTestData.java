package com.train4game.munoon.data;

import com.train4game.munoon.model.Meal;

import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT;
import static com.train4game.munoon.data.RestaurantTestData.SECOND_RESTAURANT;

public class MealTestData {
    public static final int FIRST_MEAL_ID = 105;
    public static final Meal FIRST_MEAL = new Meal(FIRST_MEAL_ID, "Burger", FIRST_RESTAURANT, 50);
    public static final Meal SECOND_MEAL = new Meal(FIRST_MEAL_ID + 1, "French Fries", FIRST_RESTAURANT, 20);
    public static final Meal THIRD_MEAL = new Meal(FIRST_MEAL_ID + 2, "Burger", SECOND_RESTAURANT, 30);
    public static final Meal FOURTH_MEAL = new Meal(FIRST_MEAL_ID + 3, "Chicken", SECOND_RESTAURANT, 35);

    private MealTestData() {
    }
}
