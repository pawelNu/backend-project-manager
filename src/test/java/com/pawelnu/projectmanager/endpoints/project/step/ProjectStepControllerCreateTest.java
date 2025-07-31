package com.pawelnu.projectmanager.endpoints.project.step;

import static com.pawelnu.projectmanager.utils.Utils.accessDeniedError;
import static com.pawelnu.projectmanager.utils.Utils.unauthorizedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepDTO;
import com.pawelnu.projectmanager.exception.model.ReactAdminBadRequestError;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.Utils;
import com.pawelnu.projectmanager.utils.Utils.Postgres;
import com.pawelnu.projectmanager.utils.Utils.SpringDataSource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
class ProjectStepControllerCreateTest {
  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_PROJECT_STEPS;

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
  void shouldReturn_201_createProject() throws Exception {
    Instant deadline = LocalDateTime.of(2025, 7, 29, 12, 0).toInstant(ZoneOffset.UTC);
    ProjectStepCreateRequestDTO request =
        ProjectStepCreateRequestDTO.builder()
            .name("step for test")
            .projectId(UUID.fromString("bc6b9fee-e9d7-4692-853e-d6d2c00383b3"))
            .priorityValueId(UUID.fromString("26da6b3c-2079-47cf-a00f-06ea28702eb5"))
            .assignedEmployeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .deadline(deadline)
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
    ProjectStepDTO responseBody = objectMapper.readValue(contentAsString, ProjectStepDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertNotNull(responseBody.getId());
    assertEquals(request.getName(), responseBody.getName());
    assertEquals("project for project steps tetes", responseBody.getProjectName());
    assertEquals("MEDIUM", responseBody.getPriorityValue());
    assertEquals("Janita Jakubowski", responseBody.getAssignedEmployee());
    assertEquals(Instant.parse("2025-07-29T12:00:00Z"), responseBody.getDeadline());
  }

  @Test
  void shouldReturn_400_createProject() throws Exception {
    Instant deadline = LocalDateTime.of(2025, 7, 29, 0, 0).toInstant(ZoneOffset.UTC);
    ProjectStepCreateRequestDTO request =
        ProjectStepCreateRequestDTO.builder()
            .name("test")
            .projectId(UUID.fromString("bc6b9fee-e9d7-4692-853e-d6d2c00383b3"))
            .priorityValueId(UUID.fromString("26da6b3c-2079-47cf-a00f-06ea28702eb5"))
            .assignedEmployeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .deadline(deadline)
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
    assertEquals(
        "Project step name must be 5-255 characters", responseBody.getErrors().get("name"));
  }

  @Test
  void shouldReturn_401_createProject() throws Exception {
    Instant deadline = LocalDateTime.of(2025, 7, 29, 0, 0).toInstant(ZoneOffset.UTC);
    ProjectStepCreateRequestDTO request =
        ProjectStepCreateRequestDTO.builder()
            .name("step for test")
            .projectId(UUID.fromString("bc6b9fee-e9d7-4692-853e-d6d2c00383b3"))
            .priorityValueId(UUID.fromString("26da6b3c-2079-47cf-a00f-06ea28702eb5"))
            .assignedEmployeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .deadline(deadline)
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
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_createProject() throws Exception {
    Instant deadline = LocalDateTime.of(2025, 7, 29, 0, 0).toInstant(ZoneOffset.UTC);
    ProjectStepCreateRequestDTO request =
        ProjectStepCreateRequestDTO.builder()
            .name("step for test")
            .projectId(UUID.fromString("bc6b9fee-e9d7-4692-853e-d6d2c00383b3"))
            .priorityValueId(UUID.fromString("26da6b3c-2079-47cf-a00f-06ea28702eb5"))
            .assignedEmployeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .deadline(deadline)
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
}
