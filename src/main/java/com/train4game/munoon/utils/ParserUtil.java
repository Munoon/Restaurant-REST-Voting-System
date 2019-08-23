package com.train4game.munoon.utils;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.to.MealTo;
import com.train4game.munoon.to.RestaurantTo;
import com.train4game.munoon.to.RestaurantToWithVotes;
import com.train4game.munoon.to.VoteTo;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ParserUtil {
    public static final Type MEAL_LIST_MAPPER = new TypeToken<List<MealTo>>() {}.getType();
    public static final Type MEAL_TO_LIST_MAPPER = new TypeToken<List<Meal>>() {}.getType();
    public static final Type RESTAURANT_LIST_MAPPER = new TypeToken<List<RestaurantTo>>() {}.getType();
    public static final Type VOTE_LIST_MAPPER = new TypeToken<List<VoteTo>>() {}.getType();

    public static RestaurantToWithVotes parseRestaurantWithVotes(Restaurant restaurant, List<MealTo> menu, int votes) {
        return new RestaurantToWithVotes(restaurant.getId(), restaurant.getName(), menu, votes);
    }
}
