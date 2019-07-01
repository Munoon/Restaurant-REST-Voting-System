package com.train4game.munoon.utils;

import com.train4game.munoon.model.Roles;
import com.train4game.munoon.model.User;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import com.train4game.munoon.utils.exceptions.PermissionDeniedException;
import com.train4game.munoon.utils.exceptions.TimeOverException;

import java.time.LocalTime;

public class ValidationUtils {
    private ValidationUtils() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkUserForAdmin(User user) {
        if (!user.getRoles().contains(Roles.ROLE_ADMIN))
            throw new PermissionDeniedException("User didnt have admin role");
    }

    public static void checkForTimeException() {
        LocalTime time = LocalTime.now();
        LocalTime endTime = LocalTime.of(11, 0);
        if (time.isAfter(endTime))
            throw new TimeOverException();
    }
}
