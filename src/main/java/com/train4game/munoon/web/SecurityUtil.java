package com.train4game.munoon.web;

import com.train4game.munoon.model.AbstractBaseEntity;
import com.train4game.munoon.model.Roles;
import com.train4game.munoon.model.User;

import java.time.LocalDateTime;
import java.util.Collections;

public class SecurityUtil {
    private static int id = AbstractBaseEntity.START_SEQ;
    private static User user = new User(AbstractBaseEntity.START_SEQ, "Nikita", "munoon@yandex.ru", "easyPass", LocalDateTime.now(), true, Collections.singleton(Roles.ROLE_ADMIN));

    private SecurityUtil() {
    }

    public static void setUserAndId(User user, int id) {
        SecurityUtil.user = user;
        SecurityUtil.id = id;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        SecurityUtil.user = user;
    }

    public static int authUserId() {
        return id;
    }

    public static void setId(int id) {
        SecurityUtil.id = id;
    }
}
