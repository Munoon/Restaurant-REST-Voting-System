package com.train4game.munoon.service;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import com.train4game.munoon.utils.exceptions.PermissionDeniedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import static com.train4game.munoon.data.RestaurantTestData.*;
import static com.train4game.munoon.data.UserTestData.FIRST_USER;
import static com.train4game.munoon.data.UserTestData.SECOND_USER;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class RestaurantServiceTest {
    @Autowired
    private RestaurantService service;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void create() {
        Restaurant newRestaurant = new Restaurant(null, "Burger King");
        Restaurant created = service.create(newRestaurant, FIRST_USER);
        newRestaurant.setId(created.getId());
        assertMatch(service.getAll(), newRestaurant, SECOND_RESTAURANT, FIRST_RESTAURANT);
    }

    @Test
    public void createNoPermission() {
        exception.expect(PermissionDeniedException.class);
        service.create(FIRST_RESTAURANT, SECOND_USER);
    }

    @Test
    public void delete() {
        service.delete(FIRST_RESTAURANT_ID, FIRST_USER);
        assertMatch(service.getAll(), SECOND_RESTAURANT);
    }

    @Test
    public void deleteNoPermission() {
        exception.expect(PermissionDeniedException.class);
        service.delete(FIRST_RESTAURANT_ID, SECOND_USER);
    }

    @Test
    public void deleteNotFound() {
        exception.expect(NotFoundException.class);
        service.delete(9999, FIRST_USER);
    }

    @Test
    public void get() {
        assertMatch(service.get(FIRST_RESTAURANT_ID), FIRST_RESTAURANT);
    }

    @Test
    public void getNotFound() {
        exception.expect(NotFoundException.class);
        service.get(999);
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
        exception.expect(PermissionDeniedException.class);
        service.update(FIRST_RESTAURANT, SECOND_USER);
    }
}