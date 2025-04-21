package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.dto.PagingAndSortingMetadataDTO;
import com.pawelnu.projectmanager.dto.SimpleResponse;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.mapper.PagingAndSortingMapper;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final CompanyRepositoryQuery companyRepositoryQuery;
  private final CompanyMapper companyMapper;
  private final PagingAndSortingMapper pageMapper;

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
    return companyRepository
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
}
