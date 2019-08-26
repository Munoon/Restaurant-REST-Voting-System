package com.train4game.munoon.web.controllers;

import com.train4game.munoon.View;
import com.train4game.munoon.model.Meal;
import com.train4game.munoon.service.MealService;
import com.train4game.munoon.to.MealTo;
import com.train4game.munoon.to.MealToWithRestaurant;
import com.train4game.munoon.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.train4game.munoon.utils.ParserUtil.MEAL_LIST_MAPPER;
import static com.train4game.munoon.utils.ParserUtil.MEAL_TO_LIST_MAPPER;
import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController {
    public static final String REST_URL = "/meals";
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/all/{restaurant}")
    public List<MealTo> getAll(@PathVariable int restaurant, @DateTimeFormat(iso = DATE) @RequestParam(required = false) LocalDate date) {
        return date == null ? getAll(restaurant) : getAllByDate(restaurant, date);
    }

    @GetMapping("/today/{restaurant}")
    public List<MealTo> getAllForToday(@PathVariable int restaurant) {
        return getAllByDate(restaurant, LocalDate.now());
    }

    @GetMapping("/{id}")
    public MealTo get(@PathVariable int id) {
        log.info("Get meal with id {}", id);
        return modelMapper.map(service.get(id), MealTo.class);
    }

    @GetMapping("/with/{id}")
    public MealToWithRestaurant getWithRestaurant(@PathVariable int id) {
        log.info("Get meal with restaurant and id {}", id);
        return modelMapper.map(service.getWithRestaurant(id), MealToWithRestaurant.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Delete meal with id {}, {}", id);
        service.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody MealTo meal, @PathVariable int id) {
        assureIdConsistent(meal, id);
        log.info("Update {}", meal);
        service.update(modelMapper.map(meal, Meal.class));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MealTo> createWithLocation(@Validated(View.Web.class) @RequestBody MealTo meal) {
        MealTo created = create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PostMapping(value = "/all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public List<MealTo> createAllWithLocation(@Validated(View.Web.class) @RequestBody List<MealTo> meals) {
        return createAll(meals);
    }

    private List<MealTo> getAll(int restaurantId) {
        log.info("Get all meals of restaurant {}", restaurantId);
        return modelMapper.map(service.getAll(restaurantId), MEAL_LIST_MAPPER);
    }

    private List<MealTo> getAllByDate(int restaurantId, @DateTimeFormat(iso = DATE) LocalDate date) {
        log.info("Get all meals of restaurant {} by date {}", restaurantId, date);
        return modelMapper.map(service.getAllByDate(restaurantId, date), MEAL_LIST_MAPPER);
    }

    private List<MealTo> createAll(List<MealTo> meals) {
        meals.forEach(ValidationUtils::checkNew);
        log.info("Create meals from list {}", meals);
        List<Meal> mealsList = modelMapper.map(meals, MEAL_TO_LIST_MAPPER);
        return modelMapper.map(service.create(mealsList), MEAL_LIST_MAPPER);
    }

    private MealTo create(MealTo mealTo) {
        checkNew(mealTo);
        log.info("Create {}", mealTo);
        Meal meal = modelMapper.map(mealTo, Meal.class);
        Meal created = service.create(meal);
        return modelMapper.map(created, MealTo.class);
    }
}
