package com.pawelnu.projectmanager.repository;

import com.pawelnu.projectmanager.entity.TicketHierarchyEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketHierarchyRepository extends JpaRepository<TicketHierarchyEntity, UUID> {}
