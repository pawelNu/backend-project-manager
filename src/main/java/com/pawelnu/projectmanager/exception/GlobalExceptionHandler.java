package com.pawelnu.projectmanager.exception;

import com.pawelnu.projectmanager.exception.model.ConstraintValidationProblem;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.violations.Violation;

@ControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NotNullOrEmptyException.class)
  public ResponseEntity<String> handleNotNullOrEmptyException(NotNullOrEmptyException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(NotFoundSortingFieldException.class)
  public ResponseEntity<String> handleNotFoundPropertyException(NotFoundSortingFieldException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<Problem> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception, NativeWebRequest request) {
    StatusType status = this.defaultConstraintViolationStatus();
    List<Violation> violations =
        this.createViolations(exception.getBindingResult()).stream()
            .sorted(Comparator.comparing(Violation::getField).thenComparing(Violation::getMessage))
            .toList();
    Problem problem = new ConstraintValidationProblem(status, violations);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
  }
}
