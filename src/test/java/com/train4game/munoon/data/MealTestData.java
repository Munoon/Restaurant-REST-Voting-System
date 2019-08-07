package com.train4game.munoon.data;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.to.MealTo;
import com.train4game.munoon.to.MealToWithRestaurant;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.train4game.munoon.TestUtil.readFromJsonMvcResult;
import static com.train4game.munoon.TestUtil.readListFromJsonMvcResult;
import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT;
import static com.train4game.munoon.data.RestaurantTestData.SECOND_RESTAURANT;
import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int FIRST_MEAL_ID = 105;
    public static final Meal FIRST_MEAL = new Meal(FIRST_MEAL_ID, "Burger", FIRST_RESTAURANT, 50, LocalDate.of(2019, 8, 6));
    public static final Meal SECOND_MEAL = new Meal(FIRST_MEAL_ID + 1, "French Fries", FIRST_RESTAURANT, 20, LocalDate.of(2019, 8, 6));
    public static final Meal THIRD_MEAL = new Meal(FIRST_MEAL_ID + 2, "Burger", SECOND_RESTAURANT, 30, LocalDate.of(2019, 8, 6));
    public static final Meal FOURTH_MEAL = new Meal(FIRST_MEAL_ID + 3, "Chicken", FIRST_RESTAURANT, 35, LocalDate.of(2019, 8, 7));
    public static final List<Meal> FIRST_RESTAURANT_MENU = Arrays.asList(FOURTH_MEAL, FIRST_MEAL, SECOND_MEAL);

    private MealTestData() {
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "restaurant");
    }

    public static void assertMatchToWithRestaurant(MealToWithRestaurant actual, MealToWithRestaurant expected) {
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    public static void assertMatchWithRestaurant(Meal actual, Meal expected) {
        assertMatch(actual, expected);
        assertThat(actual.getRestaurant()).isEqualToComparingFieldByFieldRecursively(expected.getRestaurant());
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant").isEqualTo(expected);
    }

    public static void assertMatchMealTo(Iterable<MealTo> actual, Iterable<MealTo> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant").isEqualTo(expected);
    }

    public static void assertMatchMealTo(MealTo actual, MealTo expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "restaurant");
    }

    public static ResultMatcher contentJson(List<MealTo> expected) {
        return result -> assertMatchMealTo(readListFromJsonMvcResult(result, MealTo.class), expected);
    }

    public static ResultMatcher contentJson(MealTo expected) {
        return result -> assertMatchMealTo(readFromJsonMvcResult(result, MealTo.class), expected);
    }

    public static ResultMatcher contentJson(MealToWithRestaurant expected) {
        return result -> assertMatchToWithRestaurant(readFromJsonMvcResult(result, MealToWithRestaurant.class), expected);
    }
}
