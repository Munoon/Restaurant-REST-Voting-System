package com.train4game.munoon.web.controller.restaurant;

import com.train4game.munoon.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RestaurantRestController extends AbstractRestaurantController {
    @Autowired
    public RestaurantRestController(RestaurantService service) {
        super(service);
    }
}
