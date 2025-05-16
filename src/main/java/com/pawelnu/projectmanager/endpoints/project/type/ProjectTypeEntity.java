package com.pawelnu.projectmanager.endpoints.project.type;

import com.pawelnu.projectmanager.endpoints.project.ProjectEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectTypeEntity extends Auditable {
  @Id @GeneratedValue private UUID id;
  private String name;

  @OneToMany private List<ProjectEntity> projects;
}
