package com.pawelnu.projectmanager.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class Auditable {

  @Version private Integer version;

  @CreationTimestamp private Instant created;

  @UpdateTimestamp private Instant lastModified;

  @Getter @Setter private Boolean isDeleted = false;
}
