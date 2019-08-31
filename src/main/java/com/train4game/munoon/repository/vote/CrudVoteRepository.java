package com.train4game.munoon.repository.vote;

import com.train4game.munoon.model.Vote;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {
    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.restaurant r LEFT JOIN FETCH r.menu LEFT JOIN FETCH v.user u WHERE v.id=:id AND u.id=:uid")
    Vote get(@Param("id") int id, @Param("uid") int userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id AND v.user.id=:uid")
    int delete(@Param("id") int id, @Param("uid") int userId);

    @Query("SELECT DISTINCT v FROM Vote v JOIN FETCH v.restaurant r JOIN FETCH r.menu JOIN FETCH v.user u WHERE u.id=:id")
    List<Vote> getAllByUserId(@Param("id") int userId, Sort sort);

    @Query("SELECT DISTINCT v FROM Vote v JOIN FETCH v.restaurant r JOIN FETCH r.menu JOIN FETCH v.user u WHERE u.id=:id AND v.date=:date")
    List<Vote> getAllByUserIdAndDate(@Param("id") int userId, @Param("date") LocalDate date, Sort sort);

    @Query("SELECT DISTINCT v FROM Vote v JOIN FETCH v.restaurant r JOIN FETCH r.menu JOIN FETCH v.user u JOIN FETCH u.roles")
    List<Vote> getAll(Sort sort);
}
