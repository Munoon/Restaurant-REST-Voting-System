package com.train4game.munoon.service;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.repository.JpaUtil;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import javax.validation.ConstraintViolationException;

import java.time.LocalDate;
import java.util.Collections;

import static com.train4game.munoon.data.MealTestData.FOURTH_MEAL;
import static com.train4game.munoon.data.RestaurantTestData.*;
import static com.train4game.munoon.data.UserTestData.FIRST_USER;
import static com.train4game.munoon.data.UserTestData.SECOND_USER;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RestaurantServiceTest extends AbstractServiceTest  {
    @Autowired
    private RestaurantService service;

    @BeforeEach
    void setUp() {
        cacheManager.getCache("meals").clear();
        jpaUtil.clear2ndLevelCache();
    }

    @Test
    void create() {
        Restaurant newRestaurant = new Restaurant(null, "Burger King");
        Restaurant created = service.create(newRestaurant);
        newRestaurant.setId(created.getId());
        assertMatch(service.getAll(), newRestaurant, SECOND_RESTAURANT, FIRST_RESTAURANT);
    }

    @Test
    void delete() {
        service.delete(FIRST_RESTAURANT_ID);
        assertMatch(service.getAll(), SECOND_RESTAURANT);
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(9999));
    }

    @Test
    void get() {
        assertMatch(service.get(FIRST_RESTAURANT_ID), FIRST_RESTAURANT);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(999));
    }

    @Test
    void getAll() {
        assertMatch(service.getAll(), SECOND_RESTAURANT, FIRST_RESTAURANT);
    }

    @Test
    void getAllByMealDate() {
        Restaurant expected = new Restaurant(FIRST_RESTAURANT);
        expected.setMenu(Collections.singletonList(FOURTH_MEAL));
        assertMatch(service.getAllByMealDate(LocalDate.of(2019, 8, 7)), expected);
    }

    @Test
    void update() {
        Restaurant restaurant = new Restaurant(FIRST_RESTAURANT);
        restaurant.setName("Another Restaurant");
        service.update(restaurant);
        assertMatch(service.getAll(), restaurant, SECOND_RESTAURANT);
    }

    @Test
    void testValidation() {
        validateRootCause(() -> service.create(new Restaurant(null, "  ")), ConstraintViolationException.class);
    }
}