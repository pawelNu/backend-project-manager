package com.pawelnu.projectmanager.config;

import com.github.javafaker.Faker;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyRepository;
import com.pawelnu.projectmanager.endpoints.companyaddress.CompanyAddressEntity;
import com.pawelnu.projectmanager.endpoints.companyaddress.CompanyAddressRepository;
import com.pawelnu.projectmanager.endpoints.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.employee.EmployeeRepository;
import com.pawelnu.projectmanager.enums.CompanyStatus;
import jakarta.annotation.PostConstruct;
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
  private final EmployeeRepository employeeRepository;

  @PostConstruct
  private void loadData() {

    List<CompanyEntity> companies = createCompanies();
    createCompanyAddresses(companies);
    createEmployees();
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
      CompanyAddressEntity ca = generateCompanyAddress(company);
      CompanyAddressEntity ca2 = generateCompanyAddress(company);
      companyAddresses.add(ca);
      companyAddresses.add(ca2);
    }
    return companyAddressRepository.saveAll(companyAddresses);
  }

  private CompanyAddressEntity generateCompanyAddress(CompanyEntity company) {
    CompanyAddressEntity ca =
        CompanyAddressEntity.builder()
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
    return ca;
  }

  private String formatStringToEmail(String s) {
    return s.replaceAll(" ", "_").replaceAll(",", "").replaceAll("-", "_").toLowerCase();
  }

  private EmployeeEntity createEmployee() {
    String firstName = faker.name().firstName();
    String lastName = faker.name().lastName();
    String username = firstName + "_" + lastName + faker.number().randomNumber(5, true);
    return EmployeeEntity.builder()
        .firstName(firstName)
        .lastName(lastName)
        .username(username)
        .email(faker.internet().safeEmailAddress(formatStringToEmail(firstName + "." + lastName)))
        .phoneNumber(faker.phoneNumber().cellPhone())
        .build();
  }

  private void createEmployees() {
    List<EmployeeEntity> employees = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      EmployeeEntity employee = createEmployee();
      employees.add(employee);
    }
    employeeRepository.saveAll(employees);
  }
}
