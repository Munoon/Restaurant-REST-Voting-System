package com.train4game.munoon.repository.meal;

import com.train4game.munoon.model.Meal;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    @Override
    Meal save(Meal meal);

    @Transactional
    @Modifying
    int deleteMealById(int id);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.restaurant WHERE m.id=:id")
    Meal getMealByIdWithRestaurant(@Param("id") int id);

    Meal getMealById(int id);

    List<Meal> getMealsByRestaurantId(int restaurantId, Sort sort);

    List<Meal> getMealsByRestaurantIdAndDate(int restaurantId, LocalDate date, Sort sort);
}
