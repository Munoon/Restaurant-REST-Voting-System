package com.train4game.munoon.repository.user;

import com.train4game.munoon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Transactional
    int deleteUserById(int id);

    User getUserByEmail(String email);
}
