package com.train4game.munoon.data;

import com.train4game.munoon.model.Vote;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT;
import static com.train4game.munoon.data.RestaurantTestData.SECOND_RESTAURANT;
import static com.train4game.munoon.data.UserTestData.FIRST_USER;
import static com.train4game.munoon.data.UserTestData.SECOND_USER;
import static org.assertj.core.api.Assertions.assertThat;

public class VoteTestData {
    public static final int FIRST_VOTE_ID = 109;
    public static final Vote FIRST_VOTE = new Vote(FIRST_VOTE_ID, FIRST_USER, SECOND_RESTAURANT, LocalDateTime.of(2019, 7, 1, 10, 0, 0));
    public static final Vote SECOND_VOTE = new Vote(FIRST_VOTE_ID + 1, FIRST_USER, FIRST_RESTAURANT, LocalDateTime.of(2019, 7, 2, 10, 0, 0));
    public static final Vote THIRD_VOTE = new Vote(FIRST_VOTE_ID + 2, SECOND_USER, FIRST_RESTAURANT, LocalDateTime.of(2019, 7, 1, 10, 0, 0));

    private VoteTestData() {
    }

    public static void assertMatch(Vote actual, Vote expected) {
        assertThat(actual.getUser()).isEqualToIgnoringGivenFields(expected.getUser(), "registered");
        assertThat(actual.getRestaurant()).isEqualToComparingFieldByField(expected.getRestaurant());
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user", "restaurant", "date");
    }

    public static void assertMatch(Iterable<Vote> actual, Vote... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("date", "user", "restaurant").isEqualTo(expected);
    }
}
