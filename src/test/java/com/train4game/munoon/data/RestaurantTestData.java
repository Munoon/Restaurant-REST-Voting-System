package com.train4game.munoon.data;

import com.train4game.munoon.model.Restaurant;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;

import static com.train4game.munoon.TestUtil.readFromJsonMvcResult;
import static com.train4game.munoon.TestUtil.readListFromJsonMvcResult;
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

    public static ResultMatcher contentJson(Restaurant... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, Restaurant.class), List.of(expected));
    }

    public static ResultMatcher contentJson(Restaurant expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, Restaurant.class), expected);
    }
}
