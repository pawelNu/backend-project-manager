package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.mapper.CompanyMapper;
import com.pawelnu.projectmanager.mapper.PagingAndSortingMapper;
import com.pawelnu.projectmanager.model.CompanyCreateRequestDTO;
import com.pawelnu.projectmanager.model.CompanyDTO;
import com.pawelnu.projectmanager.model.CompanyListResponseDTO;
import com.pawelnu.projectmanager.model.PagingAndSortingMetadataDTO;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
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
  private final CompanyMapper companyMapper;
  private final PagingAndSortingMapper pageMapper;

  private static final String COMPANY_NOT_FOUND_MSG = "Company not found with id: ";

  public CompanyListResponseDTO getAllCompanies(
      Integer pageNumber, Integer pageSize, String sortedBy, String direction) {
    Pageable pageable = Shared.preparePageable(pageNumber, pageSize, sortedBy, direction);
    Page<CompanyEntity> page = companyRepository.findAll(pageable);
    List<CompanyDTO> companyDTOs = page.getContent().stream().map(companyMapper::toDTO).toList();
    Order pageSort = page.getSort().stream().findFirst().orElse(null);
    PagingAndSortingMetadataDTO paging = pageMapper.toPagingAndSortingMetadataDTO(page, pageSort);
    CompanyListResponseDTO response = new CompanyListResponseDTO();
    response.setData(companyDTOs);
    response.setPaging(paging);
    return response;
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
}
