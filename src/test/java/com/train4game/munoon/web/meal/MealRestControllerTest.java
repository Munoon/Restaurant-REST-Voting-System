package com.train4game.munoon.web.meal;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.repository.JpaUtil;
import com.train4game.munoon.service.MealService;
import com.train4game.munoon.service.UserService;
import com.train4game.munoon.to.MealTo;
import com.train4game.munoon.to.MealToWithRestaurant;
import com.train4game.munoon.utils.JsonUtil;
import com.train4game.munoon.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    private static final Type mapperType = new TypeToken<List<MealTo>>() {}.getType();

    private MealService service;
    private TypeMap<Meal, MealTo> toMealTo;

    @Autowired
    public MealRestControllerTest(UserService userService, ModelMapper modelMapper, JpaUtil jpaUtil, CacheManager cacheManager, WebApplicationContext webApplicationContext, MealService service) {
        super(userService, modelMapper, jpaUtil, cacheManager, webApplicationContext);
        this.service = service;
        this.toMealTo = modelMapper.getTypeMap(Meal.class, MealTo.class);
    }


    @Test
    void testGetAll() throws Exception {
        List<MealTo> expected = modelMapper.map(Arrays.asList(FIRST_MEAL, SECOND_MEAL, FOURTH_MEAL), mapperType);

        mockMvc.perform(get(REST_URL + "all/" + FIRST_RESTAURANT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(expected));
    }

    @Test
    void getAllForDate() throws Exception {
        List<MealTo> expected = Collections.singletonList(toMealTo.map(FOURTH_MEAL));

        mockMvc.perform(get(REST_URL + "all/" + FIRST_RESTAURANT_ID + "?date=" + LocalDate.of(2019, 8, 7)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(expected));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + FIRST_MEAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(toMealTo.map(FIRST_MEAL)));
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
        mockMvc.perform(delete(REST_URL + FIRST_MEAL_ID))
                .andExpect(status().isNoContent());
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), SECOND_MEAL, FOURTH_MEAL);
    }

    @Test
    void testUpdate() throws Exception {
        Meal meal = new Meal(FIRST_MEAL);
        meal.setName("New Name");
        meal.setPrice(1000);

        MealTo updated = toMealTo.map(meal);

        mockMvc.perform(put(REST_URL + FIRST_MEAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatchMealTo(toMealTo.map(service.get(FIRST_MEAL_ID)), updated);
    }

    @Test
    void testCreate() throws Exception {
        Meal meal = new Meal(null, "New Meal", FIRST_RESTAURANT, 500, LocalDate.of(2019, 8, 6));
        MealTo expected = toMealTo.map(meal);
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        MealTo returned = readFromJson(actions, MealTo.class);
        expected.setId(returned.getId());

        List<MealTo> expectedList = modelMapper.map(Arrays.asList(FIRST_MEAL, SECOND_MEAL, expected, FOURTH_MEAL), mapperType);

        assertMatchMealTo(returned, expected);
        assertMatchMealTo(modelMapper.map(service.getAll(FIRST_RESTAURANT_ID), mapperType), expectedList);
    }
}