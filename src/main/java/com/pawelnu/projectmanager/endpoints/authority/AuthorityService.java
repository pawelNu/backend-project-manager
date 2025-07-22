package com.pawelnu.projectmanager.endpoints.authority;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityDTO;
import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityListResponseDTO;
import com.pawelnu.projectmanager.endpoints.authority.mapper.AuthorityMapper;
import com.pawelnu.projectmanager.endpoints.authority.mapper.AuthorityMapperManual;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.Map;
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
  private final AuthorityMapperManual authorityMapperManual;
  private final ObjectMapper objectMapper;

  public AuthorityDTO create(AuthorityCreateRequestDTO body) {
    AuthorityEntity entity = authorityMapper.toEntity(body);
    String nameFrontend = generateNameFrontend(body.getNameFrontend(), entity.getNameBackend());
    entity.setNameFrontend(nameFrontend);
    AuthorityEntity save = authorityRepository.save(entity);
    return authorityMapper.toDTO(save);
  }

  private String generateNameFrontend(String nameFrontend, String nameBackend) {
    if (nameFrontend != null) {
      return nameFrontend;
    }

    Map<String, String> suffixMap =
        Map.of(
            "DELETE_BY_ID", "delete",
            "GET_BY_ID", "show",
            "GET_LIST", "list",
            "EDIT_BY_ID", "edit");

    for (Map.Entry<String, String> entry : suffixMap.entrySet()) {
      String suffix = entry.getKey();
      String replacement = entry.getValue();
      if (nameBackend.endsWith(suffix)) {
        nameBackend = nameBackend.replace(suffix, replacement);
        break;
      }
    }

    return nameBackend.toLowerCase();
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
    return authorityMapperManual.toDTO(getAuthorityEntityById(id));
  }

  public AuthorityEntity getAuthorityEntityById(UUID id) {
    return authorityQueryRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MSG.AUTHORITY_NOT_FOUND_MSG + id));
  }

  public AuthorityDTO editById(UUID id, AuthorityEditRequestDTO body) {
    AuthorityEntity companyToEdit = getAuthorityEntityById(id);
    authorityMapper.toEntity(body, companyToEdit);
    AuthorityEntity updatedCompany = authorityRepository.save(companyToEdit);
    return authorityMapper.toDTO(updatedCompany);
  }

  public SimpleResponse deleteById(UUID id) {
    AuthorityEntity authorityToDelete = getAuthorityEntityById(id);
    authorityToDelete.setIsDeleted(true);
    AuthorityEntity updatedAuthority = authorityRepository.save(authorityToDelete);
    if (updatedAuthority.getIsDeleted()) {
      return SimpleResponse.builder().message("Deleted authority with id: " + id).build();
    } else {
      return SimpleResponse.builder().message("Cannot delete authority with id: " + id).build();
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
