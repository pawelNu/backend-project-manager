package com.pawelnu.projectmanager.endpoints.ticket;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {
  Optional<TicketEntity> findByIdAndIsDeletedFalse(UUID id);
}
