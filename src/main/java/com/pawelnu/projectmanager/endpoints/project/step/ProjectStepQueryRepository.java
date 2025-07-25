package com.pawelnu.projectmanager.endpoints.project.step;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProjectStepQueryRepository {

  public Optional<ProjectStepEntity> findById(UUID id) {
    //    TODO implement Optional<ProjectStepEntity> findById(UUID id)
    throw new NotImplementedException("not implemented!");
  }
}
