package com.pawelnu.projectmanager.config;

import com.github.javafaker.Faker;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyRepository;
import com.pawelnu.projectmanager.endpoints.companyaddress.CompanyAddressEntity;
import com.pawelnu.projectmanager.endpoints.companyaddress.CompanyAddressRepository;
import com.pawelnu.projectmanager.entity.PersonEntity;
import com.pawelnu.projectmanager.entity.ProjectEntity;
import com.pawelnu.projectmanager.entity.TicketEntity;
import com.pawelnu.projectmanager.entity.TicketHierarchyEntity;
import com.pawelnu.projectmanager.entity.TicketHistoryEntity;
import com.pawelnu.projectmanager.enums.CompanyStatus;
import com.pawelnu.projectmanager.enums.PersonRole;
import com.pawelnu.projectmanager.enums.ProjectStatus;
import com.pawelnu.projectmanager.enums.TicketStatus;
import com.pawelnu.projectmanager.repository.PersonRepository;
import com.pawelnu.projectmanager.repository.ProjectRepository;
import com.pawelnu.projectmanager.repository.TicketHierarchyRepository;
import com.pawelnu.projectmanager.repository.TicketHistoryRepository;
import com.pawelnu.projectmanager.repository.TicketRepository;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class DataInit {

  private final Faker faker = new Faker(new Random(12345));
  private static long counter = 0;
  private final CompanyRepository companyRepository;
  private final CompanyAddressRepository companyAddressRepository;
  private final ProjectRepository projectRepository;
  private final PersonRepository personRepository;
  private final TicketRepository ticketRepository;
  private final TicketHierarchyRepository ticketHierarchyRepository;
  private final TicketHistoryRepository ticketHistoryRepository;

  @PostConstruct
  private void loadData() {

    List<CompanyEntity> companies = createCompanies();
    createCompanyAddresses(companies);
    //    List<ProjectEntity> projects = createProjects(companies);
    //    List<PersonEntity> people = createPeople(companies);
    //    createTickets(people, projects);
    //    createTicketsWithSubTickets(people, projects);
    //    createTicketWithHistory(people, projects);
  }

  private List<CompanyEntity> createCompanies() {
    List<CompanyEntity> companies = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      CompanyEntity c =
          CompanyEntity.builder()
              .name(faker.company().name())
              .nip(String.valueOf(faker.number().randomNumber(10, true)))
              .regon(String.valueOf(faker.number().randomNumber(9, true)))
              .website("https://" + faker.internet().domainName())
              .status(i % 2 == 0 ? CompanyStatus.ACTIVE : CompanyStatus.TERMINATED)
              .build();
      companies.add(c);
    }
    return companyRepository.saveAll(companies);
  }

  private List<CompanyAddressEntity> createCompanyAddresses(List<CompanyEntity> companies) {
    List<CompanyAddressEntity> companyAddresses = new ArrayList<>();
    for (CompanyEntity company : companies) {
      CompanyAddressEntity ca = CompanyAddressEntity.builder()
          .company(company)
          .street(faker.address().streetName())
          .streetNumber(faker.address().streetAddressNumber())
          .city(faker.address().city())
          .zipCode(faker.address().zipCode())
          .country(faker.address().country())
          .phoneNumber(faker.phoneNumber().cellPhone())
          .emailAddress(faker.internet().safeEmailAddress(formatStringToEmail(company.getName())))
          .addressType("main")
          .build();
      companyAddresses.add(ca);
    }
    return companyAddressRepository.saveAll(companyAddresses);
  }

  private String formatStringToEmail(String s) {
    return s.replaceAll(" ", "_")
        .replaceAll(",", "")
        .replaceAll("-", "_")
        .toLowerCase();
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
                .build(),
            PersonEntity.builder()
                .firstName("Adam")
                .lastName("Blue")
                .role(PersonRole.EMPLOYEE)
                .company(companies.get(3))
                .build(),
            PersonEntity.builder()
                .firstName("Service")
                .lastName("User")
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

  private void createTicketWithHistory(List<PersonEntity> people, List<ProjectEntity> projects) {
    TicketEntity ticketWithHistory =
        TicketEntity.builder()
            .seriesNumber(generateSeriesNumber())
            .title("Ticket with history")
            .registeringPerson(people.get(0))
            .assignedPerson(people.get(2))
            .project(projects.get(0))
            .build();
    TicketEntity savedTicket = ticketRepository.save(ticketWithHistory);
    TicketHistoryEntity h1 =
        TicketHistoryEntity.builder()
            .ticket(savedTicket)
            .date(Instant.now())
            .fromState(TicketStatus.CREATED)
            .toState(TicketStatus.ASSIGNED)
            .fromPerson(people.get(0))
            .toPerson(people.get(2))
            .comment("Created ticket for problem xyz.")
            .build();
    TicketHistoryEntity h2 =
        TicketHistoryEntity.builder()
            .ticket(savedTicket)
            .date(Instant.now())
            .fromState(TicketStatus.ASSIGNED)
            .toState(TicketStatus.ASSIGNED)
            .fromPerson(people.get(2))
            .toPerson(people.get(3))
            .comment("Assigned problem to further analysis.")
            .build();
    TicketHistoryEntity h3 =
        TicketHistoryEntity.builder()
            .ticket(savedTicket)
            .date(Instant.now())
            .fromState(TicketStatus.ASSIGNED)
            .toState(TicketStatus.SEND_TO_CLIENT)
            .fromPerson(people.get(3))
            .toPerson(people.get(0))
            .comment("Analysis completed. Problem no longer exists.")
            .build();
    TicketHistoryEntity h4 =
        TicketHistoryEntity.builder()
            .ticket(savedTicket)
            .date(Instant.now())
            .fromState(TicketStatus.SEND_TO_CLIENT)
            .toState(TicketStatus.CLOSED)
            .fromPerson(people.get(0))
            .toPerson(people.get(4))
            .comment("Analysis completed. Problem no longer exists.")
            .build();
    List<TicketHistoryEntity> ticketHistories = List.of(h1, h2, h3, h4);
    ticketHistoryRepository.saveAll(ticketHistories);
  }
}
