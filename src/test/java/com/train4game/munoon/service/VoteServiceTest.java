package com.train4game.munoon.service;

import com.train4game.munoon.model.Vote;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import com.train4game.munoon.utils.exceptions.VoteNotAllowedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.train4game.munoon.data.RestaurantTestData.*;
import static com.train4game.munoon.data.UserTestData.*;
import static com.train4game.munoon.data.VoteTestData.assertMatch;
import static com.train4game.munoon.data.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class VoteServiceTest extends AbstractServiceTest  {
    @Autowired
    private VoteService service;

    @Test
    void create() {
        Vote newVote = new Vote(null, null, FIRST_RESTAURANT, LocalDate.now());
        Vote created = service.create(newVote, FIRST_USER_ID);
        newVote.setId(created.getId());
        newVote.setUser(created.getUser());
        List<Vote> all = service.getAll(FIRST_USER_ID);
        assertMatch(all, newVote, SECOND_VOTE, FIRST_VOTE);
    }

    @Test
    void delete() {
        assumeFalse(LocalTime.now().isAfter(LocalTime.of(11, 0)), "IT is after 11");
        service.delete(FIRST_VOTE_ID, FIRST_USER_ID);
        assertMatch(service.getAll(FIRST_USER_ID), SECOND_VOTE);
    }

    @Test
    void deleteTimeOver() {
        assumeTrue(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is before 11");
        assertThrows(VoteNotAllowedException.class, () -> service.delete(FIRST_VOTE_ID, FIRST_USER_ID));
    }

    @Test
    void deleteNotOwn() {
        assumeFalse(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is after 11");
        assertThrows(NotFoundException.class, () -> service.delete(FIRST_VOTE_ID, SECOND_USER.getId()));
    }

    @Test
    void get() {
        Vote vote = new Vote(FIRST_VOTE);
        vote.setUser(FIRST_USER);
        assertMatch(service.get(FIRST_VOTE_ID, FIRST_USER_ID), vote);
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(FIRST_VOTE_ID, SECOND_USER.getId()));
    }

    @Test
    void getAllByUserId() {
        assertMatch(service.getAll(FIRST_USER_ID), SECOND_VOTE, FIRST_VOTE);
    }

    @Test
    void getAll() {
        assertMatch(service.getAll(), SECOND_VOTE, FIRST_VOTE, THIRD_VOTE);
    }

    @Test
    void getAllByUserIdAndDate() {
        assertMatch(service.getAllByDate(FIRST_USER_ID, FIRST_VOTE.getDate()), FIRST_VOTE);
    }

    @Test
    void update() {
        assumeFalse(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is after 11");

        Vote vote = new Vote(SECOND_VOTE);
        vote.setRestaurant(SECOND_RESTAURANT);

        service.update(vote, FIRST_USER_ID);
        assertMatch(service.getAll(FIRST_USER_ID), vote, FIRST_VOTE);
    }

    @Test
    void updateNotOwn() {
        assumeFalse(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is after 11");
        assertThrows(NotFoundException.class, () -> service.update(FIRST_VOTE, SECOND_USER.getId()));
    }

    @Test
    void updateTimeOver() {
        assumeTrue(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is before 11");
        assertThrows(VoteNotAllowedException.class, () -> service.update(FIRST_VOTE, FIRST_USER_ID));
    }

    @Test
    void twoVoteInOneDay() {
        LocalDate date = LocalDate.of(2019, 8, 11);
        Vote firstVote = new Vote(null, FIRST_USER, FIRST_RESTAURANT, date);
        Vote secondVote = new Vote(null, FIRST_USER, FIRST_RESTAURANT, date);

        service.create(firstVote, FIRST_USER_ID);
        assertThrows(DataIntegrityViolationException.class, () -> service.create(secondVote, FIRST_USER_ID));

        assertMatch(service.getAll(FIRST_USER_ID), firstVote, SECOND_VOTE, FIRST_VOTE);
    }
}