package com.train4game.munoon.repository;

import com.train4game.munoon.model.Meal;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class MealRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            return em.merge(meal);
        }
    }

    public Meal get(int id) {
        return em.find(Meal.class, id);
    }

    @Transactional
    public boolean delete(int id) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    public List<Meal> getAll(int restaurantId) {
        return em.createNamedQuery(Meal.GET_ALL, Meal.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }
}
