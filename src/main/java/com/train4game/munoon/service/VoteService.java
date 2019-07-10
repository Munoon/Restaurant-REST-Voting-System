package com.train4game.munoon.service;

import com.train4game.munoon.model.Vote;
import com.train4game.munoon.repository.vote.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.checkForTimeException;
import static com.train4game.munoon.utils.ValidationUtils.checkNotFoundWithId;

@Service
public class VoteService {
    private final VoteRepository repository;

    @Autowired
    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    public Vote create(Vote vote, int userId) {
        Assert.notNull(vote, "Vote must be not null");
        return repository.save(vote, userId);
    }

    public void delete(int id, int userId) {
        checkForTimeException();
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Vote get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Vote> getAll(int userId) {
        return repository.getAll(userId);
    }

    public void update(Vote vote, int userId) {
        Assert.notNull(vote, "Vote must be not null");
        checkForTimeException();
        checkNotFoundWithId(repository.save(vote, userId), vote.getId());
    }
}
