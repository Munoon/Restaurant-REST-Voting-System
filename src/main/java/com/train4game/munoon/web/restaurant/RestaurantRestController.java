package com.train4game.munoon.web.restaurant;

import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.to.RestaurantTo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController extends AbstractRestaurantController {
    public static final String REST_URL = "/restaurant";

    @Autowired
    public RestaurantRestController(RestaurantService service, ModelMapper modelMapper) {
        super(service, modelMapper);
    }

    @GetMapping("/all")
    public List<RestaurantTo> getAll() {
        return super.getAll();
    }

    @GetMapping
    public List<RestaurantTo> getAllWithTodayMeals(@RequestParam(required = false) LocalDate date) {
        return super.getAllByMealDate(date == null ? LocalDate.now() : date);
    }

    @Override
    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public RestaurantTo update(@RequestBody RestaurantTo restaurant, @PathVariable int id) {
        return super.update(restaurant, id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> createWithLocation(@RequestBody RestaurantTo restaurant) {
        RestaurantTo created = super.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
