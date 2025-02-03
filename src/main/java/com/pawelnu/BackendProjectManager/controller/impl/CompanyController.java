package com.pawelnu.BackendProjectManager.controller.impl;

import com.pawelnu.BackendProjectManager.controller.CompanyRestApi;
import com.pawelnu.BackendProjectManager.dto.company.CompanyCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.company.CompanyDTO;
import com.pawelnu.BackendProjectManager.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController implements CompanyRestApi {
  private final CompanyService service;

  @Override
  public ResponseEntity<CompanyDTO> createCompany(
      String authorizationHeader, CompanyCreateRequestDTO body) {
//    TODO add authentication
//    TODO add authorization
//    TODO add request logging
    CompanyDTO createdCompany = service.createCompany(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
  }
}
