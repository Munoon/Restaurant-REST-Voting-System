package com.train4game.munoon.web;

import com.train4game.munoon.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.train4game.munoon.web.MainServlet.mealRestController;
import static com.train4game.munoon.web.MainServlet.restaurantController;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("type") == null ? "default" : req.getParameter("type")) {
            case "delete":
                int deleteId = Integer.parseInt(req.getParameter("deleteId"));
                mealRestController.delete(deleteId);
                break;
            case "edit":
                int editId = Integer.parseInt(req.getParameter("editId"));
                req.setAttribute("edit", mealRestController.get(editId));
                break;
        }

        int restaurantId = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("restaurant", restaurantController.get(restaurantId));
        req.setAttribute("menu", mealRestController.getAll(restaurantId));
        req.getRequestDispatcher("/menu.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int restaurantId = Integer.parseInt(req.getParameter("restaurant"));
        String name = req.getParameter("name");
        int price = Integer.parseInt(req.getParameter("price"));
        LocalDateTime date = LocalDateTime.parse(req.getParameter("date"));

        switch (req.getParameter("type")) {
            case "edit":
                int mealId = Integer.parseInt(req.getParameter("mealId"));
                Meal meal = mealRestController.get(mealId);
                meal.setName(name);
                meal.setPrice(price);
                meal.setDate(date);
                mealRestController.update(meal, mealId);
                break;
            case "create":
                Meal newMeal = new Meal(null, name, restaurantController.get(restaurantId), price, date);
                mealRestController.create(newMeal);
                break;
        }

        resp.sendRedirect("./menu?id=" + restaurantId);
    }
}
