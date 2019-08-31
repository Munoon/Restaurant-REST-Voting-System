package com.train4game.munoon.repository.vote;

import com.train4game.munoon.model.Vote;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {
    @EntityGraph(Vote.WITH_PARENTS)
    @Query("SELECT v FROM Vote v WHERE v.id=:id AND v.user.id=:uid")
    Vote get(@Param("id") int id, @Param("uid") int userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id AND v.user.id=:uid")
    int delete(@Param("id") int id, @Param("uid") int userId);

    @EntityGraph(Vote.WITH_PARENTS)
    @Query("SELECT v FROM Vote v WHERE v.user.id=:id")
    List<Vote> getAllByUserId(@Param("id") int userId, Sort sort);

    @EntityGraph(Vote.WITH_PARENTS)
    @Query("SELECT v FROM Vote v WHERE v.user.id=:id AND v.date=:date")
    List<Vote> getAllByUserIdAndDate(@Param("id") int userId, @Param("date") LocalDate date, Sort sort);

    @Override
    @EntityGraph(Vote.WITH_PARENTS)
    List<Vote> findAll(Sort sort);
}
