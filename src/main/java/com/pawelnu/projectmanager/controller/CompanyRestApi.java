// package com.pawelnu.projectmanager.controller;
//
// import com.pawelnu.projectmanager.dto.company.CompanyCreateRequestDTO;
// import com.pawelnu.projectmanager.dto.company.CompanyDTO;
// import com.pawelnu.projectmanager.utils.Consts.Request;
// import com.pawelnu.projectmanager.utils.ResponseErrors;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.media.Content;
// import io.swagger.v3.oas.annotations.media.Schema;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.validation.Valid;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.web.bind.annotation.RequestMapping;
//
// @Tag(name = "Companies")
// @RequestMapping("companies")
// public interface CompanyRestApi {
//
//  @ApiResponse(
//      responseCode = "201",
//      description = "Created",
//      content = {
//        @Content(
//            mediaType = MediaType.APPLICATION_JSON_VALUE,
//            schema = @Schema(implementation = CompanyDTO.class))
//      })
//  @ResponseErrors
//  @PostMapping("")
//  @Operation(description = "Add new company.")
//  ResponseEntity<CompanyDTO> createCompany(
//      @Parameter(hidden = true) @RequestHeader(value = Request.AUTH_HEADER)
//          String authorizationHeader,
//      @Valid @RequestBody CompanyCreateRequestDTO body);
// }
