package com.train4game.munoon.web.restaurant;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.repository.JpaUtil;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.service.UserService;
import com.train4game.munoon.to.RestaurantTo;
import com.train4game.munoon.utils.JsonUtil;
import com.train4game.munoon.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.train4game.munoon.TestUtil.readFromJson;
import static com.train4game.munoon.data.MealTestData.FOURTH_MEAL;
import static com.train4game.munoon.data.RestaurantTestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantRestController.REST_URL + "/";

    private RestaurantService service;

    @Autowired
    public RestaurantRestControllerTest(UserService userService, ModelMapper modelMapper, JpaUtil jpaUtil, CacheManager cacheManager, WebApplicationContext webApplicationContext, RestaurantService service) {
        super(userService, modelMapper, jpaUtil, cacheManager, webApplicationContext);
        this.service = service;
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL + "all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(SECOND_RESTAURANT, FIRST_RESTAURANT));
    }

    @Test
    void testGetAllForDate() throws Exception {
        Restaurant expected = new Restaurant(FIRST_RESTAURANT);
        expected.setMenu(Collections.singletonList(FOURTH_MEAL));

        mockMvc.perform(get(REST_URL + "?date=" + LocalDate.of(2019, 8, 7)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonWithMenu(modelMapper.map(expected, RestaurantTo.class)));
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