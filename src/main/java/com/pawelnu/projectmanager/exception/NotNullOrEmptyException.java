package com.pawelnu.projectmanager.exception;

public class NotNullOrEmptyException extends RuntimeException {

  public NotNullOrEmptyException(String message) {
    super(message);
  }
}
