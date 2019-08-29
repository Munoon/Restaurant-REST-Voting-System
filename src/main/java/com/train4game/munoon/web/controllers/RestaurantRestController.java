package com.train4game.munoon.web.controllers;

import com.train4game.munoon.View;
import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.to.RestaurantTo;
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

import static com.train4game.munoon.utils.ParserUtil.*;
import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {
    public static final String REST_URL = "/restaurant";
    private final static Logger log = LoggerFactory.getLogger(RestaurantRestController.class);

    @Autowired
    private RestaurantService service;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/all")
    public List<RestaurantTo> getAll() {
        log.info("Get all restaurants");
        return modelMapper.map(service.getAll(), RESTAURANT_LIST_MAPPER);
    }

    @GetMapping
    public List<RestaurantTo> getAllByDate(@DateTimeFormat(iso = DATE) @RequestParam(required = false) LocalDate date) {
        LocalDate localDate = date == null ? LocalDate.now() : date;
        log.info("Get all restaurants by meal date {}", localDate);
        return modelMapper.map(service.getAllByMealDate(localDate), RESTAURANT_LIST_MAPPER);
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("Get restaurant with id {}", id);
        return modelMapper.map(service.get(id), RestaurantTo.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Delete restaurant {}", id);
        service.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public RestaurantTo update(@Validated(View.Web.class) @RequestBody RestaurantTo restaurant, @PathVariable int id) {
        assureIdConsistent(restaurant, id);
        log.info("Update {}", restaurant);
        Restaurant created = service.update(modelMapper.map(restaurant, Restaurant.class));
        return modelMapper.map(created, RestaurantTo.class);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> createWithLocation(@Validated(View.Web.class) @RequestBody RestaurantTo restaurant) {
        RestaurantTo created = create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    private RestaurantTo create(RestaurantTo restaurant) {
        checkNew(restaurant);
        log.info("Create {}", restaurant);
        Restaurant created = service.create(modelMapper.map(restaurant, Restaurant.class));
        return modelMapper.map(created, RestaurantTo.class);
    }
}
