package com.train4game.munoon.data;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.to.RestaurantTo;
import com.train4game.munoon.to.RestaurantToWithVotes;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;

import static com.train4game.munoon.TestUtil.readFromJsonMvcResult;
import static com.train4game.munoon.TestUtil.readListFromJsonMvcResult;
import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {
    public static final int FIRST_RESTAURANT_ID = 103;
    public static final int SECOND_RESTAURANT_ID = 104;
    public static final Restaurant FIRST_RESTAURANT = new Restaurant(FIRST_RESTAURANT_ID, "McDonalds");
    public static final Restaurant SECOND_RESTAURANT = new Restaurant(SECOND_RESTAURANT_ID, "KFC");

    private RestaurantTestData() {
    }

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "menu");
    }

    public static void assertMatch(RestaurantToWithVotes actual, RestaurantToWithVotes expected) {
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("menu").isEqualTo(expected);
    }

    public static void assertMatchWithVotes(Iterable<RestaurantToWithVotes> actual, Iterable<RestaurantToWithVotes> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static void assertMatchTo(RestaurantTo actual, RestaurantTo expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "menu");
    }

    public static void assertMatchTo(Iterable<RestaurantTo> actual, Iterable<RestaurantTo> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("menu").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(List<RestaurantTo> expected) {
        return result -> assertMatchTo(readListFromJsonMvcResult(result, RestaurantTo.class), expected);
    }

    public static ResultMatcher contentJsonWithVotes(List<RestaurantToWithVotes> expected) {
        return result -> assertMatchWithVotes(readListFromJsonMvcResult(result, RestaurantToWithVotes.class), expected);
    }

    public static ResultMatcher contentJson(RestaurantTo expected) {
        return result -> assertMatchTo(readFromJsonMvcResult(result, RestaurantTo.class), expected);
    }

    public static ResultMatcher contentJson(RestaurantToWithVotes expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, RestaurantToWithVotes.class), expected);
    }
}
