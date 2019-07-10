package com.train4game.munoon.service;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.model.User;
import com.train4game.munoon.repository.restaurant.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.checkNotFoundWithId;
import static com.train4game.munoon.utils.ValidationUtils.checkUserForAdmin;

@Service
public class RestaurantService {
    private final RestaurantRepository repository;

    @Autowired
    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant create(Restaurant restaurant, User user) {
        checkUserForAdmin(user);
        Assert.notNull(restaurant, "Restaurant must be not null");
        return repository.save(restaurant);
    }

    public void delete(int id, User user) {
        checkUserForAdmin(user);
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    public Restaurant update(Restaurant restaurant, User user) {
        checkUserForAdmin(user);
        Assert.notNull(restaurant, "Restaurant must be not null");
        return repository.save(restaurant);
    }

    public List<Restaurant> getBetween(LocalDateTime start, LocalDateTime end) {
        Assert.notNull(start, "Start date must be not null");
        Assert.notNull(end, "End date must be not null");
        return repository.getBetween(start, end);
    }
}
