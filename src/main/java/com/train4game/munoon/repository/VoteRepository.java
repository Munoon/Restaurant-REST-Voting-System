package com.train4game.munoon.repository;

import com.train4game.munoon.model.User;
import com.train4game.munoon.model.Vote;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class VoteRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Vote save(Vote vote, int userId) {
        User ref = em.getReference(User.class, userId);
        vote.setUser(ref);

        if (vote.isNew()) {
            em.persist(vote);
            return vote;
        } else {
            Vote getVote = get(vote.getId(), userId);
            if (getVote != null)
                return em.merge(vote);
            return null;
        }
    }

    public Vote get(int id, int userId) {
        Vote vote = em.find(Vote.class, id);
        return vote == null || vote.getUser().getId() != userId ? null : vote;
    }

    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Vote.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    public List<Vote> getAll(int userId) {
        return em.createNamedQuery(Vote.GET_ALL, Vote.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
