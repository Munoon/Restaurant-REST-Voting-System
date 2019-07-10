package com.train4game.munoon.repository.restaurant;

import com.train4game.munoon.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    int deleteMealById(int id);

    Restaurant getById(int id);
}
