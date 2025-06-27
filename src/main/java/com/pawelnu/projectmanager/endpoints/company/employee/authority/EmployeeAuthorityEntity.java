package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import com.pawelnu.projectmanager.endpoints.authority.AuthorityEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.entity.Auditable;
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
public class EmployeeAuthorityEntity extends Auditable {

  @Id @GeneratedValue private UUID id;

  @ManyToOne
  @JoinColumn(name = "authority_id")
  private AuthorityEntity authority;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  private EmployeeEntity employee;
}
