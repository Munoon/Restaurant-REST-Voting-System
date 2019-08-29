package com.train4game.munoon.repository.user;

import com.train4game.munoon.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserRepository {
    private static final Sort SORT_BY_DATE = new Sort(Sort.Direction.DESC, "registered");

    @Autowired
    private CrudUserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public User get(int id) {
        return repository.get(id);
    }

    @Transactional
    public boolean delete(int id) {
        return repository.deleteUserById(id) != 0;
    }

    public User getByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    public List<User> getAll() {
        return repository.getAll(SORT_BY_DATE);
    }
}
