package com.pawelnu.projectmanager.utils;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import com.pawelnu.projectmanager.exception.model.ReactAdminBadRequestError;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.MediaType;

@Target({METHOD, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(
    responseCode = "400",
    description = "Bad Request",
    content = {
      @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ReactAdminBadRequestError.class))
    })
@ApiResponse(
    responseCode = "401",
    description = "Unauthorized",
    content = {
      @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ReactAdminError.class))
    })
@ApiResponse(
    responseCode = "403",
    description = "Forbidden",
    content = {
      @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ReactAdminError.class))
    })
@ApiResponse(
    responseCode = "404",
    description = "Not found",
    content = {
      @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ReactAdminError.class))
    })
@ApiResponse(
    responseCode = "500",
    description = "Internal Server Error",
    content = {
      @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ReactAdminError.class))
    })
public @interface ResponseErrors {}
