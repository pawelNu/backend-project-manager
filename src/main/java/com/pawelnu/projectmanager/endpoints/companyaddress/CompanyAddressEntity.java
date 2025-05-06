package com.pawelnu.projectmanager.endpoints.companyaddress;

import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyAddressEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "company_id")
  @NotNull
  private CompanyEntity company;

  @NotNull private String street;
  @NotNull private String streetNumber;
  @NotNull private String city;
  @NotNull private String zipCode;
  @NotNull private String country;
  @NotNull private String phoneNumber;
  @NotNull private String emailAddress;
  @NotNull private String addressType;
}
