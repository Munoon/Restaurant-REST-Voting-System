package com.train4game.munoon.web.controller.user;

import com.train4game.munoon.model.User;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AdminRestController extends AbstractUserController {
    @Override
    public User create(User user) {
        return super.create(user);
    }

    @Override
    public void delete(int userId) {
        super.delete(userId);
    }

    @Override
    public User get(int userId) {
        return super.get(userId);
    }

    @Override
    public User getByEmail(String email) {
        return super.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    public void update(User user, int id) {
        super.update(user, id);
    }
}
