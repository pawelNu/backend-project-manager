package com.pawelnu.projectmanager.endpoints.category;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

  Optional<CategoryEntity> findByName(String name);

  Optional<CategoryEntity> findByIdAndIsDeletedFalse(UUID id);
}
