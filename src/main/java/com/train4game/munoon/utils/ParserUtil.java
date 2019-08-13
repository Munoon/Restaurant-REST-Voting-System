package com.train4game.munoon.utils;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.service.VoteService;
import com.train4game.munoon.to.MealTo;
import com.train4game.munoon.to.RestaurantTo;
import com.train4game.munoon.to.RestaurantToWithVotes;
import com.train4game.munoon.to.VoteTo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ParserUtil {
    public static final Type MEAL_LIST_MAPPER = new TypeToken<List<MealTo>>() {}.getType();
    public static final Type RESTAURANT_LIST_MAPPER = new TypeToken<List<RestaurantTo>>() {}.getType();
    public static final Type VOTE_LIST_MAPPER = new TypeToken<List<VoteTo>>() {}.getType();

    public static List<RestaurantToWithVotes> parseRestaurantWithVotes(List<Restaurant> restaurants, ModelMapper modelMapper, VoteService service, LocalDate date) {
        List<RestaurantToWithVotes> result = new ArrayList<>();
        restaurants.forEach(restaurant -> {
            result.add(parseRestaurantWithVotes(restaurant, modelMapper, service, date));
        });
        return result;
    }

    public static RestaurantToWithVotes parseRestaurantWithVotes(Restaurant restaurant, ModelMapper modelMapper, VoteService service, LocalDate date) {
        int votes = service.getCount(restaurant.getId(), date);
        List<MealTo> menu = modelMapper.map(restaurant.getMenu(), MEAL_LIST_MAPPER);
        return new RestaurantToWithVotes(restaurant.getId(), restaurant.getName(), menu, votes);
    }
}
