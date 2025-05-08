package com.pawelnu.projectmanager.endpoints.authority;

import com.pawelnu.projectmanager.endpoints.employee.EmployeeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_authorities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeAuthorityEntity {

  @Id @GeneratedValue private UUID id;

  //  @NotNull private String authority;
  @ManyToOne
  @JoinColumn(name = "employee_id")
  private EmployeeEntity employee;

  @ManyToOne
  @JoinColumn(name = "authority_id")
  private AuthorityEntity authority;
}
