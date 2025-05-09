package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.endpoints.companyaddress.CompanyAddressEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import com.pawelnu.projectmanager.enums.CompanyStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

  private String name;
  private String nip;
  private String regon;
  private String website;

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
