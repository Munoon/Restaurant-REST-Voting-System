package com.train4game.munoon.web.restaurant;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.utils.JsonUtil;
import com.train4game.munoon.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.train4game.munoon.TestUtil.readFromJson;
import static com.train4game.munoon.data.RestaurantTestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantRestController.REST_URL + "/";

    @Autowired
    private RestaurantService service;

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(SECOND_RESTAURANT, FIRST_RESTAURANT));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + FIRST_RESTAURANT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(FIRST_RESTAURANT));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + FIRST_RESTAURANT_ID))
                .andExpect(status().isNoContent());

        assertMatch(service.getAll(), SECOND_RESTAURANT);
    }

    @Test
    void testUpdate() throws Exception {
        Restaurant updated = new Restaurant(FIRST_RESTAURANT);
        updated.setName("Updated Name");

        mockMvc.perform(put(REST_URL + FIRST_RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(service.get(FIRST_RESTAURANT_ID), updated);
    }

    @Test
    void testCreate() throws Exception {
        Restaurant expected = new Restaurant(null, "New Restaurant");
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Restaurant returned = readFromJson(actions, Restaurant.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(service.getAll(), SECOND_RESTAURANT, FIRST_RESTAURANT, expected);
    }
}