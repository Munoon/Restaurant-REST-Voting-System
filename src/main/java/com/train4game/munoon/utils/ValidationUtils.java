package com.train4game.munoon.utils;

import com.train4game.munoon.model.AbstractBaseEntity;
import com.train4game.munoon.model.Roles;
import com.train4game.munoon.model.User;
import com.train4game.munoon.to.AbstractBaseTo;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import com.train4game.munoon.utils.exceptions.PermissionDeniedException;
import com.train4game.munoon.utils.exceptions.TimeOverException;

import java.time.DateTimeException;
import java.time.LocalDate;
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

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void checkNew(AbstractBaseTo entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    public static void assureIdConsistent(AbstractBaseTo entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static void checkDateForToday(LocalDate date, String msg) {
        if (!date.equals(LocalDate.now()))
            throw new DateTimeException(msg);
    }

    public static void checkForSameDate(LocalDate actual, LocalDate expected, String msg) {
        if (!actual.equals(expected))
            throw new DateTimeException(msg);
    }
}
