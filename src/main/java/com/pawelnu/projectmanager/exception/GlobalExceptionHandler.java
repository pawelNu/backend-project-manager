package com.pawelnu.projectmanager.exception;

import com.pawelnu.projectmanager.exception.model.ReactAdminBadRequestError;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ReactAdminError> handleNotFoundException(NotFoundException e) {
    return new ResponseEntity<>(new ReactAdminError(e.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NotNullOrEmptyException.class)
  public ResponseEntity<String> handleNotNullOrEmptyException(NotNullOrEmptyException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(NotFoundSortingFieldException.class)
  public ResponseEntity<String> handleNotFoundPropertyException(NotFoundSortingFieldException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ReactAdminBadRequestError> handleValidationErrors(
      MethodArgumentNotValidException exception) {

    List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ReactAdminError> handleException(Exception e) {
    return new ResponseEntity<>(
        new ReactAdminError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
