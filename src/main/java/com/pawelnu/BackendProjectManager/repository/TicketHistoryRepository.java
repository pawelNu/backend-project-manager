package com.pawelnu.BackendProjectManager.repository;

import com.pawelnu.BackendProjectManager.entity.TicketHistoryEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketHistoryRepository extends JpaRepository<TicketHistoryEntity, UUID> {}
