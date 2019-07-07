package com.train4game.munoon.data;

import com.train4game.munoon.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT;
import static com.train4game.munoon.data.RestaurantTestData.SECOND_RESTAURANT;
import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int FIRST_MEAL_ID = 105;
    public static final Meal FIRST_MEAL = new Meal(FIRST_MEAL_ID, "Burger", FIRST_RESTAURANT, 50, LocalDateTime.now());
    public static final Meal SECOND_MEAL = new Meal(FIRST_MEAL_ID + 1, "French Fries", FIRST_RESTAURANT, 20, LocalDateTime.now());
    public static final Meal THIRD_MEAL = new Meal(FIRST_MEAL_ID + 2, "Burger", SECOND_RESTAURANT, 30, LocalDateTime.now());
    public static final Meal FOURTH_MEAL = new Meal(FIRST_MEAL_ID + 3, "Chicken", SECOND_RESTAURANT, 35, LocalDateTime.now());

    private MealTestData() {
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "restaurant", "date");
    }

    public static void assertMatchWithRestaurant(Meal actual, Meal expected) {
        assertMatch(actual, expected);
        assertThat(actual.getRestaurant()).isEqualToComparingFieldByField(expected.getRestaurant());
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant", "date").isEqualTo(expected);
    }
}
