package com.pawelnu.projectmanager.enums;

import com.pawelnu.projectmanager.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TicketStatus {
  CREATED("Created"),
  ASSIGNED("Assigned"),
  SEND_TO_CLIENT("Send to client"),
  CLOSED("Closed");

  private final String value;

  public static TicketStatus fromValue(String value) {
    for (TicketStatus ticketStatus : values()) {
      if (ticketStatus.value.equalsIgnoreCase(value)) {
        return ticketStatus;
      }
    }
    throw new BadRequestException("Unsupported ticket status: " + value);
  }
}
