package com.pawelnu.BackendProjectManager.repository;

import com.pawelnu.BackendProjectManager.entity.TicketEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {}
