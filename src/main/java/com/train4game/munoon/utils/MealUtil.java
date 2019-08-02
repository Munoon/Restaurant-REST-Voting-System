package com.train4game.munoon.utils;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.to.MealTo;

import java.util.List;
import java.util.stream.Collectors;

public class MealUtil {
    public static MealTo parse(Meal meal) {
        return new MealTo(meal.getId(), meal.getName(), meal.getPrice(), meal.getDate(), meal.getRestaurant().getId());
    }

    public static Meal parseToMeal(MealTo meal, Restaurant restaurant) {
        return new Meal(meal.getId(), meal.getName(), restaurant, meal.getPrice(), meal.getDate());
    }

    public static List<MealTo> parseAll(List<Meal> meal) {
        return meal.stream()
                .map(MealUtil::parse)
                .collect(Collectors.toList());
    }
}
