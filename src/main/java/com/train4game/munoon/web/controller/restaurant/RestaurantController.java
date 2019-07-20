package com.train4game.munoon.web.controller.restaurant;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController extends AbstractRestaurantController {
    @Autowired
    public RestaurantController(RestaurantService service) {
        super(service);
    }

    @GetMapping
    public String get(HttpServletRequest req) {
        req.setAttribute("restaurants", getAll());
        return "restaurants";
    }

    @GetMapping("/update")
    public String updateGet(HttpServletRequest req) {
        int id = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("edit", get(id));
        req.setAttribute("restaurants", getAll());
        return "restaurants";
    }

    @GetMapping("/delete")
    public String deleteGet(HttpServletRequest req) {
        int id = Integer.parseInt(req.getParameter("id"));
        delete(id);
        return "redirect:/restaurants";
    }

    @PostMapping("/edit")
    public String editPost(HttpServletRequest req) {
        String name = req.getParameter("name");
        int id = Integer.parseInt(req.getParameter("id"));
        Restaurant restaurant = get(id);
        restaurant.setName(name);
        update(restaurant, id);
        return "redirect:/restaurants";
    }

    @PostMapping("/create")
    public String createPost(HttpServletRequest req) {
        String name = req.getParameter("name");
        create(new Restaurant(null, name));
        return "redirect:/restaurants";
    }
}
