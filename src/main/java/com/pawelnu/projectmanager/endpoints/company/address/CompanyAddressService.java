package com.pawelnu.projectmanager.endpoints.company.address;

import static com.pawelnu.projectmanager.utils.Consts.MSG.COMPANY_ADDRESS_NOT_FOUND;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyRepository;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
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

  public CompanyAddressDTO getById(UUID id) {
    return companyAddressQueryRepository
        .findById(id)
        .map(companyAddressMapper::toDTO)
        .orElseThrow(() -> new NotFoundException(COMPANY_ADDRESS_NOT_FOUND + id));
  }

  public CompanyAddressDTO create(CompanyAddressCreateRequestDTO body) {
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

  public CompanyAddressDTO editById(UUID id, CompanyAddressEditRequestDTO body) {
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

  public SimpleResponse deleteById(UUID id) {
    Optional<CompanyAddressEntity> companyToDelete = companyAddressQueryRepository.findById(id);
    if (companyToDelete.isPresent()) {
      companyAddressRepository.delete(companyToDelete.get());
      return SimpleResponse.builder().message("Deleted company address with id: " + id).build();
    } else {
      throw new NotFoundException(MSG.COMPANY_ADDRESS_NOT_FOUND + id);
    }
  }

  public CompanyAddressesListResponseDTO filter(String sort, String range, String filter) {
    PageableParams params = Shared.preparePageableParams(objectMapper, "city", sort, range, filter);

    Page<CompanyAddressEntity> page =
        companyAddressQueryRepository.filter(
            params.getFilters(),
            params.getOffset(),
            params.getLimit(),
            params.getSortDir(),
            params.getSortField());
    List<CompanyAddressDTO> companyDTOs =
        page.getContent().stream().map(companyAddressMapper::toDTO).toList();
    String contentRange = Shared.prepareContentRange(page, params.getOffset(), params.getLimit());
    return CompanyAddressesListResponseDTO.builder()
        .data(companyDTOs)
        .contentRange(contentRange)
        .build();
  }
}
