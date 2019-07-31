package com.train4game.munoon.web.vote;

import com.train4game.munoon.model.Vote;
import com.train4game.munoon.service.VoteService;
import com.train4game.munoon.utils.JsonUtil;
import com.train4game.munoon.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.train4game.munoon.TestUtil.readFromJson;
import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT;
import static com.train4game.munoon.data.RestaurantTestData.SECOND_RESTAURANT;
import static com.train4game.munoon.data.UserTestData.FIRST_USER;
import static com.train4game.munoon.data.UserTestData.FIRST_USER_ID;
import static com.train4game.munoon.data.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.*;
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
                .andExpect(contentJson(FIRST_VOTE, SECOND_VOTE));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + FIRST_VOTE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(FIRST_VOTE));
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

        Vote updated = new Vote(FIRST_VOTE);
        updated.setRestaurant(FIRST_RESTAURANT);

        mockMvc.perform(put(REST_URL + FIRST_VOTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isCreated());

        assertMatch(service.get(FIRST_VOTE_ID, FIRST_USER_ID), updated);
    }

    @Test
    void testCreate() throws Exception {
        Vote expected = new Vote(null, FIRST_USER, SECOND_RESTAURANT, LocalDateTime.now());
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Vote returned = readFromJson(actions, Vote.class);
        expected.setId(returned.getId());

        System.out.println(returned);
        System.out.println(expected);
        assertMatch(returned, expected);
        assertMatch(service.getAll(FIRST_USER_ID), FIRST_VOTE, SECOND_VOTE, expected);
    }
}