package com.pawelnu.BackendProjectManager.entity;

import com.pawelnu.BackendProjectManager.enums.ProjectStatus;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity {

  @Id private UUID id = UUID.randomUUID();

  private String name;

  @Enumerated(value = EnumType.STRING)
  private ProjectStatus projectStatus;
}
