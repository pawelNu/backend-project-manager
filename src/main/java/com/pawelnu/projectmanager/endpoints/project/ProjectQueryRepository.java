package com.pawelnu.projectmanager.endpoints.project;

import com.pawelnu.projectmanager.endpoints.category.QCategoryEntity;
import com.pawelnu.projectmanager.endpoints.category.value.QCategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.company.QCompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.QEmployeeEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProjectQueryRepository {
  private final JPAQueryFactory queryFactory;

  public Optional<ProjectEntity> findById(UUID id) {
    QProjectEntity project = QProjectEntity.projectEntity;
    QCategoryValueEntity projectCategoryValue = QCategoryValueEntity.categoryValueEntity;
    QCategoryEntity projectCategory = QCategoryEntity.categoryEntity;
    QCompanyEntity company = QCompanyEntity.companyEntity;
    QEmployeeEntity assignedEmployee = QEmployeeEntity.employeeEntity;
    QCategoryValueEntity projectPriorityValue = QCategoryValueEntity.categoryValueEntity;
    QCategoryEntity projectPriority = QCategoryEntity.categoryEntity;

    List<ProjectEntity> fetch =
        queryFactory
            .selectFrom(project)
            .leftJoin(project.categoryValue, projectCategoryValue)
            .fetchJoin()
            .leftJoin(projectCategoryValue.category, projectCategory)
            .fetchJoin()
            .leftJoin(project.company, company)
            .fetchJoin()
            .leftJoin(project.assignedEmployee, assignedEmployee)
            .fetchJoin()
            .leftJoin(project.priorityValue, projectPriorityValue)
            .fetchJoin()
            .leftJoin(projectPriorityValue.category, projectPriority)
            .fetchJoin()
            .where(project.id.eq(id).and(project.isDeleted.isFalse()))
            .fetch();

    if (fetch != null && !fetch.isEmpty()) {
      ProjectEntity projectEntity = fetch.getFirst();
      return Optional.of(projectEntity);
    }
    return Optional.empty();
  }
}
