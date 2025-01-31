package com.pawelnu.BackendProjectManager.entity;

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
  @Column(updatable = false)
  private Instant created;

  @UpdateTimestamp private Instant lastModified;
}
