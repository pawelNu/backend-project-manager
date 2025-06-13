package com.pawelnu.projectmanager.endpoints.category.value;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryValueRepository extends JpaRepository<CategoryValueEntity, UUID> {
  Optional<CategoryValueEntity> findByIdAndIsDeletedFalse(UUID id);
}
