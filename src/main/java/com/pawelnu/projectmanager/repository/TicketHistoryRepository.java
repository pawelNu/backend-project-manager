package com.pawelnu.projectmanager.repository;

import com.pawelnu.projectmanager.entity.TicketHistoryEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketHistoryRepository extends JpaRepository<TicketHistoryEntity, UUID> {}
