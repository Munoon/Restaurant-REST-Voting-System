package com.train4game.munoon.web.restaurant;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.model.User;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.web.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;

abstract public class AbstractRestaurantController {
    private final static Logger log = LoggerFactory.getLogger(AbstractRestaurantController.class);

    @Autowired
    private RestaurantService service;

    public Restaurant create(Restaurant restaurant) {
        User user = SecurityUtil.getUser();
        checkNew(restaurant);
        log.info("Create {} from user {}", restaurant, user);
        return service.create(restaurant, user);
    }

    public void delete(int id) {
        User user = SecurityUtil.getUser();
        log.info("Delete restaurant {} from user {}", id, user);
        service.delete(id, user);
    }

    public Restaurant get(int id) {
        log.info("Get restaurant with id {}", id);
        return service.get(id);
    }

    public List<Restaurant> getAll() {
        log.info("Get all restaurants");
        return service.getAll();
    }

    public Restaurant update(Restaurant restaurant, int id) {
        User user = SecurityUtil.getUser();
        assureIdConsistent(restaurant, id);
        log.info("Update {} from user {}", restaurant, user);
        return service.update(restaurant, user);
    }
}
