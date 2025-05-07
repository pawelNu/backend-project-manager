package com.pawelnu.projectmanager.endpoints.authority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorityService {

  private final AuthorityRepository authorityRepository;
  private final AuthorityMapper authorityMapper;
  private static final String AUTHORITY_NOT_FOUND_MSG = "Authority not found with id: ";

  public AuthorityDTO createAuthority(AuthorityCreateRequestDTO body) {
    AuthorityEntity entity = authorityMapper.toEntity(body);
    AuthorityEntity save = authorityRepository.save(entity);
    return authorityMapper.toDTO(save);
  }
}
