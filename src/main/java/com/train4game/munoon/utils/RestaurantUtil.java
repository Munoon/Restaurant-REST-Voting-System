package com.train4game.munoon.utils;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.service.VoteService;
import com.train4game.munoon.to.MealTo;
import com.train4game.munoon.to.RestaurantToWithVotes;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.train4game.munoon.utils.ParserUtil.MEAL_LIST_MAPPER;
import static com.train4game.munoon.utils.ParserUtil.parseRestaurantWithVotes;

public class RestaurantUtil {
    public static List<RestaurantToWithVotes> parseWithVotes(List<Restaurant> restaurants, LocalDate date, ModelMapper mapper, VoteService service) {
        List<RestaurantToWithVotes> result = new ArrayList<>();
        restaurants.forEach(restaurant -> result.add(parseWithVotes(restaurant, date, mapper, service)));
        return result;
    }

    public static RestaurantToWithVotes parseWithVotes(Restaurant restaurant, LocalDate date, ModelMapper mapper, VoteService service) {
        List<MealTo> menu = mapper.map(restaurant.getMenu(), MEAL_LIST_MAPPER);
        int votes = service.getCount(restaurant.getId(), date);
        return parseRestaurantWithVotes(restaurant, menu, votes);
    }
}
