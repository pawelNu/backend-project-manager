package com.pawelnu.projectmanager.endpoints.authority;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorityService {

  private final AuthorityRepository authorityRepository;
  private final AuthorityQueryRepository authorityQueryRepository;
  private final AuthorityMapper authorityMapper;
  private final ObjectMapper objectMapper;

  public AuthorityDTO create(AuthorityCreateRequestDTO body) {
    AuthorityEntity entity = authorityMapper.toEntity(body);
    AuthorityEntity save = authorityRepository.save(entity);
    return authorityMapper.toDTO(save);
  }

  public AuthorityListResponseDTO filter(String sort, String range, String filter) {
    PageableParams params = Shared.preparePageableParams(objectMapper, sort, range, filter);

    Page<AuthorityDTO> page =
        authorityQueryRepository.filter(
            params.getFilters(),
            params.getOffset(),
            params.getLimit(),
            params.getSortDir(),
            params.getSortField());
    List<AuthorityDTO> companyDTOs = page.getContent();
    String contentRange = Shared.prepareContentRange(page, params.getOffset(), params.getLimit());
    return AuthorityListResponseDTO.builder().data(companyDTOs).contentRange(contentRange).build();
  }

  public AuthorityDTO getById(UUID id) {
    return authorityQueryRepository
        .findById(id)
        .map(authorityMapper::toDTO)
        .orElseThrow(() -> new NotFoundException(MSG.AUTHORITY_NOT_FOUND_MSG + id));
  }

  public AuthorityDTO editById(UUID id, AuthorityEditRequestDTO body) {
    Optional<AuthorityEntity> companyToEdit = authorityQueryRepository.findById(id);
    if (companyToEdit.isPresent()) {
      AuthorityEntity existingAuthority = companyToEdit.get();
      authorityMapper.toEntity(body, existingAuthority);
      AuthorityEntity updatedCompany = authorityRepository.save(existingAuthority);
      return authorityMapper.toDTO(updatedCompany);
    } else {
      throw new NotFoundException(MSG.AUTHORITY_NOT_FOUND_MSG + id);
    }
  }

  public SimpleResponse deleteById(UUID id) {
    Optional<AuthorityEntity> authorityToDelete = authorityRepository.findByIdAndIsDeletedFalse(id);
    if (authorityToDelete.isPresent()) {
      AuthorityEntity existingAuthority = authorityToDelete.get();
      existingAuthority.setIsDeleted(true);
      AuthorityEntity updatedAuthority = authorityRepository.save(existingAuthority);
      if (updatedAuthority.getIsDeleted()) {
        return SimpleResponse.builder().message("Deleted authority with id: " + id).build();
      } else {
        return SimpleResponse.builder().message("Cannot delete authority with id: " + id).build();
      }
    } else {
      throw new NotFoundException(MSG.AUTHORITY_NOT_FOUND_MSG + id);
    }
  }

  public List<AuthorityEntity> findAllByIdInAndIsDeletedFalse(List<UUID> ids) {
    List<AuthorityEntity> authorities = authorityRepository.findAllByIdInAndIsDeletedFalse(ids);
    if (authorities.isEmpty()) {
      String idsString = ids.stream().map(UUID::toString).collect(Collectors.joining(", "));
      throw new NotFoundException(MSG.AUTHORITIES_NOT_FOUND_MSG + String.join(", ", idsString));
    } else {
      return authorities;
    }
  }
}
