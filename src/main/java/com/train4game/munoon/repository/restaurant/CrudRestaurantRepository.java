package com.train4game.munoon.repository.restaurant;

import com.train4game.munoon.model.Restaurant;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int deleteMealById(@Param("id") int id);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menu WHERE r.id=:id")
    Restaurant getById(@Param("id") int id);

    @Query("SELECT DISTINCT r FROM Restaurant r JOIN FETCH r.menu m WHERE m.date=:date")
    List<Restaurant> getAllByMealsDate(@Param("date") LocalDate date, Sort sort);
}
