package com.pawelnu.projectmanager.endpoints.company;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyEditRequestDTO {

  @NotNull
  @Size(min = 3, max = 255, message = "Name must be 3-255 characters")
  private String name;

  @NotNull
  @Pattern(regexp = "^\\d{10}$", message = "NIP must contain exactly 10 digits")
  private String nip;

  @NotNull
  @Pattern(regexp = "^\\d{9}$", message = "REGON must contain exactly 9 digits")
  private String regon;

  @Pattern(regexp = "^(https://).*", message = "URL must start with https://")
  private String website;
}
