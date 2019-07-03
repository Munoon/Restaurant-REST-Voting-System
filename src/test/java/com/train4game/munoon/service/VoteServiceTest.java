package com.train4game.munoon.service;

import com.train4game.munoon.model.Vote;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import com.train4game.munoon.utils.exceptions.TimeOverException;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT;
import static com.train4game.munoon.data.RestaurantTestData.SECOND_RESTAURANT;
import static com.train4game.munoon.data.UserTestData.*;
import static com.train4game.munoon.data.VoteTestData.assertMatch;
import static com.train4game.munoon.data.VoteTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class VoteServiceTest {
    public final static Logger log = LoggerFactory.getLogger(VoteServiceTest.class);
    public static Map<String, Long> testsStatistic = new HashMap<>();

    @Autowired
    private VoteService service;

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
        log.info("Finished Vote Service testing [{} sec {} ms]", seconds, milliseconds);
        testsStatistic.forEach((testName, testTime) -> log.info("{} - {} ms", testName, testTime));
    }

    @Test
    public void create() {
        Vote newVote = new Vote(null, null, FIRST_RESTAURANT, LocalDateTime.now());
        Vote created = service.create(newVote, FIRST_USER_ID);
        newVote.setId(created.getId());
        newVote.setUser(created.getUser());
        List<Vote> all = service.getAll(FIRST_USER_ID);
        assertMatch(all, FIRST_VOTE, SECOND_VOTE, newVote);
    }

    @Test
    public void delete() {
        if (LocalTime.now().isAfter(LocalTime.of(11, 0)))
            exception.expect(TimeOverException.class);

        service.delete(FIRST_VOTE_ID, FIRST_USER_ID);
        assertMatch(service.getAll(FIRST_USER_ID), SECOND_VOTE);
    }

    @Test
    public void deleteNotOwn() {
        if (LocalTime.now().isAfter(LocalTime.of(11, 0)))
            exception.expect(TimeOverException.class);
        else
            exception.expect(NotFoundException.class);

        service.delete(FIRST_VOTE_ID, SECOND_USER.getId());
    }

    @Test
    public void get() {
        Vote vote = new Vote(FIRST_VOTE);
        vote.setUser(FIRST_USER);
        assertMatch(service.get(FIRST_VOTE_ID, FIRST_USER_ID), vote);
    }

    @Test
    public void getNotOwn() {
        exception.expect(NotFoundException.class);
        service.get(FIRST_VOTE_ID, SECOND_USER.getId());
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(FIRST_USER_ID), FIRST_VOTE, SECOND_VOTE);
    }

    @Test
    public void update() {
        if (LocalTime.now().isAfter(LocalTime.of(11, 0)))
            exception.expect(TimeOverException.class);

        Vote vote = new Vote(SECOND_VOTE);
        vote.setRestaurant(SECOND_RESTAURANT);
        service.update(vote, FIRST_USER_ID);
        assertMatch(service.getAll(FIRST_USER_ID), FIRST_VOTE, vote);
    }

    @Test
    public void updateNotOwn() {
        if (LocalTime.now().isAfter(LocalTime.of(11, 0)))
            exception.expect(TimeOverException.class);
        else
            exception.expect(NotFoundException.class);

        service.update(FIRST_VOTE, SECOND_USER.getId());
    }

    @Test
    public void updateTimeOver() {
        exception.expect(TimeOverException.class);
        service.update(FIRST_VOTE, FIRST_USER_ID);
    }
}