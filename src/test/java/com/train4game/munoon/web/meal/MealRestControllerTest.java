package com.train4game.munoon.web.meal;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.service.MealService;
import com.train4game.munoon.utils.JsonUtil;
import com.train4game.munoon.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.train4game.munoon.TestUtil.readFromJson;
import static com.train4game.munoon.data.MealTestData.*;
import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT;
import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + "/";

    @Autowired
    private MealService service;

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL + "all/" + FIRST_RESTAURANT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(FIRST_MEAL, SECOND_MEAL));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + FIRST_MEAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(FIRST_MEAL));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + FIRST_MEAL_ID))
                .andExpect(status().isNoContent());
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), SECOND_MEAL);
    }

    @Test
    void testUpdate() throws Exception {
        Meal updated = new Meal(FIRST_MEAL);
        updated.setName("New Name");
        updated.setPrice(1000);

        mockMvc.perform(put(REST_URL + FIRST_MEAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(service.get(FIRST_MEAL_ID), updated);
    }

    @Test
    void testCreate() throws Exception {
        Meal expected = new Meal(null, "New Meal", FIRST_RESTAURANT, 500, LocalDate.now());
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Meal returned = readFromJson(actions, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), FIRST_MEAL, SECOND_MEAL, expected);
    }
}