package com.train4game.munoon.web.controller;

import com.train4game.munoon.model.Vote;
import com.train4game.munoon.service.VoteService;
import com.train4game.munoon.web.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;

@Controller
public class VoteRestController {
    private static final Logger log = LoggerFactory.getLogger(VoteRestController.class);
    private final VoteService service;

    @Autowired
    public VoteRestController(VoteService service) {
        this.service = service;
    }

    public Vote create(Vote vote) {
        int userId = SecurityUtil.getId();
        log.info("Create {}", vote);
        checkNew(vote);
        return service.create(vote, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.getId();
        log.info("Delete vote {} with user {}", id, userId);
        service.delete(id, userId);
    }

    public Vote get(int id) {
        int userId = SecurityUtil.getId();
        log.info("Get vote {} with user {}", id, userId);
        return service.get(id, userId);
    }

    public List<Vote> getAll() {
        int userId = SecurityUtil.getId();
        log.info("Get all votes of user {}", userId);
        return service.getAll(userId);
    }

    public void update(Vote vote, int id) {
        int userId = SecurityUtil.getId();
        assureIdConsistent(vote, id);
        log.info("Update {} with id {} from user {}", vote, id, userId);
        service.update(vote, userId);
    }
}
