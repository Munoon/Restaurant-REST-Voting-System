package com.train4game.munoon.repository.vote;

import com.train4game.munoon.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {
    Vote getVoteByIdAndUser_id(int id, int userId);

    @Transactional
    int deleteVoteByIdAndUser_id(int id, int userId);

    List<Vote> getAllByUser_id(int userId);
}
