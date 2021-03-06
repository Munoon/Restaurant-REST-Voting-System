package com.train4game.munoon.service;

import com.train4game.munoon.model.Roles;
import com.train4game.munoon.model.User;
import com.train4game.munoon.repository.JpaUtil;
import com.train4game.munoon.to.UserTo;
import com.train4game.munoon.utils.UserUtil;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;

import static com.train4game.munoon.data.UserTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest extends AbstractServiceTest  {
    @Autowired
    private UserService service;

    @BeforeEach
    void setUp() {
        cacheManager.getCache("users").clear();
        jpaUtil.clear2ndLevelCache();
    }

    @Test
    void create() {
        User newUser = new User(
                null,
                "Alex",
                "alex@gmail.com",
                "qwerty",
                new Date(),
                true,
                Collections.singleton(Roles.ROLE_USER
        ));
        User created = service.create(newUser);
        newUser.setId(created.getId());
        assertMatch(service.getAll(), newUser, FIRST_USER, SECOND_USER);
    }

    @Test
    void createWithoutRoles() {
        User newUser = new User(
                null,
                "Alex",
                "alex@gmail.com",
                "qwerty",
                new Date(),
                true,
                EnumSet.noneOf(Roles.class)
        );
        User created = service.create(newUser);
        newUser.setId(created.getId());
        assertMatch(service.getAll(), newUser, FIRST_USER, SECOND_USER);
    }

    @Test
    void duplicateMailCreate() {
        User newUser = new User(
                null,
                "Alex",
                FIRST_USER_EMAIL,
                "qwerty",
                new Date(),
                true,
                Collections.singleton(Roles.ROLE_USER
                ));
        assertThrows(DataAccessException.class, () -> service.create(newUser));
    }

    @Test
    void delete() {
        service.delete(FIRST_USER_ID);
        assertMatch(service.getAll(), SECOND_USER);
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(9999));
    }

    @Test
    void get() {
        User user = service.get(FIRST_USER_ID);
        assertMatch(user, FIRST_USER);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(999));
    }

    @Test
    void getByEmail() {
        User user = service.getByEmail(FIRST_USER_EMAIL);
        assertMatch(user, FIRST_USER);
    }

    @Test
    void getByEmailNotFound() {
        assertThrows(NotFoundException.class, () -> service.getByEmail("unknownEmail@email.com"));
    }

    @Test
    void getAll() {
        assertMatch(service.getAll(), FIRST_USER, SECOND_USER);
    }

    @Test
    void update() {
        User user = new User(FIRST_USER);
        user.setEmail("munoon@gmail.com");
        user.setRoles(Collections.singleton(Roles.ROLE_USER));
        service.update(user);
        assertMatch(service.get(FIRST_USER_ID), user);
    }

    @Test
    void updateTo() {
        User user = new User(FIRST_USER);
        UserTo updated = UserUtil.parseTo(user);
        updated.setEmail("newemail@email.com");
        updated.setName("new name");
        service.update(updated);
        assertMatch(service.get(FIRST_USER_ID), UserUtil.updateFromTo(user, updated));
    }

    @Test
    void testValidation() {
        validateRootCause(() -> service.create(new User(null, "  ", "mail@yandex.ru", "password", new Date(), true, EnumSet.of(Roles.ROLE_USER))), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "  ", "password", new Date(), true, EnumSet.of(Roles.ROLE_USER))), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "  ", new Date(), true, EnumSet.of(Roles.ROLE_USER))), ConstraintViolationException.class);
    }
}