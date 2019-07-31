package com.train4game.munoon.web.vote;

import com.train4game.munoon.model.Vote;
import com.train4game.munoon.service.VoteService;
import com.train4game.munoon.to.VoteTo;
import com.train4game.munoon.utils.JsonUtil;
import com.train4game.munoon.utils.VoteUtil;
import com.train4game.munoon.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.train4game.munoon.TestUtil.readFromJson;
import static com.train4game.munoon.data.RestaurantTestData.*;
import static com.train4game.munoon.data.UserTestData.FIRST_USER_ID;
import static com.train4game.munoon.data.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteRestController.REST_URL + "/";

    @Autowired
    private VoteService service;

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonVoteTo(VoteUtil.parseVote(FIRST_VOTE, SECOND_VOTE)));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + FIRST_VOTE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonVoteTo(VoteUtil.parseVote(FIRST_VOTE)));
    }

    @Test
    void testDelete() throws Exception {
        assumeFalse(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is after 11");

        mockMvc.perform(delete(REST_URL + FIRST_VOTE_ID))
                .andExpect(status().isNoContent());
        assertMatch(service.getAll(FIRST_USER_ID), SECOND_VOTE);
    }

    @Test
    void testUpdate() throws Exception {
        assumeFalse(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is after 11");

        VoteTo updated = new VoteTo(VoteUtil.parseVote(FIRST_VOTE));
        updated.setRestaurantId(FIRST_RESTAURANT.getId());

        mockMvc.perform(put(REST_URL + FIRST_VOTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatchVoteTo(VoteUtil.parseVote(service.get(FIRST_VOTE_ID, FIRST_USER_ID)), updated);
    }

    @Test
    void testCreate() throws Exception {
        VoteTo expected = new VoteTo(null, FIRST_RESTAURANT_ID, LocalDate.now());
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        VoteTo returned = readFromJson(actions, VoteTo.class);
        expected.setId(returned.getId());

        Vote expectedVote = VoteUtil.createVote(expected, FIRST_RESTAURANT);

        assertMatchVoteTo(returned, expected);
        assertMatch(service.getAll(FIRST_USER_ID), FIRST_VOTE, SECOND_VOTE, expectedVote);
    }

    @Test
    void twoVoteInOneDate() throws Exception {
        VoteTo firstVote = new VoteTo(null, FIRST_RESTAURANT_ID);
        VoteTo secondVote = new VoteTo(null, SECOND_RESTAURANT_ID);

        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(firstVote)))
                .andExpect(status().isCreated());

        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(secondVote)))
                .andDo(print());
//                .andExpect(status().is5xxServerError());

        Vote expectedVote = VoteUtil.createVote(firstVote, FIRST_RESTAURANT);
        assertMatch(service.getAll(FIRST_USER_ID), FIRST_VOTE, SECOND_VOTE, expectedVote);
    }
}