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

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import java.util.List;

import static com.train4game.munoon.utils.exceptions.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
public class ExceptionInfoHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo notFoundHandler(HttpServletRequest req, NotFoundException e) {
        return warnAndGetErrorInfo(req.getRequestURL(), DATA_NOT_FOUND, e);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String errorMessage = e.getRootCause().getMessage().toLowerCase();
        String url = req.getRequestURL().toString();

        if (errorMessage.contains("users_unique_email_idx")) {
            log.warn("Database error users_unique_email_idx");
            return new ErrorInfo(url, DATA_ERROR, "User with this email already exists");
        } else if (errorMessage.contains("restaurants_unique_name_idx")) {
            log.warn("Database error restaurants_unique_name_idx");
            return new ErrorInfo(url, DATA_ERROR, "Restaurant with this name already exists");
        } else if (errorMessage.contains("meals_unique_name_idx")) {
            log.warn("Database error meals_unique_name_idx");
            return new ErrorInfo(url, DATA_ERROR, "Meal with this name already exists");
        } else if (errorMessage.contains("users_votes_unique_date_idx")) {
            log.warn("Database error users_votes_unique_date_idx");
            return new ErrorInfo(url, DATA_ERROR, "You have already voted today");
        }

        return warnAndGetErrorInfo(url, DATA_ERROR, e);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return warnAndGetErrorInfo(req.getRequestURL(), VALIDATION_ERROR, e);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorInfo beanValidationExceptionHandler(HttpServletRequest req, BindException e) {
        List<String> errors = ValidationUtils.getErrorsFieldList(e.getFieldErrors());
        return warnAndGetErrorInfo(req.getRequestURL(), VALIDATION_ERROR, errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
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
        return new ErrorInfo(url, errorType, rootCause.toString());
    }

    private ErrorInfo warnAndGetErrorInfo(CharSequence url, ErrorType errorType, List<String> errors) {
        log.warn("{} at request  {}: {}", errorType, url.toString(), errors);
        return new ErrorInfo(url, errorType, errors);
    }
}
