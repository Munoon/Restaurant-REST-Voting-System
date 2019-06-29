package com.train4game.munoon;

import com.train4game.munoon.model.Roles;
import com.train4game.munoon.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTestData {
    public static final int FIRST_USER_ID = 100;
    public static final String FIRST_USER_EMAIL = "munoon@yandex.ru";
    public static final User FIRST_USER = new User(FIRST_USER_ID, "Nikita", "munoon@yandex.ru", "easyPass", LocalDateTime.now(), true, Collections.singleton(Roles.ROLE_ADMIN));
    public static final User SECOND_USER = new User(FIRST_USER_ID + 1, "Vasya", "vasya@gmail.com", "VasyaTheBest", LocalDateTime.now(), true, Collections.singleton(Roles.ROLE_USER));
    public static final User THIRD_USER = new User(FIRST_USER_ID + 2, "Petr", "petr@gmail.com", "PertHasStrongPass123", LocalDateTime.now(), true, Collections.singleton(Roles.ROLE_USER));

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "roles");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }
}
