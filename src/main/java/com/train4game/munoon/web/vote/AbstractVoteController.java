package com.train4game.munoon.web.vote;

import com.train4game.munoon.model.Vote;
import com.train4game.munoon.service.VoteService;
import com.train4game.munoon.web.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;

abstract public class AbstractVoteController {
    private static final Logger log = LoggerFactory.getLogger(AbstractVoteController.class);

    @Autowired
    private VoteService service;

    public Vote create(Vote vote) {
        int userId = SecurityUtil.authUserId();
        log.info("Create {}", vote);
        checkNew(vote);
        return service.create(vote, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Delete vote {} with user {}", id, userId);
        service.delete(id, userId);
    }

    public Vote get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Get vote {} with user {}", id, userId);
        return service.get(id, userId);
    }

    public List<Vote> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("Get all votes of user {}", userId);
        return service.getAll(userId);
    }

    public void update(Vote vote, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(vote, id);
        log.info("Update {} with id {} from user {}", vote, id, userId);
        service.update(vote, userId);
    }
}
