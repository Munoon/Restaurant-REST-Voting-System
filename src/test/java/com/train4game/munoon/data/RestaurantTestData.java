package com.train4game.munoon.data;

import com.train4game.munoon.model.Restaurant;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {
    public static final int FIRST_RESTAURANT_ID = 103;
    public static final Restaurant FIRST_RESTAURANT = new Restaurant(FIRST_RESTAURANT_ID, "McDonalds");
    public static final Restaurant SECOND_RESTAURANT = new Restaurant(FIRST_RESTAURANT_ID + 1, "KFC");

    private RestaurantTestData() {
    }

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "menu");
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("menu").isEqualTo(expected);
    }
}
