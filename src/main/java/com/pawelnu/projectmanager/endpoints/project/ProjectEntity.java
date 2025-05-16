package com.pawelnu.projectmanager.endpoints.project;

import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepEntity;
import com.pawelnu.projectmanager.endpoints.project.type.ProjectTypeEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectEntity extends Auditable {
  @Id @GeneratedValue private UUID id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "project_type_id")
  private ProjectTypeEntity projectType;

  @OneToMany private List<ProjectStepEntity> projectSteps;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private CompanyEntity company;

  @ManyToOne
  @JoinColumn(name = "assigned_employee_id")
  private EmployeeEntity assignedEmployee;
}
