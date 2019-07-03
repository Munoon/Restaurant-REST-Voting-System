package com.train4game.munoon.web;

import com.train4game.munoon.web.controller.MealRestController;
import com.train4game.munoon.web.controller.RestaurantController;
import com.train4game.munoon.web.controller.user.ProfileRestController;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("")
public class MainServlet extends HttpServlet {
    public static ConfigurableApplicationContext springContext;
    public static ProfileRestController profileRestController;
    public static RestaurantController restaurantController;
    public static MealRestController mealRestController;

    @Override
    public void init() throws ServletException {
        System.out.println("INIT");
        springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");
        profileRestController = springContext.getBean(ProfileRestController.class);
        restaurantController = springContext.getBean(RestaurantController.class);
        mealRestController = springContext.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        springContext.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
