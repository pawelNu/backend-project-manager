package com.pawelnu.BackendProjectManager.repository.project;

import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringRequestDTO;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity_;
import com.pawelnu.BackendProjectManager.entity.enums.Status;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import liquibase.hub.model.Project;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectSpecification {

    public static Specification<ProjectEntity> filterProject(ProjectFilteringRequestDTO projectFilteringRequestDTO) {

        return Specification.where(
                findByProjectStatus(projectFilteringRequestDTO.getProjectStatus()));
    }

    private static Specification<ProjectEntity> findByProjectStatus(List<String> projectStatuses) {
        if (projectStatuses == null || projectStatuses.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates =
                    projectStatuses.stream()
                            .map(status ->
                                    criteriaBuilder.equal(
                                            root.get(ProjectEntity_.STATUS),
                                            Status.fromValue(status)
                                    ))
                            .toArray(Predicate[]::new);

            return criteriaBuilder.or(predicates);
        };
    }
}
