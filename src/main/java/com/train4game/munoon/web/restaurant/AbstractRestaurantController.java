package com.train4game.munoon.web.restaurant;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.model.User;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.to.RestaurantTo;
import com.train4game.munoon.web.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;

abstract public class AbstractRestaurantController {
    private final static Logger log = LoggerFactory.getLogger(AbstractRestaurantController.class);

    @Autowired
    private RestaurantService service;

    private ModelMapper modelMapper = new ModelMapper();
    private Type mapperType = new TypeToken<List<RestaurantTo>>() {}.getType();

    public RestaurantTo create(Restaurant restaurant) {
        User user = SecurityUtil.getUser();
        checkNew(restaurant);
        log.info("Create {} from user {}", restaurant, user);
        return modelMapper.map(service.create(restaurant, user), RestaurantTo.class);
    }

    public void delete(int id) {
        User user = SecurityUtil.getUser();
        log.info("Delete restaurant {} from user {}", id, user);
        service.delete(id, user);
    }

    public RestaurantTo get(int id) {
        log.info("Get restaurant with id {}", id);
        return modelMapper.map(service.get(id), RestaurantTo.class);
    }

    public List<RestaurantTo> getAll() {
        log.info("Get all restaurants");
        return modelMapper.map(service.getAll(), mapperType);
    }

    public RestaurantTo update(Restaurant restaurant, int id) {
        User user = SecurityUtil.getUser();
        assureIdConsistent(restaurant, id);
        log.info("Update {} from user {}", restaurant, user);
        return modelMapper.map(service.update(restaurant, user), RestaurantTo.class);
    }
}
