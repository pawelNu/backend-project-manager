package com.pawelnu.projectmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class Auditable {

  @Version private Integer version;

  @CreationTimestamp
  @Column(updatable = false, columnDefinition = "timestamp(6) without time zone")
  private Instant created;

  @UpdateTimestamp
  @Column(columnDefinition = "timestamp(6) without time zone")
  private Instant lastModified;
}
