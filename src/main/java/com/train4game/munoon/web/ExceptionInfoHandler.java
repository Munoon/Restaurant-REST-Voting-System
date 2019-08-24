package com.train4game.munoon.web;

import com.train4game.munoon.utils.ValidationUtils;
import com.train4game.munoon.utils.exceptions.ErrorInfo;
import com.train4game.munoon.utils.exceptions.ErrorType;
import com.train4game.munoon.utils.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.train4game.munoon.utils.exceptions.ErrorType.*;

@ControllerAdvice
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionInfoHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);
    public static final Map<String, String> DATABASE_ERROR_MAP = Map.of(
            "users_unique_email_idx", "User with this email already exists",
            "restaurants_unique_name_idx", "Restaurant with this name already exists",
            "meals_unique_name_idx", "Meal with this name already exists",
            "users_votes_unique_date_idx", "You have already voted today"
    );

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo notFoundHandler(HttpServletRequest req, NotFoundException e) {
        return warnAndGetErrorInfo(req.getRequestURL(), DATA_NOT_FOUND, e);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorInfo wrongRequest(HttpServletRequest req, NoHandlerFoundException e) {
        return warnAndGetErrorInfo(req.getRequestURL(), ErrorType.WRONG_REQUEST, e);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtils.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            Optional<Map.Entry<String, String>> entry = DATABASE_ERROR_MAP.entrySet().stream()
                    .filter(it -> lowerCaseMsg.contains(it.getKey()))
                    .findAny();
            if (entry.isPresent())
                return warnAndGetErrorInfo(req.getRequestURL(), VALIDATION_ERROR, Collections.singletonList(entry.get().getValue()));
        }
        return warnAndGetErrorInfo(req.getRequestURL(), DATA_ERROR, e);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return warnAndGetErrorInfo(req.getRequestURL(), VALIDATION_ERROR, e);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    public ErrorInfo beanValidationExceptionHandler(HttpServletRequest req, BindException e) {
        List<String> errors = ValidationUtils.getErrorsFieldList(e.getFieldErrors());
        return warnAndGetErrorInfo(req.getRequestURL(), VALIDATION_ERROR, errors);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo argumentNotValidHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        List<String> errors = ValidationUtils.getErrorsFieldList(e.getBindingResult().getFieldErrors());
        return warnAndGetErrorInfo(req.getRequestURL(), VALIDATION_ERROR, errors);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo globalExceptionHandler(HttpServletRequest req, Exception e) {
        String url = req.getRequestURL().toString();
        Throwable rootCause = ValidationUtils.getRootCause(e);
        log.error(APP_ERROR + " at request " + url, rootCause);
        return new ErrorInfo(url, APP_ERROR, rootCause.toString());
    }

    private ErrorInfo warnAndGetErrorInfo(CharSequence url, ErrorType errorType, Exception e) {
        Throwable rootCause = ValidationUtils.getRootCause(e);
        log.warn("{} at request  {}: {}", errorType, url.toString(), rootCause.toString());
        return new ErrorInfo(url, errorType, rootCause.getMessage());
    }

    private ErrorInfo warnAndGetErrorInfo(CharSequence url, ErrorType errorType, List<String> errors) {
        log.warn("{} at request  {}: {}", errorType, url.toString(), errors);
        return new ErrorInfo(url, errorType, errors);
    }
}
