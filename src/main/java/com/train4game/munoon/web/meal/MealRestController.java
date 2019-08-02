package com.train4game.munoon.web.meal;

import com.train4game.munoon.service.MealService;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.to.meal.MealTo;
import com.train4game.munoon.to.meal.MealToWithRestaurant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {
    public static final String REST_URL = "/meals";

    @Autowired
    public MealRestController(MealService service, RestaurantService restaurantService, ModelMapper modelMapper) {
        super(service, restaurantService, modelMapper);
    }

    @Override
    @GetMapping("/all/{restaurant}")
    public List<MealTo> getAll(@PathVariable int restaurant) {
        return super.getAll(restaurant);
    }

    @Override
    @GetMapping("/{id}")
    public MealTo get(@PathVariable int id) {
        return super.get(id);
    }

    @GetMapping("/with/{id}")
    public MealToWithRestaurant getWithRestaurant(@PathVariable int id) {
        return super.getWithRestaurant(id);
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
    public void update(@RequestBody MealTo meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MealTo> createWithLocation(@RequestBody MealTo meal) {
        MealTo created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
