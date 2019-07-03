package com.train4game.munoon.web;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.model.Vote;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.train4game.munoon.web.MainServlet.restaurantController;
import static com.train4game.munoon.web.MainServlet.voteRestController;

@WebServlet("/vote")
public class VoteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("type") == null ? "default" : req.getParameter("type")) {
            case "edit":
                int editId = Integer.parseInt(req.getParameter("editId"));
                req.setAttribute("edit", voteRestController.get(editId));
                break;
            case "delete":
                int deleteId = Integer.parseInt(req.getParameter("deleteId"));
                voteRestController.delete(deleteId);
                break;
        }

        req.setAttribute("votes", voteRestController.getAll());
        req.setAttribute("restaurants", restaurantController.getAll());
        req.getRequestDispatcher("/vote.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int restaurantId = Integer.parseInt(req.getParameter("restaurantId"));

        switch (req.getParameter("type")) {
            case "create":
                Restaurant restaurant = restaurantController.get(restaurantId);
                Vote newVote = new Vote(null, null, restaurant, LocalDateTime.now());
                System.out.println(newVote);
                voteRestController.create(newVote);
                break;
            case "edit":
                int editId = Integer.parseInt(req.getParameter("editId"));
                Vote vote = voteRestController.get(editId);
                vote.setRestaurant(restaurantController.get(restaurantId));
                voteRestController.update(vote, editId);
                break;
        }

        resp.sendRedirect("./vote");
    }
}
