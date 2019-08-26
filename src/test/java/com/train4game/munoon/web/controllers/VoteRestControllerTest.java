package com.train4game.munoon.web.controllers;

import com.train4game.munoon.model.Vote;
import com.train4game.munoon.repository.JpaUtil;
import com.train4game.munoon.service.UserService;
import com.train4game.munoon.service.VoteService;
import com.train4game.munoon.to.VoteTo;
import com.train4game.munoon.utils.JsonUtil;
import com.train4game.munoon.utils.ParserUtil;
import com.train4game.munoon.web.AbstractControllerTest;
import com.train4game.munoon.web.controllers.VoteRestController;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static com.train4game.munoon.TestUtil.readFromJson;
import static com.train4game.munoon.TestUtil.userAuth;
import static com.train4game.munoon.data.RestaurantTestData.*;
import static com.train4game.munoon.data.UserTestData.FIRST_USER;
import static com.train4game.munoon.data.UserTestData.FIRST_USER_ID;
import static com.train4game.munoon.data.VoteTestData.assertMatch;
import static com.train4game.munoon.data.VoteTestData.*;
import static com.train4game.munoon.utils.ParserUtil.VOTE_LIST_MAPPER;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteRestController.REST_URL + "/";

    private VoteService service;

    @Autowired
    public VoteRestControllerTest(UserService userService, ModelMapper modelMapper, JpaUtil jpaUtil, CacheManager cacheManager, WebApplicationContext webApplicationContext, VoteService service) {
        super(userService, modelMapper, jpaUtil, cacheManager, webApplicationContext);
        this.service = service;
    }

    @Test
    void testGetAll() throws Exception {
        List<VoteTo> expected = modelMapper.map(Arrays.asList(FIRST_VOTE, SECOND_VOTE), VOTE_LIST_MAPPER);

        mockMvc.perform(get(REST_URL)
                .with(userAuth(FIRST_USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonVoteTo(expected));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + FIRST_VOTE_ID)
                .with(userAuth(FIRST_USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonVoteTo(modelMapper.map(FIRST_VOTE, VoteTo.class)));
    }

    @Test
    void testDelete() throws Exception {
        assumeFalse(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is after 11");

        mockMvc.perform(delete(REST_URL + FIRST_VOTE_ID)
                .with(userAuth(FIRST_USER)))
                .andExpect(status().isNoContent());
        assertMatch(service.getAll(FIRST_USER_ID), SECOND_VOTE);
    }

    @Test
    void testUpdate() throws Exception {
        assumeFalse(LocalTime.now().isAfter(LocalTime.of(11, 0)), "It is after 11");

        VoteTo updated = modelMapper.map(FIRST_VOTE, VoteTo.class);
        updated.setRestaurantId(FIRST_RESTAURANT.getId());

        mockMvc.perform(put(REST_URL + FIRST_VOTE_ID)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        VoteTo expected = modelMapper.map(service.get(FIRST_VOTE_ID, FIRST_USER_ID), VoteTo.class);
        assertMatchVoteTo(expected, updated);
    }

    @Test
    void testCreate() throws Exception {
        VoteTo expected = new VoteTo(null, FIRST_RESTAURANT_ID);
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        VoteTo returned = readFromJson(actions, VoteTo.class);
        expected.setId(returned.getId());
        expected.setUserId(returned.getUserId());

        Vote expectedVote = modelMapper.map(expected, Vote.class);
        expectedVote.setRestaurant(FIRST_RESTAURANT);

        assertMatchVoteTo(returned, expected);
        assertMatch(service.getAll(FIRST_USER_ID), FIRST_VOTE, SECOND_VOTE, expectedVote);
    }

    @Test
    void updateTimeOver() throws Exception {
        assumeFalse(LocalTime.now().isBefore(LocalTime.of(11, 0)), "It is before 11");

        VoteTo updated = modelMapper.map(FIRST_VOTE, VoteTo.class);
        updated.setRestaurantId(FIRST_RESTAURANT.getId());

        mockMvc.perform(put(REST_URL + FIRST_VOTE_ID)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().is5xxServerError());

        assertMatch(service.get(FIRST_VOTE_ID, FIRST_USER_ID), FIRST_VOTE);
    }

    @Test
    void deleteTimeOver() throws Exception {
        assumeFalse(LocalTime.now().isBefore(LocalTime.of(11, 0)), "It is before 11");

        mockMvc.perform(delete(REST_URL + FIRST_VOTE_ID)
                .with(userAuth(FIRST_USER)))
                .andExpect(status().is5xxServerError());

        assertMatch(service.getAll(FIRST_USER_ID), FIRST_VOTE, SECOND_VOTE);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void votedTwiceForToday() throws Exception {
        VoteTo firstVote = new VoteTo(null, FIRST_RESTAURANT_ID);
        VoteTo secondVote = new VoteTo(null, SECOND_RESTAURANT_ID);

        ResultActions actions = mockMvc.perform(post(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(firstVote)))
                .andExpect(status().isCreated());

        mockMvc.perform(post(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(secondVote)))
                .andExpect(status().isConflict());

        VoteTo returned = readFromJson(actions, VoteTo.class);
        firstVote.setId(returned.getId());
        firstVote.setUserId(returned.getUserId());

        Vote expectedVote = modelMapper.map(firstVote, Vote.class);
        expectedVote.setRestaurant(FIRST_RESTAURANT);

        assertMatchVoteTo(returned, firstVote);
        assertMatch(service.getAll(FIRST_USER_ID), FIRST_VOTE, SECOND_VOTE, expectedVote);
    }
}