package com.train4game.munoon.web.controllers;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Type;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController {
    public static final String REST_URL = "/meals";
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private static final Type mapperType = new TypeToken<List<MealTo>>() {}.getType();

    private final MealService service;
    private final ModelMapper modelMapper;
    private final TypeMap<MealTo, Meal> parseMeal;
    private final TypeMap<Meal, MealTo> parseMealTo;
    private final TypeMap<Meal, MealToWithRestaurant> parseMealToWithRestaurant;

    @Autowired
    public MealRestController(MealService service, RestaurantService restaurantService, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;

        Converter<Integer, Restaurant> intToRestaurantParser = num -> restaurantService.get(num.getSource());

        parseMeal = modelMapper.createTypeMap(MealTo.class, Meal.class)
                .addMappings(mapper -> mapper.using(intToRestaurantParser).map(MealTo::getRestaurantId, Meal::setRestaurant));

        parseMealTo = modelMapper.createTypeMap(Meal.class, MealTo.class);
        parseMealToWithRestaurant = modelMapper.createTypeMap(Meal.class, MealToWithRestaurant.class);
    }

    @GetMapping("/all/{restaurant}")
    public List<MealTo> getAll(@PathVariable int restaurant, @RequestParam(required = false) LocalDate date) {
        return date == null ? getAll(restaurant) : getAllByDate(restaurant, date);
    }

    @GetMapping("/today/{restaurant}")
    public List<MealTo> getAllForToday(@PathVariable int restaurant) {
        return getAllByDate(restaurant, LocalDate.now());
    }

    @GetMapping("/{id}")
    public MealTo get(@PathVariable int id) {
        log.info("Get meal with id {}", id);
        return parseMealTo.map(service.get(id));
    }

    @GetMapping("/with/{id}")
    public MealToWithRestaurant getWithRestaurant(@PathVariable int id) {
        log.info("Get meal with restaurant and id {}", id);
        return parseMealToWithRestaurant.map(service.getWithRestaurant(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Delete meal with id {}, {}", id);
        service.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody MealTo meal, @PathVariable int id) {
        assureIdConsistent(meal, id);
        log.info("Update {}", meal);
        service.update(parseMeal.map(meal));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MealTo> createWithLocation(@RequestBody MealTo meal) {
        MealTo created = create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    private List<MealTo> getAll(int restaurantId) {
        log.info("Get all meals of restaurant {}", restaurantId);
        return modelMapper.map(service.getAll(restaurantId), mapperType);
    }

    private List<MealTo> getAllByDate(int restaurantId, LocalDate date) {
        log.info("Get all meals of restaurant {} by date {}", restaurantId, date);
        return modelMapper.map(service.getAllByDate(restaurantId, date), mapperType);
    }

    public MealTo create(MealTo mealTo) {
        checkNew(mealTo);
        log.info("Create {}", mealTo);
        Meal meal = parseMeal.map(mealTo);
        return parseMealTo.map(service.create(meal));
    }
}
