package com.train4game.munoon.repository.meal;

import com.train4game.munoon.model.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class MealRepository {
    private static final Sort SORT_BY_DATE = new Sort(Sort.Direction.ASC, "date");

    @Autowired
    private CrudMealRepository repository;

    public Meal save(Meal meal) {
        return repository.save(meal);
    }

    public List<Meal> save(List<Meal> meals) {
        return repository.saveAll(meals);
    }

    public Meal get(int id) {
        return repository.getMealById(id);
    }

    public boolean delete(int id) {
        return repository.deleteMealById(id) != 0;
    }

    public List<Meal> getAll(int restaurantId) {
        return repository.getMealsByRestaurantId(restaurantId, SORT_BY_DATE);
    }

    public List<Meal> getAllByDate(int restaurantId, LocalDate date) {
        return repository.getMealsByRestaurantIdAndDate(restaurantId, date, SORT_BY_DATE);
    }

    public Meal getWithRestaurant(int id) {
        return repository.getMealByIdWithRestaurant(id);
    }
}
