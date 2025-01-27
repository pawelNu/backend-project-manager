package com.pawelnu.BackendProjectManager.config.init;

import com.pawelnu.BackendProjectManager.entity.CompanyEntity;
import com.pawelnu.BackendProjectManager.entity.PersonEntity;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.entity.TicketEntity;
import com.pawelnu.BackendProjectManager.enums.CompanyStatus;
import com.pawelnu.BackendProjectManager.enums.PersonRole;
import com.pawelnu.BackendProjectManager.enums.ProjectStatus;
import com.pawelnu.BackendProjectManager.repository.CompanyRepository;
import com.pawelnu.BackendProjectManager.repository.PersonRepository;
import com.pawelnu.BackendProjectManager.repository.ProjectRepository;
import com.pawelnu.BackendProjectManager.repository.TicketRepository;
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
  private final TicketRepository ticketRepository;

  @PostConstruct
  private void loadData() {

    List<CompanyEntity> companies = createCompanies();
    List<ProjectEntity> projects = createProjects(companies);
    List<PersonEntity> people = createPeople(companies);
    createdTickets(people, projects);
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

  private List<ProjectEntity> createProjects(List<CompanyEntity> companies) {
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

    return projectRepository.saveAll(projects);
  }

  private List<PersonEntity> createPeople(List<CompanyEntity> companies) {
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
    return personRepository.saveAll(people);
  }

  private void createdTickets(List<PersonEntity> people, List<ProjectEntity> projects) {
    List<TicketEntity> tickets =
        List.of(
            TicketEntity.builder()
                .seriesNumber(202501270001L)
                .title("Problem with something")
                .registeringPerson(people.get(0))
                .assignedPerson(people.get(2))
                .project(projects.get(0))
                .build(),
            TicketEntity.builder()
                .seriesNumber(202501270001L)
                .title("Problem with something 2")
                .registeringPerson(people.get(0))
                .assignedPerson(people.get(2))
                .project(projects.get(0))
                .build(),
            TicketEntity.builder()
                .seriesNumber(202501270001L)
                .title("Problem with something else")
                .registeringPerson(people.get(1))
                .assignedPerson(people.get(2))
                .project(projects.get(0))
                .build());
    ticketRepository.saveAll(tickets);
  }
}
