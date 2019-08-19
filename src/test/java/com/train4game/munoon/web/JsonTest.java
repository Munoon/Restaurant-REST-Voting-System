package com.train4game.munoon.web;

import com.train4game.munoon.data.UserTestData;
import com.train4game.munoon.model.Meal;
import com.train4game.munoon.model.User;
import com.train4game.munoon.utils.JsonUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.train4game.munoon.data.MealTestData.*;
import static com.train4game.munoon.data.UserTestData.FIRST_USER;
import static org.assertj.core.api.Assertions.assertThat;

// Решил JsonTest положить в папку web так-как в папке web уменя лежит JacksonObjectMapper
// Создавать папку Util чисто для трёх тестов я считаю глупо
public class JsonTest {
    @Test
    void readWriteTest() {
        String json = JsonUtil.writeValue(FIRST_MEAL);
        System.out.println(json);
        Meal meal = JsonUtil.readValue(json, Meal.class);
        assertMatch(meal, FIRST_MEAL);
    }

    @Test
    void readWriteValues() throws Exception {
        String json = JsonUtil.writeValue(FIRST_RESTAURANT_MENU);
        System.out.println(json);
        List<Meal> meals = JsonUtil.readValues(json, Meal.class);
        assertMatch(meals, FIRST_RESTAURANT_MENU);
    }

    @Test
    void testWriteOnlyAccess() throws Exception {
        String json = JsonUtil.writeValue(FIRST_USER);
        System.out.println(json);

        assertThat(json.contains("password") || json.contains(FIRST_USER.getPassword())).isEqualTo(false);

        String jsonWithPass = UserTestData.jsonWithPassword(FIRST_USER, "new Password");
        System.out.println(jsonWithPass);

        User user = JsonUtil.readValue(jsonWithPass, User.class);
        assertThat(user.getPassword()).isEqualTo("new Password");
    }
}
