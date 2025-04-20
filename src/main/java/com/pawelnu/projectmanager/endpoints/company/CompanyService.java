package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.mapper.CompanyMapper;
import com.pawelnu.projectmanager.mapper.PagingAndSortingMapper;
import com.pawelnu.projectmanager.model.CompanyDTO;
import com.pawelnu.projectmanager.model.CompanyListResponseDTO;
import com.pawelnu.projectmanager.model.PagingAndSortingMetadataDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final CompanyMapper companyMapper;
  private final PagingAndSortingMapper pageMapper;

  public CompanyListResponseDTO getAllCompanies(
      Integer pageNumber, Integer pageSize, String sortingField, Boolean isAscendingSorting) {
    Sort sort =
        isAscendingSorting ? Sort.by(sortingField).ascending() : Sort.by(sortingField).descending();
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
    Page<CompanyEntity> page = companyRepository.findAll(pageable);
    List<CompanyDTO> companyDTOs = page.getContent().stream().map(companyMapper::toDTO).toList();
    Order pageSort = page.getSort().stream().findFirst().orElse(Sort.Order.by("name"));
    PagingAndSortingMetadataDTO paging = pageMapper.toPagingAndSortingMetadataDTO(page, pageSort);
    CompanyListResponseDTO response = new CompanyListResponseDTO();
    response.setData(companyDTOs);
    response.setPaging(paging);
    return response;
  }
}
