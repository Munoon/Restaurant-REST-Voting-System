package com.train4game.munoon.web.vote;

import com.train4game.munoon.model.Vote;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.service.VoteService;
import com.train4game.munoon.to.VoteTo;
import com.train4game.munoon.utils.VoteUtil;
import com.train4game.munoon.web.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;
import static com.train4game.munoon.utils.VoteUtil.parseVote;

abstract public class AbstractVoteController {
    private static final Logger log = LoggerFactory.getLogger(AbstractVoteController.class);

    @Autowired
    private VoteService service;

    @Autowired
    private RestaurantService restaurantService;

    public VoteTo create(VoteTo voteTo) {
        int userId = SecurityUtil.authUserId();
        log.info("Create {}", voteTo);
        checkNew(voteTo);
        return parseVote(service.create(createVote(voteTo), userId));
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Delete vote {} with user {}", id, userId);
        service.delete(id, userId);
    }

    public VoteTo get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Get vote {} with user {}", id, userId);
        return parseVote(service.get(id, userId));
    }

    public List<VoteTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("Get all votes of user {}", userId);
        return parseVote(service.getAll(userId));
    }

    public void update(VoteTo voteTo, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(voteTo, id);
        log.info("Update {} with id {} from user {}", voteTo, id, userId);
        service.update(createVote(voteTo), userId);
    }

    private Vote createVote(VoteTo voteTo) {
        return VoteUtil.createVote(voteTo, restaurantService.get(voteTo.getRestaurantId()));
    }
}
