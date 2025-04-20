// package com.pawelnu.projectmanager.controller.impl;
//
// import com.pawelnu.projectmanager.controller.CompanyRestApi;
// import com.pawelnu.projectmanager.dto.company.CompanyCreateRequestDTO;
// import com.pawelnu.projectmanager.dto.company.CompanyDTO;
// import com.pawelnu.projectmanager.service.CompanyService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.RestController;
//
// @RestController
// @RequiredArgsConstructor
// public class CompanyController implements CompanyRestApi {
//  private final CompanyService service;
//
//  @Override
//  public ResponseEntity<CompanyDTO> createCompany(
//      String authorizationHeader, CompanyCreateRequestDTO body) {
//    //    TODO add authentication
//    //    TODO add authorization
//    //    TODO add request logging
//
//    CompanyDTO createdCompany = service.createCompany(body);
//
//    return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
//  }
//
//  //  TODO get company by id
//  //  TODO post company
//  //  TODO put company by id
//  //  TODO delete company by id
//  //  TODO add custom filters
// }
