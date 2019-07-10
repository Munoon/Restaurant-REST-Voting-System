package com.train4game.munoon.repository.restaurant;

import com.train4game.munoon.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    int deleteMealById(int id);

    Restaurant getById(int id);

    @Query("SELECT r FROM Restaurant r WHERE r.date BETWEEN :start AND :end")
    List<Restaurant> getBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
