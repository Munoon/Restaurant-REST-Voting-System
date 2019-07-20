package com.train4game.munoon.web.controller.meal;

import com.train4game.munoon.model.Meal;
import com.train4game.munoon.service.MealService;
import com.train4game.munoon.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/menu")
public class MealController extends AbstractMealController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    public MealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getMenu(HttpServletRequest req) {
        int restaurantId = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("restaurant", restaurantService.get(restaurantId));
        req.setAttribute("menu", getAll(restaurantId));
        return "menu";
    }

    @GetMapping("/delete")
    public String deleteMenu(HttpServletRequest req) {
        int restaurantId = Integer.parseInt(req.getParameter("id"));
        int id = Integer.parseInt(req.getParameter("deleteId"));
        delete(id);
        return "redirect:/menu?id=" + restaurantId;
    }

    @GetMapping("/edit")
    public String updateMenu(HttpServletRequest req) {
        int id = Integer.parseInt(req.getParameter("editId"));
        int restaurantId = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("edit", get(id));
        req.setAttribute("menu", getAll(restaurantId));
        req.setAttribute("restaurant", restaurantService.get(restaurantId));
        return "menu";
    }

    @PostMapping("/update")
    public String editMenu(HttpServletRequest req) {
        int restaurantId = Integer.parseInt(req.getParameter("restaurant"));
        String name = req.getParameter("name");
        int price = Integer.parseInt(req.getParameter("price"));
        LocalDateTime date = LocalDateTime.parse(req.getParameter("date"));
        int id = Integer.parseInt(req.getParameter("mealId"));

        Meal meal = get(id);
        meal.setName(name);
        meal.setPrice(price);
        meal.setDate(date);
        update(meal, id);
        return "redirect:/menu?id=" + restaurantId;
    }

    @PostMapping("/create")
    public String createMenu(HttpServletRequest req) {
        int restaurantId = Integer.parseInt(req.getParameter("restaurant"));
        String name = req.getParameter("name");
        int price = Integer.parseInt(req.getParameter("price"));
        LocalDateTime date = LocalDateTime.parse(req.getParameter("date"));

        create(new Meal(null, name, restaurantService.get(restaurantId), price, date));
        return "redirect:/menu?id=" + restaurantId;
    }
}
