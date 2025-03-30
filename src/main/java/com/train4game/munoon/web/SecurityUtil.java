package com.train4game.munoon.web;

import com.train4game.munoon.AuthorizedUser;
import com.train4game.munoon.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Objects.requireNonNull;

public class SecurityUtil {
    private SecurityUtil() {
    }

    public static AuthorizedUser saveGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return null;
        Object principal = auth.getPrincipal();
        return principal instanceof AuthorizedUser ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser get() {
        AuthorizedUser user = saveGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    public static int authUserId() {
        return get().getUser().getId();
    }

    public static void unusedMethod() {
        throw new RuntimeException("Shouldn't be used");
    }

//    public static User getUser() {
//        return get().getUser();
//    }
}
