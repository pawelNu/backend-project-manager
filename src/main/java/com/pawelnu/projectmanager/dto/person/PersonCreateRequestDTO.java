package com.pawelnu.projectmanager.dto.person;

import lombok.Data;

@Data
public class PersonCreateRequestDTO {

  private String firstName;
  private String lastName;
  private String role;
}
