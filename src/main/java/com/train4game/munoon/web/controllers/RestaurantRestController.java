package com.train4game.munoon.web.controllers;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.service.VoteService;
import com.train4game.munoon.to.RestaurantTo;
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
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {
    public static final String REST_URL = "/restaurant";
    private final static Logger log = LoggerFactory.getLogger(RestaurantRestController.class);
    private final static Type mapperType = new TypeToken<List<RestaurantTo>>() {}.getType();

    private final RestaurantService service;
    private final VoteService voteService;
    private final ModelMapper modelMapper;
    private final TypeMap<Restaurant, RestaurantTo> toRestaurantTo;
    private final TypeMap<RestaurantTo, Restaurant> toRestaurant;

    @Autowired
    public RestaurantRestController(RestaurantService service, VoteService voteService, ModelMapper modelMapper) {
        this.service = service;
        this.voteService = voteService;
        this.modelMapper = modelMapper;
        this.toRestaurantTo = modelMapper.createTypeMap(Restaurant.class, RestaurantTo.class);
        this.toRestaurant = modelMapper.createTypeMap(RestaurantTo.class, Restaurant.class);
    }

    @GetMapping("/all")
    public List<RestaurantTo> getAll() {
        log.info("Get all restaurants");
        return modelMapper.map(service.getAll(), mapperType);
    }

    @GetMapping
    public List<RestaurantTo> getAllWithTodayMeals(@RequestParam(required = false) LocalDate date) {
        LocalDate localDate = date == null ? LocalDate.now() : date;
        log.info("Get all restaurants by meal date {}", localDate);
        return modelMapper.map(service.getAllByMealDate(localDate), mapperType);
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("Get restaurant with id {}", id);
        return toRestaurantTo.map(service.get(id));
    }

    @GetMapping("/votes/{id}")
    public int getRestaurantVotes(@PathVariable int id) {
        return voteService.getCount(id, LocalDate.now());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Delete restaurant {}", id);
        service.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public RestaurantTo update(@RequestBody RestaurantTo restaurant, @PathVariable int id) {
        assureIdConsistent(restaurant, id);
        log.info("Update {}", restaurant);
        Restaurant created = service.update(toRestaurant.map(restaurant));
        return toRestaurantTo.map(created);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> createWithLocation(@RequestBody RestaurantTo restaurant) {
        RestaurantTo created = create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    private RestaurantTo create(RestaurantTo restaurant) {
        checkNew(restaurant);
        log.info("Create {}", restaurant);
        Restaurant created = service.create(toRestaurant.map(restaurant));
        return toRestaurantTo.map(created);
    }
}
