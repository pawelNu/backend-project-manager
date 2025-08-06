package com.pawelnu.projectmanager.endpoints.project.step.comment.dto;

import com.pawelnu.projectmanager.entity.RowPagination;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ProjectStepCommentRowDTO extends RowPagination {
  private UUID id;
  private String comment;
  private UUID stepId;
  private String stepName;
  private UUID employeeId;
  private String employeeFirstName;
  private String employeeLastName;
}
