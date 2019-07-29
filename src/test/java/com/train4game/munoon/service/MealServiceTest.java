package com.train4game.munoon.service;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import com.train4game.munoon.utils.exceptions.PermissionDeniedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static com.train4game.munoon.data.MealTestData.*;
import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT;
import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT_ID;
import static com.train4game.munoon.data.UserTestData.FIRST_USER;
import static com.train4game.munoon.data.UserTestData.SECOND_USER;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MealServiceTest extends AbstractServiceTest {
    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal newMeal = new Meal(null, "Test meal", FIRST_RESTAURANT, 50, LocalDateTime.now());
        Meal created = service.create(newMeal, FIRST_USER);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), FIRST_MEAL, SECOND_MEAL, newMeal);
    }

    @Test
    public void createNoPermission() {
        assertThrows(PermissionDeniedException.class, () -> service.create(FIRST_MEAL, SECOND_USER));
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), FIRST_MEAL, SECOND_MEAL);
    }

    @Test
    public void delete() {
        service.delete(FIRST_MEAL_ID, FIRST_USER);
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), SECOND_MEAL);
    }

    @Test
    public void deleteNoPermission() {
        assertThrows(PermissionDeniedException.class, () -> service.delete(FIRST_MEAL_ID, SECOND_USER));
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), FIRST_MEAL, SECOND_MEAL);
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(99999, FIRST_USER));
    }

    @Test
    public void get() {
        assertMatch(service.get(FIRST_MEAL_ID), FIRST_MEAL);
    }

    @Test
    public void getWithRestaurant() {
        assertMatchWithRestaurant(service.getWithRestaurant(FIRST_MEAL_ID), FIRST_MEAL);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), FIRST_MEAL, SECOND_MEAL);
    }

    @Test
    public void update() {
        Meal meal = new Meal(FIRST_MEAL);
        meal.setPrice(999);
        service.update(meal, FIRST_USER);
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), meal, SECOND_MEAL);
    }

    @Test
    public void updateNoPermission() {
        assertThrows(PermissionDeniedException.class, () -> service.update(FIRST_MEAL, SECOND_USER));
    }

    @Test
    public void testValidation() {
        validateRootCause(() -> service.create(new Meal(null, " ", FIRST_RESTAURANT, 500, LocalDateTime.now()), FIRST_USER), ConstraintViolationException.class);
    }
}