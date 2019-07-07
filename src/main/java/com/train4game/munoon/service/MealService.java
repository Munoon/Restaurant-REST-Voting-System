package com.train4game.munoon.service;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.model.User;
import com.train4game.munoon.repository.meal.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.checkNotFoundWithId;
import static com.train4game.munoon.utils.ValidationUtils.checkUserForAdmin;

@Service
public class MealService {
    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, User user) {
        checkUserForAdmin(user);
        Assert.notNull(meal, "Meal must be not null");
        return repository.save(meal);
    }

    public void delete(int id, User user) {
        checkUserForAdmin(user);
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Meal get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Meal> getAll(int restaurantId) {
        return repository.getAll(restaurantId);
    }

    public void update(Meal meal, User user) {
        checkUserForAdmin(user);
        Assert.notNull(meal, "Meal must be not null");
        repository.save(meal);
    }

    public Meal getWithRestaurant(int id) {
        return checkNotFoundWithId(repository.getWithRestaurant(id), id);
    }
}
