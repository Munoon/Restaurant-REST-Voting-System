package com.train4game.munoon.web.meal;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.model.User;
import com.train4game.munoon.service.MealService;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.to.MealTo;
import com.train4game.munoon.to.MealToWithRestaurant;
import com.train4game.munoon.web.SecurityUtil;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;

abstract public class AbstractMealController {
    private static final Logger log = LoggerFactory.getLogger(AbstractMealController.class);
    private static final Type mapperType = new TypeToken<List<MealTo>>() {}.getType();

    private final MealService service;
    private final ModelMapper modelMapper;
    private final TypeMap<MealTo, Meal> parseMeal;
    private final TypeMap<Meal, MealTo> parseMealTo;
    private final TypeMap<Meal, MealToWithRestaurant> parseMealToWithRestaurant;

    public AbstractMealController(MealService service, RestaurantService restaurantService, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;

        Converter<Integer, Restaurant> intToRestaurantParser = num -> restaurantService.get(num.getSource());

        parseMeal = modelMapper.createTypeMap(MealTo.class, Meal.class)
                .addMappings(mapper -> mapper.using(intToRestaurantParser).map(MealTo::getRestaurantId, Meal::setRestaurant));

        parseMealTo = modelMapper.createTypeMap(Meal.class, MealTo.class);
        parseMealToWithRestaurant = modelMapper.createTypeMap(Meal.class, MealToWithRestaurant.class);
    }

    public MealTo get(int id) {
        log.info("Get meal with id {}", id);
        return parseMealTo.map(service.get(id));
    }

    public MealToWithRestaurant getWithRestaurant(int id) {
        log.info("Get meal with restaurant and id {}", id);
        return parseMealToWithRestaurant.map(service.getWithRestaurant(id));
    }

    public void delete(int id) {
        User user = SecurityUtil.getUser();
        log.info("Delete meal with id {}, {}", id, user);
        service.delete(id, user);
    }

    public List<MealTo> getAll(int restaurantId) {
        log.info("Get all meals of restaurant {}", restaurantId);
        return modelMapper.map(service.getAll(restaurantId), mapperType);
    }

    public List<MealTo> getAllByDate(int restaurantId, LocalDate date) {
        log.info("Get all meals of restaurant {} by date {}", restaurantId, date);
        return modelMapper.map(service.getAllByDate(restaurantId, date), mapperType);
    }

    public MealTo create(MealTo mealTo) {
        User user = SecurityUtil.getUser();
        checkNew(mealTo);
        log.info("Create {}, {}", mealTo, user);
        Meal meal = parseMeal.map(mealTo);
        return parseMealTo.map(service.create(meal, user));
    }

    public void update(MealTo meal, int id) {
        User user = SecurityUtil.getUser();
        assureIdConsistent(meal, id);
        log.info("Update {}, {}", meal, user);
        service.update(parseMeal.map(meal), user);
    }
}
