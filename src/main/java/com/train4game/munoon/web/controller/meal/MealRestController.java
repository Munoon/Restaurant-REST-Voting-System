package com.train4game.munoon.web.controller.meal;

import com.train4game.munoon.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MealRestController extends AbstractMealController {
    @Autowired
    public MealRestController(MealService service) {
        super(service);
    }
}
