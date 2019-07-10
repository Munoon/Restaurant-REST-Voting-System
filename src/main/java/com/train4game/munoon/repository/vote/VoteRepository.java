package com.train4game.munoon.repository.vote;

import com.train4game.munoon.model.User;
import com.train4game.munoon.model.Vote;
import com.train4game.munoon.repository.user.CrudUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class VoteRepository {
    @Autowired
    private CrudVoteRepository repository;

    @Autowired
    private CrudUserRepository userRepository;

    @Transactional
    public Vote save(Vote vote, int userId) {
        User ref = userRepository.getOne(userId);
        vote.setUser(ref);

        if (vote.isNew()) {
            return repository.save(vote);
        } else {
            return get(vote.getId(), userId) == null ? null : repository.save(vote);
        }
    }

    public Vote get(int id, int userId) {
        return repository.getVoteByIdAndUser_id(id, userId);
    }

    @Transactional
    public boolean delete(int id, int userId) {
        return repository.deleteVoteByIdAndUser_id(id, userId) != 0;
    }

    public List<Vote> getAll(int userId) {
        return repository.getAllByUser_id(userId);
    }
}
