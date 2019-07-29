package com.train4game.munoon.service;

import com.train4game.munoon.model.Roles;
import com.train4game.munoon.model.User;
import com.train4game.munoon.repository.JpaUtil;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumSet;

import static com.train4game.munoon.data.UserTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest extends AbstractServiceTest  {
    @Autowired
    private UserService service;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private JpaUtil jpaUtil;

    @BeforeEach
    public void setUp() {
        cacheManager.getCache("users").clear();
        jpaUtil.clear2ndLevelCache();
    }

    @Test
    public void create() {
        User newUser = new User(
                null,
                "Alex",
                "alex@gmail.com",
                "qwerty",
                LocalDateTime.now(),
                true,
                Collections.singleton(Roles.ROLE_USER
        ));
        User created = service.create(newUser);
        newUser.setId(created.getId());
        assertMatch(service.getAll(), newUser, THIRD_USER, SECOND_USER, FIRST_USER);
    }

    @Test
    public void createWithoutRoles() {
        User newUser = new User(
                null,
                "Alex",
                "alex@gmail.com",
                "qwerty",
                LocalDateTime.now(),
                true,
                EnumSet.noneOf(Roles.class)
        );
        User created = service.create(newUser);
        newUser.setId(created.getId());
        assertMatch(service.getAll(), newUser, THIRD_USER, SECOND_USER, FIRST_USER);
    }

    @Test
    public void duplicateMailCreate() {
        User newUser = new User(
                null,
                "Alex",
                FIRST_USER_EMAIL,
                "qwerty",
                LocalDateTime.now(),
                true,
                Collections.singleton(Roles.ROLE_USER
                ));
        assertThrows(DataAccessException.class, () -> service.create(newUser));
    }

    @Test
    public void delete() {
        service.delete(FIRST_USER_ID);
        assertMatch(service.getAll(), THIRD_USER, SECOND_USER);
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(9999));
    }

    @Test
    public void get() {
        User user = service.get(FIRST_USER_ID);
        assertMatch(user, FIRST_USER);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(999));
    }

    @Test
    public void getByEmail() {
        User user = service.getByEmail(FIRST_USER_EMAIL);
        assertMatch(user, FIRST_USER);
    }

    @Test
    public void getByEmailNotFound() {
        assertThrows(NotFoundException.class, () -> service.getByEmail("unknownEmail@email.com"));
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(), THIRD_USER, SECOND_USER, FIRST_USER);
    }

    @Test
    public void update() {
        User user = new User(FIRST_USER);
        user.setEmail("munoon@gmail.com");
        user.setRoles(Collections.singleton(Roles.ROLE_USER));
        service.update(user);
        assertMatch(service.get(FIRST_USER_ID), user);
    }

    @Test
    public void testValidation() {
        validateRootCause(() -> service.create(new User(null, "  ", "mail@yandex.ru", "password", LocalDateTime.now(), true, EnumSet.of(Roles.ROLE_USER))), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "  ", "password", LocalDateTime.now(), true, EnumSet.of(Roles.ROLE_USER))), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "  ", LocalDateTime.now(), true, EnumSet.of(Roles.ROLE_USER))), ConstraintViolationException.class);
    }
}