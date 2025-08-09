package com.pawelnu.projectmanager.endpoints.project.step.comment;

import static com.pawelnu.projectmanager.utils.Utils.invalidUUIDError;
import static com.pawelnu.projectmanager.utils.Utils.unauthorizedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentDTO;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentEditRequestDTO;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.Utils;
import com.pawelnu.projectmanager.utils.Utils.Postgres;
import com.pawelnu.projectmanager.utils.Utils.SpringDataSource;
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
class ProjectStepCommentControllerEditByIdTest {
  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_PROJECT_STEP_COMMENTS;

  // TODO finish tests
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
  void shouldReturn_200_editProjectStepCommentById() throws Exception {
    String projectStepCommentId = "052b6078-6c32-4e36-9f1f-01c30c7bac90";
    String url = BASE_URL + "/" + projectStepCommentId;
    ProjectStepCommentEditRequestDTO request =
        ProjectStepCommentEditRequestDTO.builder()
            .comment("updated comment")
            .stepId(UUID.fromString("5e47e5c5-4a3d-4e67-b41d-a04f94c6b93a"))
            .employeeId(UUID.fromString("a5cb9e8c-a9cb-4657-b1e4-1dec8bcdf997"))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                put(url)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ProjectStepCommentDTO responseBody =
        objectMapper.readValue(contentAsString, ProjectStepCommentDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(request.getComment(), responseBody.getComment());
    assertEquals(
        UUID.fromString("7ebf87eb-be90-45d8-b92c-25701897211f"), responseBody.getProjectId());
    assertEquals(request.getStepId(), responseBody.getStepId());
    assertEquals(request.getEmployeeId(), responseBody.getEmployeeId());
  }

  @Test
  void shouldReturn_400_editProjectStepCommentById() throws Exception {
    String projectStepCommentId = "invalid-uuid";
    String url = BASE_URL + "/" + projectStepCommentId;
    ProjectStepCommentEditRequestDTO request =
        ProjectStepCommentEditRequestDTO.builder()
            .comment("test")
            .stepId(UUID.fromString("5e47e5c5-4a3d-4e67-b41d-a04f94c6b93a"))
            .employeeId(UUID.fromString("a5cb9e8c-a9cb-4657-b1e4-1dec8bcdf997"))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                put(url)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(invalidUUIDError(), responseBody);
  }

  @Test
  void shouldReturn_401_editProjectStepCommentById() throws Exception {
    String projectStepCommentId = "e6c8acd6-a7bb-48a6-bad7-e4154b5010f0";
    String url = BASE_URL + "/" + projectStepCommentId;
    ProjectStepCommentEditRequestDTO request =
        ProjectStepCommentEditRequestDTO.builder()
            .comment("updated comment")
            .stepId(UUID.fromString("5e47e5c5-4a3d-4e67-b41d-a04f94c6b93a"))
            .employeeId(UUID.fromString("a5cb9e8c-a9cb-4657-b1e4-1dec8bcdf997"))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(put(url).contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_editProjectStepCommentById() throws Exception {
    String projectStepCommentId = "e6c8acd6-a7bb-48a6-bad7-e4154b5010f0";
    String url = BASE_URL + "/" + projectStepCommentId;
    ProjectStepCommentEditRequestDTO request =
        ProjectStepCommentEditRequestDTO.builder()
            .comment("updated comment")
            .stepId(UUID.fromString("5e47e5c5-4a3d-4e67-b41d-a04f94c6b93a"))
            .employeeId(UUID.fromString("a5cb9e8c-a9cb-4657-b1e4-1dec8bcdf997"))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                put(url)
                    .with(withBadJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_404_editProjectStepCommentById() throws Exception {
    String projectStepCommentId = "2b7f864c-6bc0-4ff9-aae5-bd2bd5a872ca";
    String url = BASE_URL + "/" + projectStepCommentId;
    ProjectStepCommentEditRequestDTO request =
        ProjectStepCommentEditRequestDTO.builder()
            .comment("updated comment")
            .stepId(UUID.fromString("5e47e5c5-4a3d-4e67-b41d-a04f94c6b93a"))
            .employeeId(UUID.fromString("a5cb9e8c-a9cb-4657-b1e4-1dec8bcdf997"))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                put(url)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(
        MSG.PROJECT_STEP_COMMENT_NOT_FOUND + projectStepCommentId, responseBody.getMessage());
  }
}
