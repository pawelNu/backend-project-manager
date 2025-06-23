package com.pawelnu.projectmanager.endpoints.company.employee;

import static com.pawelnu.projectmanager.utils.Utils.FULL_AUTH_IS_REQUIRED;
import static com.pawelnu.projectmanager.utils.Utils.accessDeniedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.exception.model.ReactAdminBadRequestError;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.Utils;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Slf4j
class EmployeeControllerTest {
  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_EMPLOYEES;

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

  @BeforeEach
  void beforeEach() {
    Utils.generateToken(jwtUtils);
  }

  @Test
  void shouldReturn_201_createEmployee() throws Exception {
    EmployeeCreateRequestDTO request =
        EmployeeCreateRequestDTO.builder()
            .companyId(UUID.fromString("cf578fec-006b-4604-a5e8-5ad1b3ea2be5"))
            .firstName("test firstName")
            .lastName("test lastName")
            .username("testUsername")
            .password("testPassword")
            .email("test.test@test.test")
            .phoneNumber("111-111-111")
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                post(BASE_URL)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    EmployeeDTO responseBody = objectMapper.readValue(contentAsString, EmployeeDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertEquals("Hayes-Welch", responseBody.getCompanyName());
    assertEquals(request.getFirstName(), responseBody.getFirstName());
    assertEquals(request.getLastName(), responseBody.getLastName());
    assertEquals(request.getUsername(), responseBody.getUsername());
    assertEquals(request.getEmail(), responseBody.getEmail());
    assertEquals(request.getPhoneNumber(), responseBody.getPhoneNumber());
  }

  @Test
  void shouldReturn_400_createEmployee() throws Exception {
    EmployeeCreateRequestDTO request =
        EmployeeCreateRequestDTO.builder()
            .companyId(UUID.fromString("cf578fec-006b-4604-a5e8-5ad1b3ea2be5"))
            .firstName("test firstName")
            .lastName("test lastName")
            .username("testUsername")
            .password("testPassword")
            .email("invalid.email")
            .phoneNumber("111-111-111")
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                post(BASE_URL)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminBadRequestError responseBody =
        objectMapper.readValue(contentAsString, ReactAdminBadRequestError.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals("must be a well-formed email address", responseBody.getErrors().get("email"));
  }

  @Test
  void shouldReturn_401_createEmployee() throws Exception {
    EmployeeCreateRequestDTO request =
        EmployeeCreateRequestDTO.builder()
            .companyId(UUID.fromString("cf578fec-006b-4604-a5e8-5ad1b3ea2be5"))
            .firstName("test firstName")
            .lastName("test lastName")
            .username("testUsername")
            .password("testPassword")
            .email("test.test@test.test")
            .phoneNumber("111-111-111")
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(FULL_AUTH_IS_REQUIRED, responseBody.getMessage());
  }

  @Test
  void shouldReturn_403_createEmployee() throws Exception {
    EmployeeCreateRequestDTO request =
        EmployeeCreateRequestDTO.builder()
            .companyId(UUID.fromString("cf578fec-006b-4604-a5e8-5ad1b3ea2be5"))
            .firstName("test firstName")
            .lastName("test lastName")
            .username("testUsername")
            .password("testPassword")
            .email("test.test@test.test")
            .phoneNumber("111-111-111")
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                post(BASE_URL)
                    .with(withBadJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(accessDeniedError(), responseBody);
  }

  //  TODO Test getList()

  //  TODO Test getById()

  //  TODO Test editById()

  //  TODO Test deleteById()
}
