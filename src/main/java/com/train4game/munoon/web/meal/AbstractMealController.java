package com.train4game.munoon.web.meal;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.model.User;
import com.train4game.munoon.service.MealService;
import com.train4game.munoon.web.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;

abstract public class AbstractMealController {
    private static final Logger log = LoggerFactory.getLogger(AbstractMealController.class);

    @Autowired
    private MealService service;

    public Meal get(int id) {
        log.info("Get meal with id {}", id);
        return service.get(id);
    }

    public void delete(int id) {
        User user = SecurityUtil.getUser();
        log.info("Delete meal with id {}, {}", id, user);
        service.delete(id, user);
    }

    public List<Meal> getAll(int restaurantId) {
        log.info("Get all meals of restaurant {}", restaurantId);
        return service.getAll(restaurantId);
    }

    public Meal create(Meal meal) {
        User user = SecurityUtil.getUser();
        checkNew(meal);
        log.info("Create {}, {}", meal, user);
        return service.create(meal, user);
    }

    public void update(Meal meal, int id) {
        User user = SecurityUtil.getUser();
        assureIdConsistent(meal, id);
        log.info("Update {}, {}", meal, user);
        service.update(meal, user);
    }
}
