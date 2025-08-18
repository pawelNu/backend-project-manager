package com.pawelnu.projectmanager.endpoints.ticket.history;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketHistoryRepository extends JpaRepository<TicketHistoryEntity, UUID> {
  Optional<TicketHistoryEntity> findByIdAndIsDeletedFalse(UUID id);
}
