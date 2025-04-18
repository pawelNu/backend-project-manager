package com.pawelnu.BackendProjectManager.repository;

import com.pawelnu.BackendProjectManager.entity.PersonEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<PersonEntity, UUID> {}
