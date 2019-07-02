package com.train4game.munoon.web.controller.user;

import com.train4game.munoon.model.User;
import com.train4game.munoon.web.SecurityUtil;
import org.springframework.stereotype.Controller;

@Controller
public class ProfileRestController extends AbstractUserController {
    public User get() {
        return super.get(SecurityUtil.getId());
    }

    public void delete() {
        super.delete(SecurityUtil.getId());
    }

    public void update(User user) {
        super.update(user, SecurityUtil.getId());
    }
}
