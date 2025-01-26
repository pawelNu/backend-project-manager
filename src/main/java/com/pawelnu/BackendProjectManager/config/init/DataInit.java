package com.pawelnu.BackendProjectManager.config.init;

import com.pawelnu.BackendProjectManager.entity.CompanyEntity;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.enums.CompanyStatus;
import com.pawelnu.BackendProjectManager.enums.ProjectStatus;
import com.pawelnu.BackendProjectManager.repository.CompanyRepository;
import com.pawelnu.BackendProjectManager.repository.project.ProjectRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit {

  private final CompanyRepository companyRepository;
  private final ProjectRepository projectRepository;

  @PostConstruct
  private void loadData() {

    CompanyEntity c1 =
        CompanyEntity.builder().name("Drive S.A.").status(CompanyStatus.ACTIVE).build();
    CompanyEntity c2 =
        CompanyEntity.builder().name("Tele S.A.").status(CompanyStatus.ACTIVE).build();
    CompanyEntity c3 =
        CompanyEntity.builder().name("Account S.A.").status(CompanyStatus.TERMINATED).build();

    List<CompanyEntity> companies = List.of(c1, c2, c3);

    companyRepository.saveAll(companies);

    List<ProjectEntity> projects =
        List.of(
            ProjectEntity.builder()
                .name("Transport Company")
                .projectStatus(ProjectStatus.MAINTAINED)
                .company(c1)
                .build(),
            ProjectEntity.builder()
                .name("Telecommunication Company")
                .projectStatus(ProjectStatus.MAINTAINED)
                .company(c2)
                .build(),
            ProjectEntity.builder()
                .name("Outsourcing Company")
                .projectStatus(ProjectStatus.CLOSED)
                .company(c3)
                .build());

    projectRepository.saveAll(projects);
  }
}
