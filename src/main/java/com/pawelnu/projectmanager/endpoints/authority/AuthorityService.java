package com.pawelnu.projectmanager.endpoints.authority;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorityService {

  private final AuthorityRepository authorityRepository;
  private final ObjectMapper objectMapper;
  private static final String AUTHORITY_NOT_FOUND_MSG = "Authority not found with id: ";

}
