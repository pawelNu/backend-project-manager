package com.pawelnu.projectmanager.endpoints.company.address;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanyAddressEditRequestDTO {

  @NotNull private String street;
  @NotNull private String streetNumber;
  @NotNull private String city;
  @NotNull private String zipCode;
  @NotNull private String country;
  @NotNull private String phoneNumber;
  @NotNull @Email private String emailAddress;
  @NotNull private String addressType;
}
