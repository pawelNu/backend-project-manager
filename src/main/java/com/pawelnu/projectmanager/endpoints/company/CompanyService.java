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
      Integer pageNumber, Integer pageSize, String sortedBy, String direction) {
    Pageable pageable = preparePageable(pageNumber, pageSize, sortedBy, direction);
    Page<CompanyEntity> page = companyRepository.findAll(pageable);
    List<CompanyDTO> companyDTOs = page.getContent().stream().map(companyMapper::toDTO).toList();
    Order pageSort = page.getSort().stream().findFirst().orElse(null);
    PagingAndSortingMetadataDTO paging = pageMapper.toPagingAndSortingMetadataDTO(page, pageSort);
    CompanyListResponseDTO response = new CompanyListResponseDTO();
    response.setData(companyDTOs);
    response.setPaging(paging);
    return response;
  }

  private static Pageable preparePageable(Integer pageNumber, Integer pageSize, String sortedBy,
      String direction) {
    if (pageNumber == null) {
      pageNumber = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }
    Sort sort = getSort(sortedBy, direction);
    return PageRequest.of(pageNumber, pageSize, sort);
  }

  private static Sort getSort(String sortedBy, String direction) {
    if (direction != null && sortedBy != null) {
      return switch (direction.toLowerCase()) {
        case "asc" -> Sort.by(sortedBy).ascending();
        case "desc" -> Sort.by(sortedBy).descending();
        default -> Sort.unsorted();
      };
    }
    return Sort.unsorted();
  }

}
