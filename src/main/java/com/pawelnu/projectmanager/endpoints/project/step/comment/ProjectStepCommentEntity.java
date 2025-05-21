package com.pawelnu.projectmanager.endpoints.project.step.comment;

import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_step_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectStepCommentEntity extends Auditable { // TODO adjust entity to db schema
  @Id @GeneratedValue UUID id;
  private String comment;

  @ManyToOne
  @JoinColumn(name = "project_step_id")
  private ProjectStepEntity step;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  private EmployeeEntity employee;
}
