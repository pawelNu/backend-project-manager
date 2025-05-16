package com.pawelnu.projectmanager.endpoints.ticket.history;

import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.ticket.TicketEntity;
import com.pawelnu.projectmanager.endpoints.ticket.status.TicketStatusEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket_histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketHistoryEntity extends Auditable {
  @Id @GeneratedValue UUID id;

  @ManyToOne
  @JoinColumn(name = "ticket_id")
  private TicketEntity ticket;

  @ManyToOne
  @JoinColumn(name = "from_status_id")
  private TicketStatusEntity fromStatus;

  @ManyToOne
  @JoinColumn(name = "to_status_id")
  private TicketStatusEntity toStatus;

  @ManyToOne
  @JoinColumn(name = "from_employee_id")
  private EmployeeEntity fromEmployee;

  @ManyToOne
  @JoinColumn(name = "to_employee_id")
  private EmployeeEntity toEmployee;

  private String comment;
}
