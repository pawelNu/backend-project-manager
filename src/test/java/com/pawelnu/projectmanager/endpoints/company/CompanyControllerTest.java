package com.pawelnu.projectmanager.endpoints.company;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.utils.Path;
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
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Slf4j
class CompanyControllerTest {

  @Autowired
  private JwtUtils jwtUtils;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private CompanyRepository companyRepository;
  @Autowired
  private CompanyService companyService;
  @Autowired
  private CompanyQueryRepository companyQueryRepository;
  private String jwtToken;
  private UUID companyId = UUID.fromString("cf578fec-006b-4604-a5e8-5ad1b3ea2be5");

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:17")
          .withDatabaseName("testdb")
          .withUsername("user")
          .withPassword("password");

  @DynamicPropertySource
  static void postgresProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  private RequestPostProcessor withJwt() {
    return request -> {
      request.addHeader("Authorization", "Bearer " + jwtToken);
      request.addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
      return request;
    };
  }

  @BeforeEach
  void generateJwtToken() {
    if (jwtToken == null) {
      jwtToken = jwtUtils.generateTokenFromUsername("test");
    }
  }

  @Test
  void shouldReturn_200_getCompanyById() throws Exception {
    mockMvc
        .perform(get(Path.API_COMPANIES + "/" + companyId).with(withJwt()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value("Hayes-Welch"));
  }

  @Test
  void create() {
  }

  @Test
  void getAllCompanies() {
  }

  @Test
  void getById() {
  }

  @Test
  void editById() {
  }

  @Test
  void deleteById() {
  }

  @Test
  void filterCompanies() {
  }

  @Test
  void getList() {
  }
}
