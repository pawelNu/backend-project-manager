package com.pawelnu.projectmanager.endpoints.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueService;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyService;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeService;
import com.pawelnu.projectmanager.endpoints.project.ProjectEntity;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketListResponseDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketRowDTO;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TicketService {

  private final TicketRepository ticketRepository;
  private final TicketQueryRepository ticketQueryRepository;
  private final CategoryValueService categoryValueService;
  private final CompanyService companyService;
  private final EmployeeService employeeService;
  private final TicketMapper ticketMapper;
  private final ObjectMapper objectMapper;

  public TicketDTO create(TicketCreateRequestDTO body) {
    CategoryValueEntity projectCategory =
        categoryValueService.getCategoryValueById(body.getCategoryValueId());
    CompanyEntity companyEntity = companyService.getCompanyEntityById(body.getCompanyId());
    EmployeeEntity employeeEntity =
        employeeService.getEmployeeEntityById(body.getAssignedEmployeeId());
    CategoryValueEntity projectPriority =
        categoryValueService.getCategoryValueById(body.getPriorityValueId());
    TicketEntity ticketEntity =
        TicketEntity.builder()
            .name(body.getName())
            .categoryValue(projectCategory)
            .company(companyEntity)
            .assignedEmployee(employeeEntity)
            .priorityValue(projectPriority)
            .build();
    TicketEntity savedCompany = ticketRepository.save(ticketEntity);
    return ticketMapper.toDTO(savedCompany);
  }

  public TicketDTO getById(UUID id) {
    ProjectEntity companyEntity = getTicketEntityById(id);
    return ticketMapper.toDTO(companyEntity);
  }

  public TicketEntity getTicketEntityById(UUID id) {
    return ticketQueryRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MSG.PROJECT_NOT_FOUND + id));
  }

  public SimpleResponse deleteById(UUID id) {
    TicketEntity projectToDelete = getTicketEntityById(id);
    projectToDelete.setIsDeleted(true);
    TicketEntity projectDeleted = ticketRepository.save(projectToDelete);
    String item = "project";
    if (projectDeleted.getIsDeleted()) {
      return SimpleResponse.builder().message(Shared.deleteMessage(item, id)).build();
    } else {
      return SimpleResponse.builder().message(Shared.cannotDeleteMessage(item, id)).build();
    }
  }

  public TicketDTO editById(UUID id, TicketEditRequestDTO body) {
    TicketEntity projectToEdit = getTicketEntityById(id);
    CategoryValueEntity projectCategory =
        categoryValueService.getCategoryValueById(body.getCategoryValueId());
    CompanyEntity companyEntity = companyService.getCompanyEntityById(body.getCompanyId());
    EmployeeEntity employeeEntity =
        employeeService.getEmployeeEntityById(body.getAssignedEmployeeId());
    CategoryValueEntity projectPriority =
        categoryValueService.getCategoryValueById(body.getPriorityValueId());
    projectToEdit.setName(body.getName());
    projectToEdit.setCategoryValue(projectCategory);
    projectToEdit.setCompany(companyEntity);
    projectToEdit.setAssignedEmployee(employeeEntity);
    projectToEdit.setPriorityValue(projectPriority);
    ProjectEntity updatedProject = ticketRepository.save(projectToEdit);
    return ticketMapper.toDTO(updatedProject);
  }

  public TicketListResponseDTO filter(String sort, String range, String filter) {
    PageableParams params = Shared.preparePageableParams(objectMapper, sort, range, filter);

    List<TicketRowDTO> results = projectStepQueryRepository.getList(params);
    List<TicketDTO> projectDTOs = results.stream().map(projectStepMapper::toDTO).toList();
    String contentRange =
        Shared.prepareContentRange(
            results.isEmpty() ? 0 : results.getFirst().getTotalElements(),
            params.getOffset(),
            params.getLimit());
    return TicketListResponseDTO.builder().data(projectDTOs).contentRange(contentRange).build();
  }
}
