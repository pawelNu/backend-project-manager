package com.pawelnu.projectmanager.endpoints.company.employee.authority;

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
import com.pawelnu.projectmanager.utils.Utils.Postgres;
import com.pawelnu.projectmanager.utils.Utils.SpringDataSource;
import java.util.List;
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
class EmployeeAuthorityControllerTest {
  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_EMPLOYEE_AUTHORITIES;

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(Postgres.POSTGRES_17)
          .withDatabaseName(Postgres.DB_NAME)
          .withUsername(Postgres.USER)
          .withPassword(Postgres.PASSWORD);

  @DynamicPropertySource
  static void postgresProperties(DynamicPropertyRegistry registry) {
    registry.add(SpringDataSource.URL, postgres::getJdbcUrl);
    registry.add(SpringDataSource.USERNAME, postgres::getUsername);
    registry.add(SpringDataSource.PASSWORD, postgres::getPassword);
  }

  @BeforeEach
  void beforeEach() {
    Utils.generateToken(jwtUtils);
  }

  @Test
  void shouldReturn_201_createEmployeeAuthority() throws Exception {
    EmployeeAuthorityCreateRequestDTO request =
        EmployeeAuthorityCreateRequestDTO.builder()
            .employeeId(UUID.fromString("fc473d21-c66e-4fbd-9d2d-7c6b4be75984"))
            .authorityIds(
                List.of(
                    UUID.fromString("968197d0-ae85-49a6-a4c2-5b466fcb1d20"),
                    UUID.fromString("2c021bee-1f46-4014-a2ec-1784b997c540"),
                    UUID.fromString("feabbad6-6d40-4a33-8d0b-d9cbe57124ef"),
                    UUID.fromString("7d57750c-4fe7-4301-bfe8-4be52c3f307b"),
                    UUID.fromString("ca33acc1-55e0-4870-bdd5-1403f3dc3624")))
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
    EmployeeAuthoritiesDTO responseBody =
        objectMapper.readValue(contentAsString, EmployeeAuthoritiesDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertEquals("userNoAuthorities", responseBody.getUsername());
    assertTrue(
        responseBody
            .getAuthorityNames()
            .containsAll(
                List.of(
                    "COMPANY_GET_BY_ID",
                    "COMPANY_GET_LIST",
                    "COMPANY_CREATE",
                    "COMPANY_DELETE_BY_ID",
                    "COMPANY_EDIT_BY_ID")));
  }

  @Test
  void shouldReturn_400_createEmployeeAuthority() throws Exception {
    EmployeeAuthorityCreateRequestDTO request =
        EmployeeAuthorityCreateRequestDTO.builder()
            .employeeId(UUID.fromString("fc473d21-c66e-4fbd-9d2d-7c6b4be75984"))
            .authorityIds(null)
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
    assertEquals("must not be null", responseBody.getErrors().get("authorityIds"));
  }

  @Test
  void shouldReturn_401_createEmployeeAuthority() throws Exception {
    EmployeeAuthorityCreateRequestDTO request =
        EmployeeAuthorityCreateRequestDTO.builder()
            .employeeId(UUID.fromString("fc473d21-c66e-4fbd-9d2d-7c6b4be75984"))
            .authorityIds(
                List.of(
                    UUID.fromString("968197d0-ae85-49a6-a4c2-5b466fcb1d20"),
                    UUID.fromString("2c021bee-1f46-4014-a2ec-1784b997c540"),
                    UUID.fromString("feabbad6-6d40-4a33-8d0b-d9cbe57124ef"),
                    UUID.fromString("7d57750c-4fe7-4301-bfe8-4be52c3f307b"),
                    UUID.fromString("ca33acc1-55e0-4870-bdd5-1403f3dc3624")))
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
  void shouldReturn_403_createEmployeeAuthority() throws Exception {
    EmployeeAuthorityCreateRequestDTO request =
        EmployeeAuthorityCreateRequestDTO.builder()
            .employeeId(UUID.fromString("fc473d21-c66e-4fbd-9d2d-7c6b4be75984"))
            .authorityIds(
                List.of(
                    UUID.fromString("968197d0-ae85-49a6-a4c2-5b466fcb1d20"),
                    UUID.fromString("2c021bee-1f46-4014-a2ec-1784b997c540"),
                    UUID.fromString("feabbad6-6d40-4a33-8d0b-d9cbe57124ef"),
                    UUID.fromString("7d57750c-4fe7-4301-bfe8-4be52c3f307b"),
                    UUID.fromString("ca33acc1-55e0-4870-bdd5-1403f3dc3624")))
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

  //  TODO add Test getList() {

  //  TODO add Test delete() {

}
