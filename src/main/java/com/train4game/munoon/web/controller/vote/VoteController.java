package com.train4game.munoon.web.controller.vote;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.model.Vote;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/vote")
public class VoteController extends AbstractVoteController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    public VoteController(VoteService service) {
        super(service);
    }

    @GetMapping
    public String doGet(HttpServletRequest req) {
        req.setAttribute("votes", getAll());
        req.setAttribute("restaurants", restaurantService.getAll());
        return "vote";
    }

    @GetMapping("/delete")
    public String deleteGet(HttpServletRequest req) {
        int id = Integer.parseInt(req.getParameter("id"));
        delete(id);
        return "redirect:/vote";
    }

    @GetMapping("/edit")
    public String editGet(HttpServletRequest req) {
        int id = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("edit", get(id));
        req.setAttribute("restaurants", restaurantService.getAll());
        req.setAttribute("votes", getAll());
        return "vote";
    }

    @PostMapping("/create")
    public String createPost(HttpServletRequest req) {
        int restaurantId = Integer.parseInt(req.getParameter("restaurantId"));
        Restaurant restaurant = restaurantService.get(restaurantId);
        Vote newVote = new Vote(null, null, restaurant, LocalDateTime.now());
        create(newVote);
        return "redirect:/vote";
    }

    @PostMapping("/update")
    public String editPost(HttpServletRequest req) {
        int restaurantId = Integer.parseInt(req.getParameter("restaurantId"));
        int editId = Integer.parseInt(req.getParameter("editId"));
        Vote vote = get(editId);
        vote.setRestaurant(restaurantService.get(restaurantId));
        update(vote, editId);
        return "redirect:/vote";
    }
}
