package com.pawelnu.BackendProjectManager.repository.project;

import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringRequestDTO;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity_;
import com.pawelnu.BackendProjectManager.enums.ProjectStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectSpecification {

  public static Specification<ProjectEntity> filterProject(
      ProjectFilteringRequestDTO projectFilteringRequestDTO) {

    Specification<ProjectEntity> specification = Specification.where(null);

    if (projectFilteringRequestDTO != null) {
      specification =
          specification
              .and(findByProjectName(projectFilteringRequestDTO.getProjectNameKeywords()))
              .and(findByProjectStatus(projectFilteringRequestDTO.getProjectStatuses()));
    }

    return specification;
  }

  private static Specification<ProjectEntity> findByProjectName(List<String> keywords) {
    if (keywords == null || keywords.isEmpty()) {
      return null;
    }
    return (root, query, criteriaBuilder) -> {
      Predicate[] predicates =
          keywords.stream()
              .map(
                  keyword ->
                      criteriaBuilder.like(
                          criteriaBuilder.lower(root.get(ProjectEntity_.NAME)),
                          "%" + keyword.toLowerCase() + "%"))
              .toArray(Predicate[]::new);

      return criteriaBuilder.or(predicates);
    };
  }

  private static Specification<ProjectEntity> findByProjectStatus(List<String> projectStatuses) {
    if (projectStatuses == null || projectStatuses.isEmpty()) {
      return null;
    }
    return (root, query, criteriaBuilder) -> {
      Predicate[] predicates =
          projectStatuses.stream()
              .map(
                  status ->
                      criteriaBuilder.equal(
                          root.get(ProjectEntity_.STATUS), ProjectStatus.fromValue(status)))
              .toArray(Predicate[]::new);

      return criteriaBuilder.or(predicates);
    };
  }
}
