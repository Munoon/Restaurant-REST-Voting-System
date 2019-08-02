package com.train4game.munoon.web.meal;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.model.User;
import com.train4game.munoon.service.MealService;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.to.meal.MealTo;
import com.train4game.munoon.to.meal.MealToWithRestaurant;
import com.train4game.munoon.utils.MealUtil;
import com.train4game.munoon.web.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.train4game.munoon.utils.MealUtil.*;
import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;

abstract public class AbstractMealController {
    private static final Logger log = LoggerFactory.getLogger(AbstractMealController.class);

    @Autowired
    private MealService service;

    @Autowired
    private RestaurantService restaurantService;

    private ModelMapper modelMapper = new ModelMapper();

    public MealTo get(int id) {
        log.info("Get meal with id {}", id);
        return parse(service.get(id));
    }

    public MealToWithRestaurant getWithRestaurant(int id) {
        log.info("Get meal with restaurant and id {}", id);
        return modelMapper.map(service.getWithRestaurant(id), MealToWithRestaurant.class);
    }

    public void delete(int id) {
        User user = SecurityUtil.getUser();
        log.info("Delete meal with id {}, {}", id, user);
        service.delete(id, user);
    }

    public List<MealTo> getAll(int restaurantId) {
        log.info("Get all meals of restaurant {}", restaurantId);
        return parseAll(service.getAll(restaurantId));
    }

    public MealTo create(MealTo mealTo) {
        User user = SecurityUtil.getUser();
        checkNew(mealTo);
        log.info("Create {}, {}", mealTo, user);
        Meal meal = parseToMeal(mealTo);
        return parse(service.create(meal, user));
    }

    public void update(MealTo meal, int id) {
        User user = SecurityUtil.getUser();
        assureIdConsistent(meal, id);
        log.info("Update {}, {}", meal, user);
        service.update(parseToMeal(meal), user);
    }

    private Meal parseToMeal(MealTo mealTo) {
        return MealUtil.parseToMeal(mealTo, restaurantService.get(mealTo.getRestaurant()));
    }
}
