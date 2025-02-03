package com.pawelnu.BackendProjectManager.utils;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import com.pawelnu.BackendProjectManager.exception.model.BadRequestModel;
import com.pawelnu.BackendProjectManager.exception.model.InternalServerErrorModel;
import com.pawelnu.BackendProjectManager.exception.model.UnauthorizedModel;
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
          schema = @Schema(implementation = BadRequestModel.class))
    })
@ApiResponse(
    responseCode = "401",
    description = "Unauthorized",
    content = {
      @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = UnauthorizedModel.class))
    })
@ApiResponse(
    responseCode = "500",
    description = "Internal Server Error",
    content = {
      @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = InternalServerErrorModel.class))
    })
public @interface ResponseErrors {}
