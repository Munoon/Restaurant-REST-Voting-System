package com.train4game.munoon.data;

import com.train4game.munoon.model.Vote;
import com.train4game.munoon.to.VoteTo;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.train4game.munoon.TestUtil.readFromJsonMvcResult;
import static com.train4game.munoon.TestUtil.readListFromJsonMvcResult;
import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT;
import static com.train4game.munoon.data.RestaurantTestData.SECOND_RESTAURANT;
import static com.train4game.munoon.data.UserTestData.FIRST_USER;
import static com.train4game.munoon.data.UserTestData.SECOND_USER;
import static org.assertj.core.api.Assertions.assertThat;

public class VoteTestData {
    public static final int FIRST_VOTE_ID = 108;
    public static final Vote FIRST_VOTE = new Vote(FIRST_VOTE_ID, FIRST_USER, SECOND_RESTAURANT, LocalDate.of(2019, 7, 1));
    public static final Vote SECOND_VOTE = new Vote(FIRST_VOTE_ID + 1, FIRST_USER, FIRST_RESTAURANT, LocalDate.of(2019, 7, 2));
    public static final Vote THIRD_VOTE = new Vote(FIRST_VOTE_ID + 2, SECOND_USER, FIRST_RESTAURANT, LocalDate.of(2019, 7, 1));

    private VoteTestData() {
    }

    public static void assertMatch(Vote actual, Vote expected) {
        assertThat(actual.getUser()).isEqualToIgnoringGivenFields(expected.getUser(), "registered", "password");
        assertThat(actual.getRestaurant()).isEqualToIgnoringGivenFields(expected.getRestaurant(), "menu");
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user", "restaurant");
    }

    public static void assertMatch(Iterable<Vote> actual, Vote... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user", "restaurant").isEqualTo(expected);
    }

    public static void assertMatchVoteTo(Iterable<VoteTo> actual, VoteTo... expected) {
        assertMatchVoteTo(actual, Arrays.asList(expected));
    }

    public static void assertMatchVoteTo(Iterable<VoteTo> actual, Iterable<VoteTo> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static void assertMatchVoteTo(VoteTo actual, VoteTo expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static ResultMatcher contentJsonVoteTo(List<VoteTo> expected) {
        return result -> assertMatchVoteTo(readListFromJsonMvcResult(result, VoteTo.class), expected);
    }

    public static ResultMatcher contentJsonVoteTo(VoteTo... expected) {
        return result -> assertMatchVoteTo(readListFromJsonMvcResult(result, VoteTo.class), List.of(expected));
    }

    public static ResultMatcher contentJsonVoteTo(VoteTo expected) {
        return result -> assertMatchVoteTo(readFromJsonMvcResult(result, VoteTo.class), expected);
    }

    public static ResultMatcher contentJsonVoteToArray(VoteTo expected) {
        return result -> assertMatchVoteTo(readListFromJsonMvcResult(result, VoteTo.class), expected);
    }
}
