package com.train4game.munoon.service;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.repository.meal.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.checkNotFoundWithId;

@Service
public class MealService {
    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "meals", allEntries = true)
    public Meal create(Meal meal) {
        Assert.notNull(meal, "Meal must be not null");
        return repository.save(meal);
    }

    @CacheEvict(value = "meals", allEntries = true)
    public List<Meal> create(List<Meal> meal) {
        Assert.notNull(meal, "Meals list must be not null");
        return repository.save(meal);
    }

    @CacheEvict(value = "meals", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Meal get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Cacheable("meals")
    public List<Meal> getAll(int restaurantId) {
        return repository.getAll(restaurantId);
    }

    public List<Meal> getAllByDate(int restaurantId, LocalDate date) {
        Assert.notNull(date, "Date must be not null");
        return repository.getAllByDate(restaurantId, date);
    }

    @CacheEvict(value = "meals", allEntries = true)
    public void update(Meal meal) {
        Assert.notNull(meal, "Meal must be not null");
        repository.save(meal);
    }

    public Meal getWithRestaurant(int id) {
        return checkNotFoundWithId(repository.getWithRestaurant(id), id);
    }
}
