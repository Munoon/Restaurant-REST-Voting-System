package com.train4game.munoon.service;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

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
    void create() {
        Meal newMeal = new Meal(null, "Test meal", FIRST_RESTAURANT, 50, LocalDate.of(2019, 8, 6));
        Meal created = service.create(newMeal);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), FIRST_MEAL, SECOND_MEAL, newMeal, FOURTH_MEAL);
    }

    @Test
    void delete() {
        service.delete(FIRST_MEAL_ID);
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), SECOND_MEAL, FOURTH_MEAL);
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(99999));
    }

    @Test
    void get() {
        assertMatch(service.get(FIRST_MEAL_ID), FIRST_MEAL);
    }

    @Test
    void getWithRestaurant() {
        Meal expected = new Meal(FIRST_MEAL);
        expected.getRestaurant().setMenu(FIRST_RESTAURANT_MENU);
        assertMatchWithRestaurant(service.getWithRestaurant(FIRST_MEAL_ID), expected);
    }

    @Test
    void getAll() {
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), FIRST_MEAL, SECOND_MEAL, FOURTH_MEAL);
    }

    @Test
    void getAllByDate() {
        assertMatch(service.getAllByDate(FIRST_RESTAURANT_ID, LocalDate.of(2019, 8, 7)), FOURTH_MEAL);
    }

    @Test
    void update() {
        Meal meal = new Meal(FIRST_MEAL);
        meal.setPrice(999);
        service.update(meal);
        assertMatch(service.getAll(FIRST_RESTAURANT_ID), SECOND_MEAL, meal, FOURTH_MEAL);
    }

    @Test
    void testValidation() {
        validateRootCause(() -> service.create(new Meal(null, " ", FIRST_RESTAURANT, 500, LocalDate.now())), ConstraintViolationException.class);
    }
}