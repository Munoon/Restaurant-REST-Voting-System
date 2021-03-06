package com.train4game.munoon.repository.restaurant;

import com.train4game.munoon.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public class RestaurantRepository {
    private static final Sort SORT_BY_NAME = new Sort(Sort.Direction.ASC, "name");

    @Autowired
    private CrudRestaurantRepository repository;

    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public Restaurant get(int id) {
        return repository.getById(id);
    }

    public boolean delete(int id) {
        return repository.deleteMealById(id) != 0;
    }

    public List<Restaurant> getAll() {
        return repository.findAll(SORT_BY_NAME);
    }

    public List<Restaurant> getAllByMealDate(LocalDate date) {
        return repository.getAllByMenu_Date(date, SORT_BY_NAME);
    }
}
