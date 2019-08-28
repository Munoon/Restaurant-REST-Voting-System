package com.train4game.munoon.repository.vote;

import com.train4game.munoon.model.Vote;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {
    Vote getVoteByIdAndUser_id(int id, int userId);

    @Transactional
    int deleteVoteByIdAndUser_id(int id, int userId);

    List<Vote> getAllByUser_id(int userId, Sort sort);

    List<Vote> getAllByUser_idAndDate(int userId, LocalDate date, Sort sort);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.restaurant.id=:id AND v.date=:date")
    int getCount(@Param("id") int restaurantId, @Param("date") LocalDate date);
}
