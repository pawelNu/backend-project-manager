package com.pawelnu.projectmanager.endpoints.authority;

import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.Request;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.ResponseErrors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Authorities")
@RequestMapping(Path.API_AUTHORITIES)
public interface AuthorityApi {

  @ApiResponse(
      responseCode = "201",
      description = "Created",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AuthorityDTO.class))
      })
  @ResponseErrors
  @PostMapping("")
  @Operation(description = "Add new authority.")
  ResponseEntity<AuthorityDTO> createAuthority(
      @Parameter(hidden = true) @RequestHeader(value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Valid @RequestBody AuthorityCreateRequestDTO body);

  @Operation(
      description = "List authorities with filtering, sorting and pagination (react-admin format)")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = AuthorityDTO.class))))
  @ResponseErrors
  @GetMapping("")
  ResponseEntity<List<AuthorityDTO>> getAuthorityList(
      @Parameter(hidden = true) @RequestHeader(value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Parameter(description = "Sort as JSON string, e.g. [\"title\",\"ASC\"]")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "Range as JSON string, e.g. [0, 24]") @RequestParam(required = false)
          String range,
      @Parameter(description = "Filter as JSON string, e.g. {\"title\":\"bar\"}")
          @RequestParam(required = false)
          String filter);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AuthorityDTO.class))
      })
  @ResponseErrors
  @GetMapping("/{id}")
  @Operation(description = "Get authority by id.")
  ResponseEntity<AuthorityDTO> getAuthorityById(
      @Parameter(hidden = true) @RequestHeader(value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Parameter() @PathVariable() UUID id);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AuthorityDTO.class))
      })
  @ResponseErrors
  @PutMapping("/{id}")
  @Operation(description = "Edit authority by id.")
  ResponseEntity<AuthorityDTO> editAuthorityById(
      @Parameter(hidden = true) @RequestHeader(value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Parameter() @PathVariable() UUID id,
      @Valid @RequestBody AuthorityEditRequestDTO body);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = SimpleResponse.class))
      })
  @ResponseErrors
  @DeleteMapping("/{id}")
  @Operation(description = "Delete authority by id.")
  ResponseEntity<SimpleResponse> deleteAuthorityById(
      @Parameter(hidden = true) @RequestHeader(value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Parameter() @PathVariable() UUID id);

  @ApiResponse(
      responseCode = "201",
      description = "Created",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AddAuthorityToUserResponseDTO.class))
      })
  @ResponseErrors
  @PostMapping("/add-authority-to-user")
  @Operation(description = "Add authority to user.")
  ResponseEntity<AddAuthorityToUserResponseDTO> addAuthorityToUser(
      @Parameter(hidden = true) @RequestHeader(value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Valid @RequestBody AddAuthorityToUserRequestDTO body);
}
