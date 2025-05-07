package com.pawelnu.projectmanager.endpoints.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.dto.PagingAndSortingMetadataDTO;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.mapper.PagingAndSortingMapper;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final CompanyRepositoryQuery companyRepositoryQuery;
  private final CompanyMapper companyMapper;
  private final PagingAndSortingMapper pageMapper;
  private final ObjectMapper objectMapper;

  private static final String COMPANY_NOT_FOUND_MSG = "Company not found with id: ";

  public CompanyListResponseDTO getAllCompanies(
      Integer pageNumber, Integer pageSize, String sortedBy, String direction) {
    Pageable pageable = Shared.preparePageable(pageNumber, pageSize, sortedBy, direction);
    Page<CompanyEntity> companiesPage = companyRepository.findAll(pageable);
    List<CompanyDTO> companyDTOs =
        companiesPage.getContent().stream().map(companyMapper::toDTO).toList();
    Order pageSort = companiesPage.getSort().stream().findFirst().orElse(null);
    PagingAndSortingMetadataDTO paging =
        pageMapper.toPagingAndSortingMetadataDTO(companiesPage, pageSort);
    return CompanyListResponseDTO.builder().data(companyDTOs).page(paging).build();
  }

  public CompanyDTO getCompanyById(UUID id) {
    return companyRepositoryQuery
        .findById(id)
        .map(companyMapper::toDTO)
        .orElseThrow(() -> new NotFoundException(COMPANY_NOT_FOUND_MSG + id));
  }

  public CompanyDTO createCompany(CompanyCreateRequestDTO companyCreateRequestDTO) {
    CompanyEntity companyEntity = companyMapper.toEntity(companyCreateRequestDTO);
    CompanyEntity savedCompany = companyRepository.save(companyEntity);
    return companyMapper.toDTO(savedCompany);
  }

  public CompanyDTO editCompanyById(UUID id, CompanyEditRequestDTO body) {
    Optional<CompanyEntity> companyToEdit = companyRepository.findById(id);
    if (companyToEdit.isPresent()) {
      CompanyEntity existingCompany = companyToEdit.get();
      companyMapper.toEntity(body, existingCompany);
      CompanyEntity updatedCompany = companyRepository.save(existingCompany);
      return companyMapper.toDTO(updatedCompany);
    } else {
      throw new NotFoundException(COMPANY_NOT_FOUND_MSG + id);
    }
  }

  public SimpleResponse deleteCompanyById(UUID id) {
    Optional<CompanyEntity> companyToDelete = companyRepository.findById(id);
    if (companyToDelete.isPresent()) {
      companyRepository.delete(companyToDelete.get());
      return SimpleResponse.builder().message("Deleted company with id: " + id).build();
    } else {
      throw new NotFoundException(COMPANY_NOT_FOUND_MSG + id);
    }
  }

  public CompanyListResponseDTO filterCompanies(CompanyFilterRequestDTO body) {
    Page<CompanyEntity> filteredCompanies = companyRepositoryQuery.filterCompanies(body);
    List<CompanyDTO> companyDTOs =
        filteredCompanies.getContent().stream().map(companyMapper::toDTO).toList();
    Order pageSort = filteredCompanies.getSort().stream().findFirst().orElse(null);
    PagingAndSortingMetadataDTO paging =
        pageMapper.toPagingAndSortingMetadataDTO(filteredCompanies, pageSort);
    return CompanyListResponseDTO.builder().data(companyDTOs).page(paging).build();
  }

  public CompanyListResponseDTO2 filterCompanies(String sort, String range, String filter) {

    List<String> sortList = Shared.parseJsonList(objectMapper, sort);
    String sortField = sortList.isEmpty() ? "name" : sortList.get(0);
    String sortDir = sortList.size() > 1 ? sortList.get(1) : "ASC";

    List<Integer> rangeList = Shared.parseJsonListInt(objectMapper, range);
    int offset = !rangeList.isEmpty() ? rangeList.get(0) : 0;
    int limit = rangeList.size() > 1 ? rangeList.get(1) - rangeList.get(0) + 1 : 25;

    Map<String, String> filters = Shared.parseJsonMap(objectMapper, filter);

    Page<CompanyEntity> page =
        companyRepositoryQuery.filterCompanies(filters, offset, limit, sortDir, sortField);
    List<CompanySimpleDTO> companyDTOs =
        page.getContent().stream().map(companyMapper::toSimpleDTO).toList();

    long totalElements = page.getTotalElements();
    long end = Math.min(offset + limit - 1, totalElements - 1);

    return CompanyListResponseDTO2.builder()
        .data(companyDTOs)
        .totalElements(totalElements)
        .start(offset)
        .end(end)
        .build();
  }
}
