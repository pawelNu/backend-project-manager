package com.pawelnu.projectmanager.repository;

import com.pawelnu.projectmanager.entity.PersonEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<PersonEntity, UUID> {}
