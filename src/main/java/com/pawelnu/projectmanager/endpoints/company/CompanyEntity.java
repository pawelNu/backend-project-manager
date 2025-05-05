package com.pawelnu.projectmanager.endpoints.company;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pawelnu.projectmanager.endpoints.companyaddress.CompanyAddressEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import com.pawelnu.projectmanager.enums.CompanyStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
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

  @NotNull private String name;

  @NotNull
  @Size(min = 10, max = 10)
  private String nip;

  @NotNull
  @Size(min = 9, max = 9)
  private String regon;

  @NotNull private String website;

  @Enumerated(value = EnumType.STRING)
  private CompanyStatus status;

  @OneToMany(mappedBy = "company")
  private List<CompanyAddressEntity> addresses = new ArrayList<>();

  //  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
  //  private List<ProjectEntity> projects;
  //
  //  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
  //  private List<PersonEntity> companyEmployees;
}
