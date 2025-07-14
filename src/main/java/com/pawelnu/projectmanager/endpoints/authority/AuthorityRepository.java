package com.pawelnu.projectmanager.endpoints.authority;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, UUID> {

  Optional<AuthorityEntity> findByNameBackend(String name);

  Optional<AuthorityEntity> findByIdAndIsDeletedFalse(UUID id);

  List<AuthorityEntity> findAllByIdInAndIsDeletedFalse(@NotNull List<UUID> authorityIds);
}
