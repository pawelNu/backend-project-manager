package com.pawelnu.projectmanager.endpoints.company;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pawelnu.projectmanager.config.DataInit;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Slf4j
class CompanyControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private CompanyRepository companyRepository;

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:17")
          .withDatabaseName("testdb")
          .withUsername("user")
          .withPassword("password")
          .withInitScript("schema.sql");

  @DynamicPropertySource
  static void postgresProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @BeforeEach
  void initData() {
    companyRepository.deleteAll();
    CompanyEntity companyEntity =
        DataInit.generateCompany(UUID.fromString("07075a81-6219-4748-a76f-d9a66f4a79df"), 1);
    companyRepository.saveAndFlush(companyEntity);
  }

  @Test
  void shouldReturnCompanyById() throws Exception {
//    TODO start here
    mockMvc
        .perform(
            get("/api/companies/07075a81-6219-4748-a76f-d9a66f4a79df")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value("OpenAI"));
  }

  @Test
  void create() {}

  @Test
  void getAllCompanies() {}

  @Test
  void getById() {}

  @Test
  void editById() {}

  @Test
  void deleteById() {}

  @Test
  void filterCompanies() {}

  @Test
  void getList() {}
}
