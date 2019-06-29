package com.train4game.munoon.service;

import com.train4game.munoon.model.Roles;
import com.train4game.munoon.model.User;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.train4game.munoon.UserTestData.*;
import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class UserServiceTest {
    public final static Logger log = LoggerFactory.getLogger(UserServiceTest.class);
    public static Map<String, Long> testsStatistic = new HashMap<>();

    @Autowired
    private UserService service;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            long time = TimeUnit.NANOSECONDS.toMillis(nanos);
            log.info("Finished test {}: spent {} ms", description.getMethodName(), time);
            testsStatistic.put(description.getMethodName(), time);
        }
    };

    @AfterClass
    public static void afterClass() {
        Long totalTime = testsStatistic.values().stream().reduce((long) 0, (value, aLong) -> aLong += value);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
        long milliseconds = totalTime - seconds * 1000;
        log.info("Finished User Service testing [{} sec {} ms]", seconds, milliseconds);
        testsStatistic.forEach((testName, testTime) -> log.info("{} - {} ms", testName, testTime));
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
        assertMatch(service.getAll(), newUser, FIRST_USER, THIRD_USER, SECOND_USER);
    }

    @Test
    public void duplicateMailCreate() {
        exception.expect(DataAccessException.class);
        User newUser = new User(
                null,
                "Alex",
                FIRST_USER_EMAIL,
                "qwerty",
                LocalDateTime.now(),
                true,
                Collections.singleton(Roles.ROLE_USER
                ));
        service.create(newUser);
    }

    @Test
    public void delete() {
        service.delete(FIRST_USER_ID);
        assertMatch(service.getAll(), THIRD_USER, SECOND_USER);
    }

    @Test
    public void deletedNotFound() {
        exception.expect(NotFoundException.class);
        service.delete(9999);
    }

    @Test
    public void get() {
        User user = service.get(FIRST_USER_ID);
        assertMatch(user, FIRST_USER);
    }

    @Test
    public void getNotFound() {
        exception.expect(NotFoundException.class);
        service.get(999);
    }

    @Test
    public void getByEmail() {
        User user = service.getByEmail(FIRST_USER_EMAIL);
        assertMatch(user, FIRST_USER);
    }

    @Test
    public void getByEmailNotFound() {
        exception.expect(NotFoundException.class);
        service.getByEmail("unknownEmail@email.com");
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(), FIRST_USER, THIRD_USER, SECOND_USER);
    }

    @Test
    public void update() {
        User user = new User(FIRST_USER);
        user.setEmail("munoon@gmail.com");
        user.setRoles(Collections.singleton(Roles.ROLE_USER));
        service.update(user);
        assertMatch(service.get(FIRST_USER_ID), user);
    }
}