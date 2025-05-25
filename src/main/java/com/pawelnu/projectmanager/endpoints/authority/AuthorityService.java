package com.pawelnu.projectmanager.endpoints.authority;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.Map;
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
  public static final String AUTHORITY_NOT_FOUND_MSG = "Authority not found with id: ";

  public AuthorityDTO create(AuthorityCreateRequestDTO body) {
    AuthorityEntity entity = authorityMapper.toEntity(body);
    AuthorityEntity save = authorityRepository.save(entity);
    return authorityMapper.toDTO(save);
  }

  public AuthorityListResponseDTO filter(String sort, String range, String filter) {
    List<String> sortList = Shared.parseJsonList(objectMapper, sort);
    String sortField = sortList.isEmpty() ? "name" : sortList.get(0);
    String sortDir = sortList.size() > 1 ? sortList.get(1) : "ASC";

    List<Integer> rangeList = Shared.parseJsonListInt(objectMapper, range);
    int offset = !rangeList.isEmpty() ? rangeList.get(0) : 0;
    int limit = rangeList.size() > 1 ? rangeList.get(1) - rangeList.get(0) + 1 : 25;

    Map<String, String> filters = Shared.parseJsonMap(objectMapper, filter);

    Page<AuthorityDTO> page =
        authorityQueryRepository.filter(filters, offset, limit, sortDir, sortField);
    List<AuthorityDTO> companyDTOs = page.getContent();

    long totalElements = page.getTotalElements();
    long end = Math.min(offset + limit - 1, totalElements - 1);

    String contentRange = Shared.prepareContentRange(offset, end, totalElements);
    return AuthorityListResponseDTO.builder().data(companyDTOs).contentRange(contentRange).build();
  }
}
