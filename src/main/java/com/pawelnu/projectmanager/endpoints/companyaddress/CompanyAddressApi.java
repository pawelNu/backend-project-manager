package com.pawelnu.projectmanager.endpoints.companyaddress;

import com.pawelnu.projectmanager.dto.SimpleResponse;
import com.pawelnu.projectmanager.endpoints.company.CompanyCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.company.CompanyDTO;
import com.pawelnu.projectmanager.endpoints.company.CompanyEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.company.CompanyFilterRequestDTO;
import com.pawelnu.projectmanager.endpoints.company.CompanyListResponseDTO;
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
public interface CompanyAddressApi {

  // TODO @RequestHeader(required = false change to true, when auth will be done
  @ApiResponse(
      responseCode = "201",
      description = "Created",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = CompanyDTO.class))
      })
  @ResponseErrors
  @PostMapping("")
  @Operation(description = "Add new company address.")
  ResponseEntity<CompanyDTO> createCompanyAddress(
      @Parameter(hidden = true) @RequestHeader(required = false, value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Valid @RequestBody CompanyCreateRequestDTO body);


  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = CompanyDTO.class))
      })
  @ResponseErrors
  @GetMapping("/{id}")
  @Operation(description = "Get company address by id.")
  ResponseEntity<CompanyDTO> getCompanyAddressById(
      @Parameter(hidden = true) @RequestHeader(required = false, value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Parameter() @PathVariable() UUID id);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = CompanyDTO.class))
      })
  @ResponseErrors
  @PutMapping("/{id}")
  @Operation(description = "Edit company address by id.")
  ResponseEntity<CompanyDTO> editCompanyAddressById(
      @Parameter(hidden = true) @RequestHeader(required = false, value = Request.AUTH_HEADER)
          String authorizationHeader,
      @Parameter() @PathVariable() UUID id,
      @Valid @RequestBody CompanyEditRequestDTO body);

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
