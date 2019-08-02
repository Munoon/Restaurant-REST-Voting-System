package com.train4game.munoon.web.meal;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.model.User;
import com.train4game.munoon.service.MealService;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.to.meal.MealTo;
import com.train4game.munoon.to.meal.MealToWithRestaurant;
import com.train4game.munoon.web.SecurityUtil;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;

abstract public class AbstractMealController {
    private static final Logger log = LoggerFactory.getLogger(AbstractMealController.class);
    private static final Type mapperType = new TypeToken<List<MealTo>>() {}.getType();

    private MealService service;
    private TypeMap<MealTo, Meal> typeMap;
    private ModelMapper modelMapper;

    public AbstractMealController(MealService service, RestaurantService restaurantService, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;

        Converter<Integer, Restaurant> converter = num -> restaurantService.get(num.getSource());
        typeMap = modelMapper.createTypeMap(MealTo.class, Meal.class)
                .addMappings(mapper -> mapper.using(converter).map(MealTo::getRestaurantId, Meal::setRestaurant));
    }

    public MealTo get(int id) {
        log.info("Get meal with id {}", id);
        return modelMapper.map(service.get(id), MealTo.class);
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
        return modelMapper.map(service.getAll(restaurantId), mapperType);
    }

    public MealTo create(MealTo mealTo) {
        User user = SecurityUtil.getUser();
        checkNew(mealTo);
        log.info("Create {}, {}", mealTo, user);
        Meal meal = typeMap.map(mealTo);
        return modelMapper.map(service.create(meal, user), MealTo.class);
    }

    public void update(MealTo meal, int id) {
        User user = SecurityUtil.getUser();
        assureIdConsistent(meal, id);
        log.info("Update {}, {}", meal, user);
        service.update(typeMap.map(meal), user);
    }
}
