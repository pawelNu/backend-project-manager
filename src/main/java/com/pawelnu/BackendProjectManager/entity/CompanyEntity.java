package com.pawelnu.BackendProjectManager.entity;

import com.pawelnu.BackendProjectManager.enums.CompanyStatus;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String name;

  @Enumerated(value = EnumType.STRING)
  private CompanyStatus status;

  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
  private List<ProjectEntity> projects;

  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
  private List<PersonEntity> companyEmployees;
}
