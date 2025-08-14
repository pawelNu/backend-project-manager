package com.pawelnu.projectmanager.endpoints.ticket;

import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketRowDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

  @Mapping(source = "category.id", target = "categoryValueId")
  @Mapping(source = "category.stringValue", target = "categoryValue")
  @Mapping(source = "priority.id", target = "priorityValueId")
  @Mapping(source = "priority.stringValue", target = "priorityValue")
  @Mapping(source = "project.id", target = "projectId")
  @Mapping(source = "project.name", target = "projectName")
  @Mapping(source = "step.id", target = "projectStepId")
  @Mapping(source = "step.name", target = "projectStepName")
  TicketDTO toDTO(TicketEntity entity);

  //  @Mapping(source = ".", target = "employeeName", qualifiedByName = "concatEmployeeName")
  TicketDTO toDTO(TicketRowDTO row); // TODO fix mapper
}
