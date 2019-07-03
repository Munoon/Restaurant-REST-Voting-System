package com.train4game.munoon.web;

import com.train4game.munoon.model.Restaurant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.train4game.munoon.web.MainServlet.restaurantController;

@WebServlet("/restaurants")
public class RestaurantServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("type") == null ? "default" : req.getParameter("type")) {
            case "update":
                int updateId = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("edit", restaurantController.get(updateId));
                break;
            case "delete":
                int deleteId = Integer.parseInt(req.getParameter("id"));
                restaurantController.delete(deleteId);
                break;
        }
        req.setAttribute("restaurants", restaurantController.getAll());
        req.getRequestDispatcher("/restaurants.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");

        switch (req.getParameter("type")) {
            case "edit":
                int id = Integer.parseInt(req.getParameter("id"));
                Restaurant restaurant = restaurantController.get(id);
                restaurant.setName(name);
                restaurantController.update(restaurant, id);
            case "create":
                Restaurant newRestaurant = new Restaurant(null, name);
                restaurantController.create(newRestaurant);
        }

        resp.sendRedirect("./restaurants");
    }
}
