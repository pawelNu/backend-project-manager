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

  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private CompanyRepository companyRepository;
  @Autowired private CompanyService companyService;
  @Autowired private CompanyQueryRepository companyQueryRepository;
  private String jwtTokenWithAuthorities;
  private String jwtTokenWithoutAuthorities;
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
      request.addHeader("Authorization", "Bearer " + jwtTokenWithAuthorities);
      request.addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
      return request;
    };
  }

  private RequestPostProcessor withBadJwt() {
    return request -> {
      request.addHeader("Authorization", "Bearer " + jwtTokenWithoutAuthorities);
      request.addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
      return request;
    };
  }

  @BeforeEach
  void generateJwtToken() {
    if (jwtTokenWithAuthorities == null) {
      jwtTokenWithAuthorities = jwtUtils.generateTokenFromUsername("test");
    }
    if (jwtTokenWithoutAuthorities == null) {
      jwtTokenWithoutAuthorities = jwtUtils.generateTokenFromUsername("Georgine_Welch85182");
    }
  }

  @Test
  void shouldReturn_200_getCompanyById() throws Exception {
    mockMvc
        .perform(get("/" + Path.API_COMPANIES + "/" + companyId).with(withJwt()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value("Hayes-Welch"));
  }

  @Test
  void shouldReturn_400_getCompanyById() throws Exception {
    mockMvc
        .perform(get("/" + Path.API_COMPANIES + "/invalid-uuid").with(withJwt()))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message")
                .value(
                    "Method parameter 'id': Failed to convert value of type 'java.lang.String' to"
                        + " required type 'java.util.UUID'; Invalid UUID string: invalid-uuid"));
  }

  @Test
  void shouldReturn_401_getCompanyById() throws Exception {
    mockMvc
        .perform(get("/" + Path.API_COMPANIES + "/" + companyId))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));
  }

  @Test
  void shouldReturn_403_getCompanyById() throws Exception {
    mockMvc
        .perform(get("/" + Path.API_COMPANIES + "/" + companyId).with(withBadJwt()))
        .andExpect(status().isForbidden())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Access denied"));
  }

  @Test
  void shouldReturn_404_getCompanyById() throws Exception {
    mockMvc
        .perform(
            get("/" + Path.API_COMPANIES + "/cf578fec-006b-4604-a5e8-5ad1b2ea2be5").with(withJwt()))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message")
                .value("Company not found with id: cf578fec-006b-4604-a5e8-5ad1b2ea2be5"));
  }

  @Test
  void create() {
    //    TODO prepare tests for create for all responses
  }

  @Test
  void getAllCompanies() {}

  @Test
  void editById() {}

  @Test
  void deleteById() {}

  @Test
  void filterCompanies() {}

  @Test
  void getList() {}
}
