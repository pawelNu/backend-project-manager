package com.pawelnu.BackendProjectManager.config.init;

import com.pawelnu.BackendProjectManager.entity.CompanyEntity;
import com.pawelnu.BackendProjectManager.entity.PersonEntity;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.enums.CompanyStatus;
import com.pawelnu.BackendProjectManager.enums.PersonRole;
import com.pawelnu.BackendProjectManager.enums.ProjectStatus;
import com.pawelnu.BackendProjectManager.repository.CompanyRepository;
import com.pawelnu.BackendProjectManager.repository.PersonRepository;
import com.pawelnu.BackendProjectManager.repository.ProjectRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class DataInit {

  private final CompanyRepository companyRepository;
  private final ProjectRepository projectRepository;
  private final PersonRepository personRepository;

  @PostConstruct
  private void loadData() {

    List<CompanyEntity> companies = createCompanies();
    createProjects(companies);
    createPeople(companies);
  }

  private List<CompanyEntity> createCompanies() {
   CompanyEntity c1 =
        CompanyEntity.builder().name("Drive S.A.").status(CompanyStatus.ACTIVE).build();
    CompanyEntity c2 =
        CompanyEntity.builder().name("Tele S.A.").status(CompanyStatus.ACTIVE).build();
    CompanyEntity c3 =
        CompanyEntity.builder().name("Account S.A.").status(CompanyStatus.TERMINATED).build();
    CompanyEntity c4 =
        CompanyEntity.builder().name("Main Company S.A.").status(CompanyStatus.ACTIVE).build();

    List<CompanyEntity> companies = List.of(c1, c2, c3, c4);

    return companyRepository.saveAll(companies);
  }

  private void createProjects(List<CompanyEntity> companies) {
    List<ProjectEntity> projects =
        List.of(
            ProjectEntity.builder()
                .name("Transport Company")
                .projectStatus(ProjectStatus.MAINTAINED)
                .company(companies.get(0))
                .build(),
            ProjectEntity.builder()
                .name("Telecommunication Company")
                .projectStatus(ProjectStatus.MAINTAINED)
                .company(companies.get(1))
                .build(),
            ProjectEntity.builder()
                .name("Outsourcing Company")
                .projectStatus(ProjectStatus.CLOSED)
                .company(companies.get(2))
                .build());

    projectRepository.saveAll(projects);
  }

  private void createPeople(List<CompanyEntity> companies) {
    List<PersonEntity> people =
        List.of(
            PersonEntity.builder()
                .firstName("Stefan")
                .lastName("Smith")
                .role(PersonRole.CLIENT)
                .company(companies.get(0))
                .build(),
            PersonEntity.builder()
                .firstName("Mark")
                .lastName("White")
                .role(PersonRole.CLIENT_LEADER)
                .company(companies.get(0))
                .build(),
            PersonEntity.builder()
                .firstName("John")
                .lastName("Black")
                .role(PersonRole.EMPLOYEE)
                .company(companies.get(3))
                .build());
    personRepository.saveAll(people);
  }
}
