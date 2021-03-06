package com.train4game.munoon.service;

import com.train4game.munoon.model.Vote;
import com.train4game.munoon.repository.vote.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.*;

@Service
public class VoteService {
    private final VoteRepository repository;

    @Autowired
    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    public Vote create(Vote vote, int userId) {
        vote.setDate(LocalDate.now());
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

    public List<Vote> getAllByDate(int userId, LocalDate date) {
        Assert.notNull(date, "Date must be not null");
        return repository.getAllByDate(userId, date);
    }

    public List<Vote> getAll() {
        return repository.getAll();
    }

    public void update(Vote vote, int userId) {
        Assert.notNull(vote, "Vote must be not null");
        checkForSameDate(vote.getDate(), get(vote.getId(), userId).getDate(), "You cant change vote date");
        checkForTimeException();
        checkNotFoundWithId(repository.save(vote, userId), vote.getId());
    }
}
