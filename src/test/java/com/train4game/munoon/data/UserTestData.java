package com.train4game.munoon.data;

import com.train4game.munoon.model.Roles;
import com.train4game.munoon.model.User;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.train4game.munoon.TestUtil.readFromJsonMvcResult;
import static com.train4game.munoon.TestUtil.readListFromJsonMvcResult;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTestData {
    public static final int FIRST_USER_ID = 100;
    public static final String FIRST_USER_EMAIL = "admin@gmail.com";
    public static final User FIRST_USER = new User(FIRST_USER_ID, "Nikita", "admin@gmail.com", "easyPass", LocalDateTime.now(), true, Collections.singleton(Roles.ROLE_ADMIN));
    public static final User SECOND_USER = new User(FIRST_USER_ID + 1, "Vasya", "vasya@gmail.com", "VasyaTheBest", LocalDateTime.now(), true, Collections.singleton(Roles.ROLE_USER));
    public static final User THIRD_USER = new User(FIRST_USER_ID + 2, "Petr", "petr@gmail.com", "PertHasStrongPass123", LocalDateTime.now(), true, Collections.singleton(Roles.ROLE_USER));

    private UserTestData() {
    }

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(User... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, User.class), List.of(expected));
    }

    public static ResultMatcher contentJson(User expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, User.class), expected);
    }
}
