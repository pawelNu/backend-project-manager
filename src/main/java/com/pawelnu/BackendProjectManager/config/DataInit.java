package com.pawelnu.BackendProjectManager.config;

import com.pawelnu.BackendProjectManager.entity.CompanyEntity;
import com.pawelnu.BackendProjectManager.entity.PersonEntity;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.entity.TicketEntity;
import com.pawelnu.BackendProjectManager.entity.TicketHierarchyEntity;
import com.pawelnu.BackendProjectManager.enums.CompanyStatus;
import com.pawelnu.BackendProjectManager.enums.PersonRole;
import com.pawelnu.BackendProjectManager.enums.ProjectStatus;
import com.pawelnu.BackendProjectManager.repository.CompanyRepository;
import com.pawelnu.BackendProjectManager.repository.PersonRepository;
import com.pawelnu.BackendProjectManager.repository.ProjectRepository;
import com.pawelnu.BackendProjectManager.repository.TicketHierarchyRepository;
import com.pawelnu.BackendProjectManager.repository.TicketRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class DataInit {

  private static long counter = 0;
  private final CompanyRepository companyRepository;
  private final ProjectRepository projectRepository;
  private final PersonRepository personRepository;
  private final TicketRepository ticketRepository;
  private final TicketHierarchyRepository ticketHierarchyRepository;

  @PostConstruct
  private void loadData() {

    List<CompanyEntity> companies = createCompanies();
    List<ProjectEntity> projects = createProjects(companies);
    List<PersonEntity> people = createPeople(companies);
    createTickets(people, projects);
    createTicketsWithSubTickets(people, projects);
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

  private void createTickets(List<PersonEntity> people, List<ProjectEntity> projects) {
    List<TicketEntity> tickets =
        List.of(
            TicketEntity.builder()
                .seriesNumber(generateSeriesNumber())
                .title("Problem with something")
                .registeringPerson(people.get(0))
                .assignedPerson(people.get(2))
                .project(projects.get(0))
                .build(),
            TicketEntity.builder()
                .seriesNumber(generateSeriesNumber())
                .title("Problem with something 2")
                .registeringPerson(people.get(0))
                .assignedPerson(people.get(2))
                .project(projects.get(0))
                .build(),
            TicketEntity.builder()
                .seriesNumber(generateSeriesNumber())
                .title("Problem with something else")
                .registeringPerson(people.get(1))
                .assignedPerson(people.get(2))
                .project(projects.get(0))
                .build());
    ticketRepository.saveAll(tickets);
  }

  private void createTicketsWithSubTickets(
      List<PersonEntity> people, List<ProjectEntity> projects) {
    List<TicketEntity> ticketEntities = createMainTicketAndSubTickets(people, projects);
    List<TicketHierarchyEntity> subTickets = createTicketHierarchy(ticketEntities);
  }

  private List<TicketHierarchyEntity> createTicketHierarchy(List<TicketEntity> ticketEntities) {
    List<TicketHierarchyEntity> subTickets =
        List.of(
            TicketHierarchyEntity.builder()
                .parentTicket(ticketEntities.get(0))
                .childTicket(ticketEntities.get(1))
                .level(1)
                .build(),
            TicketHierarchyEntity.builder()
                .parentTicket(ticketEntities.get(0))
                .childTicket(ticketEntities.get(2))
                .level(2)
                .build());
    return ticketHierarchyRepository.saveAll(subTickets);
  }

  private List<TicketEntity> createMainTicketAndSubTickets(
      List<PersonEntity> people, List<ProjectEntity> projects) {
    List<TicketEntity> tickets =
        List.of(
            TicketEntity.builder()
                .seriesNumber(generateSeriesNumber())
                .title("Main ticket")
                .registeringPerson(people.get(0))
                .assignedPerson(people.get(2))
                .project(projects.get(0))
                .build(),
            TicketEntity.builder()
                .seriesNumber(generateSeriesNumber())
                .title("SubTicket level 1")
                .registeringPerson(people.get(0))
                .assignedPerson(people.get(2))
                .project(projects.get(0))
                .build(),
            TicketEntity.builder()
                .seriesNumber(generateSeriesNumber())
                .title("SubTicket level 2")
                .registeringPerson(people.get(1))
                .assignedPerson(people.get(2))
                .project(projects.get(0))
                .build());
    List<TicketEntity> ticketEntities = ticketRepository.saveAll(tickets);
    return ticketEntities;
  }

  public static Long generateSeriesNumber() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    String formattedDate = now.format(formatter);
    counter++;
    return Long.parseLong(formattedDate) + counter;
  }
}
