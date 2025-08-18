package com.pawelnu.projectmanager.endpoints.ticket.history;

import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketHistoryDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketHistoryRowDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TicketHistoryMapper {

  @Mapping(source = "ticket.id", target = "ticketId")
  @Mapping(source = "ticket.number", target = "ticketNumber")
  @Mapping(source = "ticket.title", target = "ticketTitle")
  @Mapping(source = "fromStatus.id", target = "fromStatusId")
  @Mapping(source = "fromStatus.stringValue", target = "fromStatusName")
  @Mapping(source = "toStatus.id", target = "toStatusId")
  @Mapping(source = "toStatus.stringValue", target = "toStatusName")
  @Mapping(source = "fromEmployee.id", target = "fromEmployeeId")
  @Mapping(source = "fromEmployee", target = "fromEmployeeName")
  @Mapping(source = "toEmployee.id", target = "toEmployeeId")
  @Mapping(source = "toEmployee", target = "toEmployeeName")
  TicketHistoryDTO toDTO(TicketHistoryEntity entity);

  @Mapping(source = ".", target = "fromEmployeeName", qualifiedByName = "concatFromEmployeeName")
  @Mapping(source = ".", target = "toEmployeeName", qualifiedByName = "concatToEmployeeName")
  TicketHistoryDTO toDTO(TicketHistoryRowDTO row);

  default String employeeToString(EmployeeEntity entity) {
    if (entity == null) {
      return null;
    }
    return entity.getFirstName() + " " + entity.getLastName();
  }

  @Named("concatFromEmployeeName")
  default String concatFromEmployeeName(TicketHistoryRowDTO row) {
    if (row == null) {
      return null;
    }
    return row.getFromEmployeeFirstName() + " " + row.getFromEmployeeLastName();
  }

  @Named("concatToEmployeeName")
  default String concatToEmployeeName(TicketHistoryRowDTO row) {
    if (row == null) {
      return null;
    }
    return row.getToEmployeeFirstName() + " " + row.getToEmployeeLastName();
  }
}
