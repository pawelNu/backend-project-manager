package com.pawelnu.BackendProjectManager.entity;

import com.pawelnu.BackendProjectManager.enums.CompanyStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyEntity extends Auditable {

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
