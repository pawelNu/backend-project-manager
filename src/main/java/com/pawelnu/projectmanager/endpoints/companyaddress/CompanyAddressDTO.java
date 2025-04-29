package com.pawelnu.projectmanager.endpoints.companyaddress;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyAddressDTO {

  private UUID id;
  private String companyName;
  private String street;
  private String streetNumber;
  private String city;
  private String zipCode;
  private String country;
  private String phoneNumber;
  private String emailAddress;
  private String addressType;
}
