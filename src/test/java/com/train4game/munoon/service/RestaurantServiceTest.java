package com.train4game.munoon.service;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.repository.JpaUtil;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import com.train4game.munoon.utils.exceptions.PermissionDeniedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import javax.validation.ConstraintViolationException;

import static com.train4game.munoon.data.RestaurantTestData.*;
import static com.train4game.munoon.data.UserTestData.FIRST_USER;
import static com.train4game.munoon.data.UserTestData.SECOND_USER;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RestaurantServiceTest extends AbstractServiceTest  {
    @Autowired
    private RestaurantService service;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private JpaUtil jpaUtil;

    @BeforeEach
    public void setUp() {
        cacheManager.getCache("restaurants").clear();
        jpaUtil.clear2ndLevelCache();
    }

    @Test
    public void create() {
        Restaurant newRestaurant = new Restaurant(null, "Burger King");
        Restaurant created = service.create(newRestaurant, FIRST_USER);
        newRestaurant.setId(created.getId());
        assertMatch(service.getAll(), newRestaurant, SECOND_RESTAURANT, FIRST_RESTAURANT);
    }

    @Test
    public void createNoPermission() {
        assertThrows(PermissionDeniedException.class, () -> service.create(FIRST_RESTAURANT, SECOND_USER));
    }

    @Test
    public void delete() {
        service.delete(FIRST_RESTAURANT_ID, FIRST_USER);
        assertMatch(service.getAll(), SECOND_RESTAURANT);
    }

    @Test
    public void deleteNoPermission() {
        assertThrows(PermissionDeniedException.class, () -> service.delete(FIRST_RESTAURANT_ID, SECOND_USER));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(9999, FIRST_USER));
    }

    @Test
    public void get() {
        assertMatch(service.get(FIRST_RESTAURANT_ID), FIRST_RESTAURANT);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(999));
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(), SECOND_RESTAURANT, FIRST_RESTAURANT);
    }

    @Test
    public void update() {
        Restaurant restaurant = new Restaurant(FIRST_RESTAURANT);
        restaurant.setName("Another Restaurant");
        service.update(restaurant, FIRST_USER);
        assertMatch(service.getAll(), restaurant, SECOND_RESTAURANT);
    }

    @Test
    public void updateNoPermission() {
        assertThrows(PermissionDeniedException.class, () -> service.update(FIRST_RESTAURANT, SECOND_USER));
    }

    @Test
    public void testValidation() {
        validateRootCause(() -> service.create(new Restaurant(null, "  "), FIRST_USER), ConstraintViolationException.class);
    }
}