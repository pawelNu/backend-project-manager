package com.pawelnu.projectmanager.endpoints.employee;

import com.pawelnu.projectmanager.dto.SimpleResponse;
import com.pawelnu.projectmanager.endpoints.companyaddress.CompanyAddressCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.companyaddress.CompanyAddressDTO;
import com.pawelnu.projectmanager.endpoints.companyaddress.CompanyAddressEditRequestDTO;
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

@Tag(name = "Company Addresses")
@RequestMapping(Path.API_COMPANY_ADDRESSES)
public interface EmployeeApi {

  // TODO @RequestHeader(required = false change to true, when auth will be done
  @ApiResponse(
      responseCode = "201",
      description = "Created",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = EmployeeDTO.class))
      })
  @ResponseErrors
  @PostMapping("")
  @Operation(description = "Add new employee.")
  ResponseEntity<EmployeeDTO> createEmployee(
      @Parameter(hidden = true) @RequestHeader(required = false, value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Valid @RequestBody EmployeeCreateRequestDTO body);

  @Operation(
      description =
          "List company addresses with filtering, sorting and pagination (react-admin format)")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = CompanyAddressDTO.class))))
  @ResponseErrors
  @GetMapping("")
  ResponseEntity<List<CompanyAddressDTO>> getCompanyAddressesList(
      @Parameter(hidden = true) @RequestHeader(required = false, value = Request.AUTH_HEADER)
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
            schema = @Schema(implementation = CompanyAddressDTO.class))
      })
  @ResponseErrors
  @GetMapping("/{id}")
  @Operation(description = "Get company address by id.")
  ResponseEntity<CompanyAddressDTO> getCompanyAddressById(
      @Parameter(hidden = true) @RequestHeader(required = false, value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Parameter() @PathVariable() UUID id);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = CompanyAddressDTO.class))
      })
  @ResponseErrors
  @PutMapping("/{id}")
  @Operation(description = "Edit company address by id.")
  ResponseEntity<CompanyAddressDTO> editCompanyAddressById(
      @Parameter(hidden = true) @RequestHeader(required = false, value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Parameter() @PathVariable() UUID id,
      @Valid @RequestBody CompanyAddressEditRequestDTO body);

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
  @Operation(description = "Delete company address by id.")
  ResponseEntity<SimpleResponse> deleteCompanyAddressById(
      @Parameter(hidden = true) @RequestHeader(required = false, value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Parameter() @PathVariable() UUID id);
}
