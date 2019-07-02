package com.train4game.munoon.web.controller.user;

import com.train4game.munoon.model.User;
import com.train4game.munoon.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;

public abstract class AbstractUserController {
    private Logger log = LoggerFactory.getLogger(AbstractUserController.class);

    @Autowired
    private UserService service;

    public User create(User user) {
        log.info("Created user {}", user);
        checkNew(user);
        return service.create(user);
    }

    public void delete(int userId) {
        log.info("Delete user {}", userId);
        service.delete(userId);
    }

    public User get(int userId) {
        log.info("Get user {}", userId);
        return service.get(userId);
    }

    public User getByEmail(String email) {
        log.info("Get user by email {}", email);
        return service.getByEmail(email);
    }

    public List<User> getAll() {
        log.info("Get all users");
        return service.getAll();
    }

    public void update(User user, int id) {
        log.info("Update {} with id {}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }
}
