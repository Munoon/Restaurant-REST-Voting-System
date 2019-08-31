package com.train4game.munoon.repository.user;

import com.train4game.munoon.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int deleteUserById(@Param("id") int id);

    User getUserByEmail(String email);

    @EntityGraph(User.WITH_ROLES)
    User getById(int id);

    @Override
    @EntityGraph(User.WITH_ROLES)
    List<User> findAll(Sort sort);
}
