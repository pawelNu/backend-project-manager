package com.pawelnu.projectmanager.endpoints.ticket;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {
  Optional<TicketEntity> findByIdAndIsDeletedFalse(UUID id);

  @Query(
      value = "SELECT count(*) FROM pg_class WHERE relkind='S' AND relname = :sequenceName",
      nativeQuery = true)
  int checkSequenceExists(@Param("sequenceName") String sequenceName);

  @Query(value = "SELECT nextval(:sequenceName)", nativeQuery = true)
  long getNextTicketNumber(@Param("sequenceName") String sequenceName);
}
