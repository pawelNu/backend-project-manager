package com.pawelnu.projectmanager.endpoints.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.dto.PagingAndSortingMetadataDTO;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.mapper.PagingAndSortingMapper;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final CompanyQueryRepository companyQueryRepository;
  private final CompanyMapper companyMapper;
  private final PagingAndSortingMapper pageMapper;
  private final ObjectMapper objectMapper;

  public CompanyListResponseDTO filter(
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

  public CompanyDTO getById(UUID id) {
    return companyQueryRepository
        .findById(id)
        .map(companyMapper::toDTO)
        .orElseThrow(() -> new NotFoundException(MSG.COMPANY_NOT_FOUND + id));
  }

  public CompanyDTO create(CompanyCreateRequestDTO companyCreateRequestDTO) {
    CompanyEntity companyEntity = companyMapper.toEntity(companyCreateRequestDTO);
    //    FIXME incompatible types: com.pawelnu.projectmanager.enums.CompanyStatus cannot be
    // converted to com.pawelnu.projectmanager.endpoints.category.value.CategoryValueEntity
    //    companyEntity.setStatus(CompanyStatus.ACTIVE);
    CompanyEntity savedCompany = companyRepository.save(companyEntity);
    return companyMapper.toDTO(savedCompany);
  }

  public CompanyDTO editById(UUID id, CompanyEditRequestDTO body) {
    Optional<CompanyEntity> companyToEdit = companyRepository.findById(id);
    if (companyToEdit.isPresent()) {
      CompanyEntity existingCompany = companyToEdit.get();
      companyMapper.toEntity(body, existingCompany);
      CompanyEntity updatedCompany = companyRepository.save(existingCompany);
      return companyMapper.toDTO(updatedCompany);
    } else {
      throw new NotFoundException(MSG.COMPANY_NOT_FOUND + id);
    }
  }

  public SimpleResponse deleteById(UUID id) {
    Optional<CompanyEntity> companyToDelete = companyRepository.findByIdAndIsDeletedFalse(id);
    if (companyToDelete.isPresent()) {
      CompanyEntity existingCompany = companyToDelete.get();
      existingCompany.setIsDeleted(true);
      CompanyEntity updatedCompany = companyRepository.save(existingCompany);
      if (updatedCompany.getIsDeleted()) {
        return SimpleResponse.builder().message("Deleted company with id: " + id).build();
      } else {
        return SimpleResponse.builder().message("Cannot delete company with id: " + id).build();
      }
    } else {
      throw new NotFoundException(MSG.COMPANY_NOT_FOUND + id);
    }
  }

  public CompanyListResponseDTO filter(CompanyFilterRequestDTO body) {
    Page<CompanyEntity> filteredCompanies = companyQueryRepository.filter(body);
    List<CompanyDTO> companyDTOs =
        filteredCompanies.getContent().stream().map(companyMapper::toDTO).toList();
    Order pageSort = filteredCompanies.getSort().stream().findFirst().orElse(null);
    PagingAndSortingMetadataDTO paging =
        pageMapper.toPagingAndSortingMetadataDTO(filteredCompanies, pageSort);
    return CompanyListResponseDTO.builder().data(companyDTOs).page(paging).build();
  }

  public CompanyListResponseDTO2 filter(String sort, String range, String filter) {
    PageableParams params = Shared.preparePageableParams(objectMapper, sort, range, filter);

    Page<CompanySimpleDTO> page =
        companyQueryRepository.filter(
            params.getFilters(),
            params.getOffset(),
            params.getLimit(),
            params.getSortDir(),
            params.getSortField());
    List<CompanySimpleDTO> companyDTOs = page.getContent();
    String contentRange = Shared.prepareContentRange(page, params.getOffset(), params.getLimit());
    return CompanyListResponseDTO2.builder().data(companyDTOs).contentRange(contentRange).build();
  }
}
