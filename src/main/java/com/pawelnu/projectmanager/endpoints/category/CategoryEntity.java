package com.pawelnu.projectmanager.endpoints.category;

import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity extends Auditable {

  @Id @GeneratedValue private UUID id;
  private String name;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CategoryValueEntity> values;
}
