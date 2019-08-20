package com.train4game.munoon.web.controllers;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.repository.JpaUtil;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.service.UserService;
import com.train4game.munoon.service.VoteService;
import com.train4game.munoon.to.MealTo;
import com.train4game.munoon.to.RestaurantTo;
import com.train4game.munoon.to.RestaurantToWithVotes;
import com.train4game.munoon.utils.JsonUtil;
import com.train4game.munoon.web.AbstractControllerTest;
import com.train4game.munoon.web.controllers.RestaurantRestController;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.train4game.munoon.TestUtil.readFromJson;
import static com.train4game.munoon.TestUtil.userAuth;
import static com.train4game.munoon.data.MealTestData.FIRST_RESTAURANT_MENU;
import static com.train4game.munoon.data.MealTestData.FOURTH_MEAL;
import static com.train4game.munoon.data.RestaurantTestData.*;
import static com.train4game.munoon.data.UserTestData.FIRST_USER;
import static com.train4game.munoon.data.UserTestData.SECOND_USER;
import static com.train4game.munoon.utils.ParserUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantRestController.REST_URL + "/";

    private RestaurantService service;
    private VoteService voteService;

    @Autowired
    public RestaurantRestControllerTest(UserService userService, ModelMapper modelMapper, JpaUtil jpaUtil, CacheManager cacheManager, WebApplicationContext webApplicationContext, RestaurantService service, VoteService voteService) {
        super(userService, modelMapper, jpaUtil, cacheManager, webApplicationContext);
        this.service = service;
        this.voteService = voteService;
    }

    @Test
    void testGetAll() throws Exception {
        List<RestaurantTo> expected = modelMapper.map(Arrays.asList(SECOND_RESTAURANT, FIRST_RESTAURANT), RESTAURANT_LIST_MAPPER);
        mockMvc.perform(get(REST_URL + "all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(expected));
    }

    @Test
    void testGetAllByDate() throws Exception {
        LocalDate date = LocalDate.of(2019, 8, 7);
        Restaurant expected = new Restaurant(FIRST_RESTAURANT);
        expected.setMenu(Collections.singletonList(FOURTH_MEAL));
        List<RestaurantToWithVotes> expectedList = parseWithVotes(Collections.singletonList(expected), date);

        mockMvc.perform(get(REST_URL + "?date=" + date))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonWithVotes(expectedList));
    }

    @Test
    void testGet() throws Exception {
        Restaurant expected = new Restaurant(FIRST_RESTAURANT);
        expected.setMenu(FIRST_RESTAURANT_MENU);

        mockMvc.perform(get(REST_URL + FIRST_RESTAURANT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(parseWithVotes(expected, LocalDate.now())));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + FIRST_RESTAURANT_ID)
                .with(userAuth(FIRST_USER)))
                .andExpect(status().isNoContent());

        assertMatch(service.getAll(), SECOND_RESTAURANT);
    }

    @Test
    void testUpdate() throws Exception {
        Restaurant updated = new Restaurant(FIRST_RESTAURANT);
        updated.setName("Updated Name");

        mockMvc.perform(put(REST_URL + FIRST_RESTAURANT_ID)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(service.get(FIRST_RESTAURANT_ID), updated);
    }

    @Test
    void testCreate() throws Exception {
        Restaurant expected = new Restaurant(null, "New Restaurant");
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Restaurant returned = readFromJson(actions, Restaurant.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(service.getAll(), SECOND_RESTAURANT, FIRST_RESTAURANT, expected);
    }

    @Test
    void noPermission() throws Exception {
        mockMvc.perform(delete(REST_URL + FIRST_RESTAURANT_ID)
                .with(userAuth(SECOND_USER)))
                .andExpect(status().isUnauthorized());

        assertMatch(service.getAll(), SECOND_RESTAURANT, FIRST_RESTAURANT);
    }

    // пришлось скопировать из контроллера
    // не нащел способ это вынести куда-то
    private List<RestaurantToWithVotes> parseWithVotes(List<Restaurant> restaurants, LocalDate date) {
        List<RestaurantToWithVotes> result = new ArrayList<>();
        restaurants.forEach(restaurant -> result.add(parseWithVotes(restaurant, date)));
        return result;
    }

    private RestaurantToWithVotes parseWithVotes(Restaurant restaurant, LocalDate date) {
        List<MealTo> menu = modelMapper.map(restaurant.getMenu(), MEAL_LIST_MAPPER);
        int votes = voteService.getCount(restaurant.getId(), date);
        return parseRestaurantWithVotes(restaurant, menu, votes);
    }
}