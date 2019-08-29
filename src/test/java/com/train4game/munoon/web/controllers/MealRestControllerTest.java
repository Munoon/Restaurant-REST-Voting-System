package com.train4game.munoon.web.controllers;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.repository.JpaUtil;
import com.train4game.munoon.service.MealService;
import com.train4game.munoon.service.UserService;
import com.train4game.munoon.to.MealTo;
import com.train4game.munoon.to.MealToWithRestaurant;
import com.train4game.munoon.utils.JsonUtil;
import com.train4game.munoon.utils.ParserUtil;
import com.train4game.munoon.utils.exceptions.ErrorType;
import com.train4game.munoon.web.AbstractControllerTest;
import com.train4game.munoon.web.controllers.MealRestController;
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

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.train4game.munoon.TestUtil.*;
import static com.train4game.munoon.data.MealTestData.*;
import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT;
import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT_ID;
import static com.train4game.munoon.data.UserTestData.FIRST_USER;
import static com.train4game.munoon.data.UserTestData.SECOND_USER;
import static com.train4game.munoon.utils.ParserUtil.MEAL_LIST_MAPPER;
import static com.train4game.munoon.utils.exceptions.ErrorType.VALIDATION_ERROR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + "/";

    private MealService service;

    @Autowired
    public MealRestControllerTest(UserService userService, ModelMapper modelMapper, JpaUtil jpaUtil, CacheManager cacheManager, WebApplicationContext webApplicationContext, MealService service) {
        super(userService, modelMapper, jpaUtil, cacheManager, webApplicationContext);
        this.service = service;
    }


    @Test
    void testGetAll() throws Exception {
        List<MealTo> expected = modelMapper.map(Arrays.asList(FOURTH_MEAL, FIRST_MEAL, SECOND_MEAL), MEAL_LIST_MAPPER );

        mockMvc.perform(get(REST_URL + "all/" + FIRST_RESTAURANT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(expected));
    }

    @Test
    void getAllForDate() throws Exception {
        List<MealTo> expected = Collections.singletonList(modelMapper.map(FOURTH_MEAL, MealTo.class));

        mockMvc.perform(get(REST_URL + "all/" + FIRST_RESTAURANT_ID + "?date=" + LocalDate.of(2019, 8, 7)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(expected));
    }

    @Test
    void getAllForToday() throws Exception {
        Meal meal = new Meal(null, "New Meal", FIRST_RESTAURANT, 50, LocalDate.now());
        MealTo expected = modelMapper.map(meal, MealTo.class);
        service.create(meal);

        ResultActions actions = mockMvc.perform(get(REST_URL + "today/" + FIRST_RESTAURANT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonIgnoreId(expected));
    }

    @Test
    void testGet() throws Exception {
        MealTo meal = modelMapper.map(FIRST_MEAL, MealTo.class);
        mockMvc.perform(get(REST_URL + FIRST_MEAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(meal));
    }

    @Test
    void testGetWithRestaurant() throws Exception {
        Meal meal = new Meal(FIRST_MEAL);
        meal.setRestaurant(FIRST_RESTAURANT);
        meal.getRestaurant().setMenu(FIRST_RESTAURANT_MENU);
        MealToWithRestaurant expected = modelMapper.map(FIRST_MEAL, MealToWithRestaurant.class);

        mockMvc.perform(get(REST_URL + "with/" + FIRST_MEAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(expected));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + FIRST_MEAL_ID)
                .with(userAuth(FIRST_USER)))
                .andExpect(status().isNoContent());
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), FOURTH_MEAL, SECOND_MEAL);
    }

    @Test
    void testUpdate() throws Exception {
        Meal meal = new Meal(FIRST_MEAL);
        meal.setName("New Name");
        meal.setPrice(1000);

        MealTo updated = modelMapper.map(meal, MealTo.class);

        mockMvc.perform(put(REST_URL + FIRST_MEAL_ID)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatchMealTo(modelMapper.map(service.get(FIRST_MEAL_ID), MealTo.class), updated);
    }

    @Test
    void testCreate() throws Exception {
        Meal meal = new Meal(null, "New Meal", FIRST_RESTAURANT, 500, LocalDate.of(2019, 8, 6));
        MealTo expected = modelMapper.map(meal, MealTo.class);
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        MealTo returned = readFromJson(actions, MealTo.class);
        expected.setId(returned.getId());

        List<MealTo> expectedList = modelMapper.map(Arrays.asList(FOURTH_MEAL, FIRST_MEAL, SECOND_MEAL, expected), MEAL_LIST_MAPPER);

        assertMatchMealTo(returned, expected);
        assertMatchMealTo(modelMapper.map(service.getAll(FIRST_RESTAURANT_ID), MEAL_LIST_MAPPER), expectedList);
    }

    @Test
    void testCreateAll() throws Exception {
        MealTo firstMeal = new MealTo(null, "New Meal 1", 500, FIRST_RESTAURANT_ID, LocalDate.of(2019, 8, 6));
        MealTo secondMeal = new MealTo(null, "New Meal 2", 500, FIRST_RESTAURANT_ID, LocalDate.of(2019, 8, 6));
        List<MealTo> expected = Arrays.asList(firstMeal, secondMeal);

        ResultActions actions = mockMvc.perform(post(REST_URL + "all")
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        List<MealTo> returned = readFromJsonList(actions, MealTo.class);
        firstMeal.setId(returned.get(0).getId());
        secondMeal.setId(returned.get(1).getId());

        List<MealTo> expectedList = modelMapper.map(Arrays.asList(FOURTH_MEAL, FIRST_MEAL, SECOND_MEAL, firstMeal, secondMeal), MEAL_LIST_MAPPER);

        assertMatchMealTo(returned, expected);
        assertMatchMealTo(modelMapper.map(service.getAll(FIRST_RESTAURANT_ID), MEAL_LIST_MAPPER), expectedList);
    }

    @Test
    void noPermission() throws Exception {
        mockMvc.perform(delete(REST_URL + FIRST_MEAL_ID)
                .with(userAuth(SECOND_USER)))
                .andExpect(status().isForbidden());

        assertMatch(service.getAll(FIRST_RESTAURANT_ID), FOURTH_MEAL, FIRST_MEAL, SECOND_MEAL);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createNotUniqueName() throws Exception {
        Meal meal = new Meal(null, FIRST_MEAL.getName(), FIRST_RESTAURANT, 500, FIRST_MEAL.getDate());
        MealTo expected = modelMapper.map(meal, MealTo.class);

        mockMvc.perform(post(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createAllNotUniqueName() throws Exception {
        MealTo firstMeal = new MealTo(null, "New Meal", 500, FIRST_RESTAURANT_ID, LocalDate.of(2019, 8, 6));
        MealTo secondMeal = new MealTo(null, "New Meal", 500, FIRST_RESTAURANT_ID, LocalDate.of(2019, 8, 6));

        mockMvc.perform(post(REST_URL + "all")
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(Arrays.asList(firstMeal, secondMeal))))
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateNotUniqueName() throws Exception {
        Meal meal = new Meal(FIRST_MEAL);
        meal.setName(SECOND_MEAL.getName());

        MealTo updated = modelMapper.map(meal, MealTo.class);

        mockMvc.perform(put(REST_URL + FIRST_MEAL_ID)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void createUnsafeHtml() throws Exception {
        MealTo meal = new MealTo(null, "<script>alert(123)</script>", 50, FIRST_RESTAURANT_ID, LocalDate.now());

        mockMvc.perform(post(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(meal)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateUnsafeHtml() throws Exception {
        Meal meal = new Meal(FIRST_MEAL);
        meal.setName("<script>alert(123)</script>");
        MealTo updated = modelMapper.map(meal, MealTo.class);

        mockMvc.perform(put(REST_URL + FIRST_MEAL_ID)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }
}