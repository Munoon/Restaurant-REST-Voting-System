package com.train4game.munoon.service;

import com.train4game.munoon.model.Vote;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import com.train4game.munoon.utils.exceptions.TimeOverException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT;
import static com.train4game.munoon.data.RestaurantTestData.SECOND_RESTAURANT;
import static com.train4game.munoon.data.UserTestData.*;
import static com.train4game.munoon.data.VoteTestData.assertMatch;
import static com.train4game.munoon.data.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class VoteServiceTest extends AbstractServiceTest  {
    @Autowired
    private VoteService service;

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
        assumeFalse(LocalTime.now().isAfter(LocalTime.of(11, 0)), "IT is after 11");
        service.delete(FIRST_VOTE_ID, FIRST_USER_ID);
        assertMatch(service.getAll(FIRST_USER_ID), SECOND_VOTE);
    }

    @Test
    void deleteTimeOver() {
        assumeTrue(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is before 11");
        assertThrows(TimeOverException.class, () -> service.delete(FIRST_VOTE_ID, FIRST_USER_ID));
    }

    @Test
    public void deleteNotOwn() {
        assumeFalse(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is after 11");
        assertThrows(NotFoundException.class, () -> service.delete(FIRST_VOTE_ID, SECOND_USER.getId()));
    }

    @Test
    public void get() {
        Vote vote = new Vote(FIRST_VOTE);
        vote.setUser(FIRST_USER);
        assertMatch(service.get(FIRST_VOTE_ID, FIRST_USER_ID), vote);
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(FIRST_VOTE_ID, SECOND_USER.getId()));
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(FIRST_USER_ID), FIRST_VOTE, SECOND_VOTE);
    }

    @Test
    public void update() {
        assumeFalse(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is after 11");

        Vote vote = new Vote(SECOND_VOTE);
        vote.setRestaurant(SECOND_RESTAURANT);

        service.update(vote, FIRST_USER_ID);
        assertMatch(service.getAll(FIRST_USER_ID), FIRST_VOTE, vote);
    }

    @Test
    public void updateNotOwn() {
        assumeFalse(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is after 11");
        assertThrows(NotFoundException.class, () -> service.update(FIRST_VOTE, SECOND_USER.getId()));
    }

    @Test
    public void updateTimeOver() {
        assumeTrue(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is before 11");
        assertThrows(TimeOverException.class, () -> service.update(FIRST_VOTE, FIRST_USER_ID));
    }
}