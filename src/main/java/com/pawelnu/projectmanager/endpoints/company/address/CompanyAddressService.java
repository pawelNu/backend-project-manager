package com.pawelnu.projectmanager.endpoints.company.address;

import static com.pawelnu.projectmanager.utils.Consts.MSG.COMPANY_NOT_FOUND;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyRepository;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
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
  private final CompanyAddressQueryRepository companyAddressQueryRepository;
  private final CompanyAddressMapper companyAddressMapper;
  private final ObjectMapper objectMapper;

  public CompanyAddressDTO getCompanyById(UUID id) {
    return companyAddressRepository
        .findById(id)
        .map(companyAddressMapper::toDTO)
        .orElseThrow(() -> new NotFoundException(COMPANY_NOT_FOUND + id));
  }

  public CompanyAddressDTO createCompany(CompanyAddressCreateRequestDTO body) {
    Optional<CompanyEntity> company =
        companyRepository.findByIdAndIsDeletedFalse(body.getCompanyId());
    if (company.isPresent()) {
      CompanyAddressEntity addressEntity = companyAddressMapper.toEntity(body);
      addressEntity.setCompany(company.get());
      CompanyAddressEntity savedAddress = companyAddressRepository.save(addressEntity);
      return companyAddressMapper.toDTO(savedAddress);
    } else {
      throw new NotFoundException(MSG.COMPANY_ADDRESS_NOT_FOUND + body.getCompanyId());
    }
  }

  public CompanyAddressDTO editCompanyById(UUID id, CompanyAddressEditRequestDTO body) {
    Optional<CompanyAddressEntity> companyToEdit = companyAddressRepository.findById(id);
    if (companyToEdit.isPresent()) {
      CompanyAddressEntity existingCompany = companyToEdit.get();
      companyAddressMapper.toEntity(body, existingCompany);
      CompanyAddressEntity updatedCompany = companyAddressRepository.save(existingCompany);
      return companyAddressMapper.toDTO(updatedCompany);
    } else {
      throw new NotFoundException(MSG.COMPANY_ADDRESS_NOT_FOUND + id);
    }
  }

  public SimpleResponse deleteCompanyById(UUID id) {
    Optional<CompanyAddressEntity> companyToDelete = companyAddressRepository.findById(id);
    if (companyToDelete.isPresent()) {
      companyAddressRepository.delete(companyToDelete.get());
      return SimpleResponse.builder().message("Deleted company address with id: " + id).build();
    } else {
      throw new NotFoundException(MSG.COMPANY_ADDRESS_NOT_FOUND + id);
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
        companyAddressQueryRepository.filterCompanies(filters, offset, limit, sortDir, sortField);
    List<CompanyAddressDTO> companyDTOs =
        page.getContent().stream().map(companyAddressMapper::toDTO).toList();

    long totalElements = page.getTotalElements();
    long end = Math.min(offset + limit - 1, totalElements - 1);
    String contentRange = Shared.prepareContentRange(offset, end, totalElements);

    return CompanyAddressesListResponseDTO.builder()
        .data(companyDTOs)
        .contentRange(contentRange)
        .build();
  }
}
