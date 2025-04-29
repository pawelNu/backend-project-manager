package com.pawelnu.projectmanager.endpoints.companyaddress;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.dto.SimpleResponse;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyRepository;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.mapper.PagingAndSortingMapper;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyAddressService {

  private final CompanyAddressRepository companyAddressRepository;
  private final CompanyRepository companyRepository;
  private final CompanyAddressRepositoryQuery companyAddressRepositoryQuery;
  private final CompanyAddressMapper companyAddressMapper;
  private final PagingAndSortingMapper pageMapper;
  private final ObjectMapper objectMapper;

  private static final String COMPANY_ADDRESS_NOT_FOUND_MSG = "Company address not found with id: ";

  public CompanyAddressDTO getCompanyById(UUID id) {
    return companyAddressRepository
        .findById(id)
        .map(companyAddressMapper::toDTO)
        .orElseThrow(() -> new NotFoundException(COMPANY_ADDRESS_NOT_FOUND_MSG + id));
  }

  public CompanyAddressDTO createCompany(CompanyAddressCreateRequestDTO body) {
    CompanyEntity companyRef = companyRepository.getReferenceById(body.getCompanyId());
    CompanyAddressEntity addressEntity = companyAddressMapper.toEntity(body);
    addressEntity.setCompany(companyRef);
    CompanyAddressEntity savedCompany = companyAddressRepository.save(addressEntity);
    return companyAddressMapper.toDTO(savedCompany);
  }

  public CompanyAddressDTO editCompanyById(UUID id, CompanyAddressEditRequestDTO body) {
    Optional<CompanyAddressEntity> companyToEdit = companyAddressRepository.findById(id);
    if (companyToEdit.isPresent()) {
      CompanyAddressEntity existingCompany = companyToEdit.get();
      companyAddressMapper.toEntity(body, existingCompany);
      CompanyAddressEntity updatedCompany = companyAddressRepository.save(existingCompany);
      return companyAddressMapper.toDTO(updatedCompany);
    } else {
      throw new NotFoundException(COMPANY_ADDRESS_NOT_FOUND_MSG + id);
    }
  }

  public SimpleResponse deleteCompanyById(UUID id) {
    Optional<CompanyAddressEntity> companyToDelete = companyAddressRepository.findById(id);
    if (companyToDelete.isPresent()) {
      companyAddressRepository.delete(companyToDelete.get());
      return SimpleResponse.builder().message("Deleted company address with id: " + id).build();
    } else {
      throw new NotFoundException(COMPANY_ADDRESS_NOT_FOUND_MSG + id);
    }
  }

  public CompanyAddressesListResponseDTO filterCompanies(String sort, String range, String filter) {

    List<String> sortList = Shared.parseJsonList(objectMapper, sort);
    String sortField = !sortList.isEmpty() ? sortList.get(0) : "city";
    String sortDir = sortList.size() > 1 ? sortList.get(1) : "ASC";

    List<Integer> rangeList = Shared.parseJsonListInt(objectMapper, range);
    int offset = !rangeList.isEmpty() ? rangeList.get(0) : 0;
    int limit = rangeList.size() > 1 ? rangeList.get(1) - rangeList.get(0) + 1 : 25;

    Map<String, String> filters = Shared.parseJsonMap(objectMapper, filter);

    Page<CompanyAddressEntity> page =
        companyAddressRepositoryQuery.filterCompanies(filters, offset, limit, sortDir, sortField);
    List<CompanyAddressDTO> companyDTOs =
        page.getContent().stream().map(companyAddressMapper::toDTO).toList();

    long totalElements = page.getTotalElements();
    long end = Math.min(offset + limit - 1, totalElements - 1);

    return CompanyAddressesListResponseDTO.builder()
        .data(companyDTOs)
        .totalElements(totalElements)
        .start(offset)
        .end(end)
        .build();
  }
}
