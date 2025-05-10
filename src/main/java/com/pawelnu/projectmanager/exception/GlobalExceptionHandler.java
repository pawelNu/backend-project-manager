package com.pawelnu.projectmanager.exception;

import com.pawelnu.projectmanager.exception.model.ReactAdminBadRequestError;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ReactAdminError> handleAuthenticationException(AuthenticationException e) {
    log.error("Stacktrace:", e);
    return new ResponseEntity<>(new ReactAdminError(e.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ReactAdminError> handleAccessDeniedException(AccessDeniedException e) {
    log.error("Stacktrace:", e);
    return new ResponseEntity<>(new ReactAdminError(e.getMessage()), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ReactAdminError> handleBadRequestException(BadRequestException e) {
    log.error("Stacktrace:", e);
    return new ResponseEntity<>(new ReactAdminError(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ReactAdminError> handleNotFoundException(NotFoundException e) {
    log.error("Stacktrace:", e);
    return new ResponseEntity<>(new ReactAdminError(e.getMessage()), HttpStatus.NOT_FOUND);
  }

  //  TODO probably to be removed
  @ExceptionHandler(NotNullOrEmptyException.class)
  public ResponseEntity<String> handleNotNullOrEmptyException(NotNullOrEmptyException e) {
    log.error("Stacktrace:", e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  //  TODO probably to be removed
  @ExceptionHandler(NotFoundSortingFieldException.class)
  public ResponseEntity<String> handleNotFoundPropertyException(NotFoundSortingFieldException e) {
    log.error("Stacktrace:", e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ReactAdminBadRequestError> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error("Stacktrace:", e);

    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
    Map<String, Object> errors = new HashMap<>();

    for (FieldError fieldError : fieldErrors) {
      if (errors.containsKey(fieldError.getField())) {
        String key = fieldError.getField();
        Object value = errors.get(key);
        errors.put(key, value.toString() + ", " + fieldError.getDefaultMessage());
      } else {
        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
      }
    }

    if (!errors.containsKey("root")) {
      errors.put(
          "root",
          Map.of(
              "serverError",
              "Some of the provided values are not valid. Please fix them and retry."));
    }

    ReactAdminBadRequestError response = ReactAdminBadRequestError.builder().errors(errors).build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ReactAdminError> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    log.error("Stacktrace:", e);
    return new ResponseEntity<>(new ReactAdminError(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ReactAdminError> handleAuthorizationDeniedException(
      AuthorizationDeniedException e) {
    log.error("Stacktrace:", e);
    return new ResponseEntity<>(new ReactAdminError(e.getMessage()), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ReactAdminError> handleException(Exception e) {
    log.error("Stacktrace:", e);
    return new ResponseEntity<>(
        new ReactAdminError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
