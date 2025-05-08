package com.pawelnu.projectmanager.endpoints.authority;

import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authorities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorityEntity extends Auditable {

  @Id @GeneratedValue private UUID id;
  @NotNull private String name;

  @ManyToMany(mappedBy = "authority")
  private Set<EmployeeAuthorityEntity> employees = new HashSet<>();
}
