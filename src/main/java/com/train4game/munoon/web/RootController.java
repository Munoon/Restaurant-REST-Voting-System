package com.train4game.munoon.web;

import com.train4game.munoon.web.controller.user.ProfileRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RootController {
    private static final Logger log = LoggerFactory.getLogger(RootController.class);

    @Autowired
    private ProfileRestController controller;

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/user")
    public String users(HttpServletRequest req) {
        log.debug("Forward to user");
        req.setAttribute("user", SecurityUtil.getUser());
        return "user";
    }

    @PostMapping("/user")
    public String userPost(HttpServletRequest req) {
        int userId = Integer.parseInt(req.getParameter("userId"));
        SecurityUtil.setId(userId);
        SecurityUtil.setUser(controller.get());
        return "redirect:user";
    }
}
