package com.pawelnu.projectmanager.endpoints.project.step;

import static com.pawelnu.projectmanager.utils.Utils.accessDeniedError;
import static com.pawelnu.projectmanager.utils.Utils.invalidUUIDError;
import static com.pawelnu.projectmanager.utils.Utils.unauthorizedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepEditRequestDTO;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.ReactAdminBadRequestError;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.Utils;
import com.pawelnu.projectmanager.utils.Utils.Postgres;
import com.pawelnu.projectmanager.utils.Utils.SpringDataSource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
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
class ProjectStepControllerTest {
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

  // TODO check all getProjectStepList tests
  @Test
  void shouldReturn_200_getProjectStepList() throws Exception {
    List<String> range = List.of("0", "1");
    String rangeString = objectMapper.writeValueAsString(range);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("range", rangeString)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<ProjectStepDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-1", headerContentRange.substring(0, 9));
    assertEquals(2, responseBody.size());
  }

  @Test
  void shouldReturn_200_getProjectStepList_withFilters() throws Exception {
    Map<String, String> filter = Map.of("name", "3", "projectName", "steps");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<ProjectStepDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-0/1", headerContentRange);
    assertEquals(1, responseBody.size());
    assertEquals("step 3", responseBody.getFirst().getName());
    assertEquals("steps checker", responseBody.getFirst().getProjectName());
    assertEquals("VERY_LOW", responseBody.getFirst().getPriorityValue());
    assertEquals("Alleen Koepp", responseBody.getFirst().getAssignedEmployee());
    assertEquals(Instant.parse("2025-10-25T00:00:00Z"), responseBody.getFirst().getDeadline());
  }

  @Test
  void shouldReturn_200_getProjectStepList_withFiltersAndSort() throws Exception {
    List<String> sort = List.of("name", "DESC");
    Map<String, String> filter = Map.of("priorityValue", "low");
    String sortString = objectMapper.writeValueAsString(sort);
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc
            .perform(
                get(BASE_URL)
                    .with(withJwt())
                    .param("sort", sortString)
                    .param("filter", filterStrig))
            .andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<ProjectStepDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-3/4", headerContentRange); // FIXME test
    assertEquals(4, responseBody.size());
    assertEquals("step 3", responseBody.getFirst().getName());
  }

  @Test
  void shouldReturn_200_getProjectStepList_withRange() throws Exception {
    List<String> range = List.of("0", "0");
    List<String> sort = List.of("name", "ASC");
    String rangeString = objectMapper.writeValueAsString(range);
    String sortString = objectMapper.writeValueAsString(sort);
    MvcResult response =
        mockMvc
            .perform(
                get(BASE_URL).with(withJwt()).param("sort", sortString).param("range", rangeString))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    List<ProjectStepDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(1, responseBody.size());
    assertEquals("step 1", responseBody.getFirst().getName());
  }

  @Test
  void shouldReturn_200_getProjectStepList_emptyResult() throws Exception {
    Map<String, String> filter = Map.of("name", "not exists");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<ProjectStepDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0--1/0", headerContentRange);
    assertEquals(0, responseBody.size());
  }

  @Test
  void shouldReturn_401_getProjectStepList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_getProjectStepList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_200_getProjectStepById() throws Exception {
    String projectStepId = "0565848e-6138-413c-a80b-e53153f30f89";
    String url = BASE_URL + "/" + projectStepId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ProjectStepDTO responseBody = objectMapper.readValue(contentAsString, ProjectStepDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(UUID.fromString(projectStepId), responseBody.getId());
    assertEquals("step 1", responseBody.getName());
    assertEquals("steps checker", responseBody.getProjectName());
    assertEquals("VERY_LOW", responseBody.getPriorityValue());
    assertEquals("Alleen Koepp", responseBody.getAssignedEmployee());
    assertEquals(Instant.parse("2025-08-25T00:00:00Z"), responseBody.getDeadline());
  }

  @Test
  void shouldReturn_400_getProjectStepById() throws Exception {
    String projectStepId = "invalid-uuid";
    String url = BASE_URL + "/" + projectStepId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(invalidUUIDError(), responseBody);
  }

  @Test
  void shouldReturn_401_getProjectStepById() throws Exception {
    String projectStepId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + projectStepId;
    MvcResult response = mockMvc.perform(get(url)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_getProjectStepById() throws Exception {
    String projectStepId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + projectStepId;
    MvcResult response = mockMvc.perform(get(url).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_404_getProjectStepById() throws Exception {
    String projectStepId = "89258385-aa00-47d3-bafe-88bc1d56e6ee";
    String url = BASE_URL + "/" + projectStepId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.PROJECT_STEP_NOT_FOUND + projectStepId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_getProjectStepById_isDeletedTrue() throws Exception {
    String projectStepId = "9d93bd6d-e48a-4bad-b4f5-6f71d27c0342";
    String url = BASE_URL + "/" + projectStepId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.PROJECT_STEP_NOT_FOUND + projectStepId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_200_editProjectStepById() throws Exception {
    String projectStepId = "e6c8acd6-a7bb-48a6-bad7-e4154b5010f0";
    String url = BASE_URL + "/" + projectStepId;
    Instant deadline = LocalDateTime.of(2025, 7, 29, 12, 0).toInstant(ZoneOffset.UTC);
    ProjectStepEditRequestDTO request =
        ProjectStepEditRequestDTO.builder()
            .name("updated step for test")
            .projectId(UUID.fromString("bc6b9fee-e9d7-4692-853e-d6d2c00383b3"))
            .priorityValueId(UUID.fromString("26da6b3c-2079-47cf-a00f-06ea28702eb5"))
            .assignedEmployeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .deadline(deadline)
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
    ProjectStepDTO responseBody = objectMapper.readValue(contentAsString, ProjectStepDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(request.getName(), responseBody.getName());
    assertEquals("project for project steps tetes", responseBody.getProjectName());
    assertEquals("MEDIUM", responseBody.getPriorityValue());
    assertEquals("Janita Jakubowski", responseBody.getAssignedEmployee());
    assertEquals(Instant.parse("2025-07-29T12:00:00Z"), responseBody.getDeadline());
  }

  @Test
  void shouldReturn_400_editProjectStepById() throws Exception {
    String projectStepId = "invalid-uuid";
    String url = BASE_URL + "/" + projectStepId;
    Instant deadline = LocalDateTime.of(2025, 7, 29, 12, 0).toInstant(ZoneOffset.UTC);
    ProjectStepEditRequestDTO request =
        ProjectStepEditRequestDTO.builder()
            .name("updated step for test")
            .projectId(UUID.fromString("bc6b9fee-e9d7-4692-853e-d6d2c00383b3"))
            .priorityValueId(UUID.fromString("26da6b3c-2079-47cf-a00f-06ea28702eb5"))
            .assignedEmployeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .deadline(deadline)
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
  void shouldReturn_401_editProjectStepById() throws Exception {
    String projectStepId = "e6c8acd6-a7bb-48a6-bad7-e4154b5010f0";
    String url = BASE_URL + "/" + projectStepId;
    Instant deadline = LocalDateTime.of(2025, 7, 29, 12, 0).toInstant(ZoneOffset.UTC);
    ProjectStepEditRequestDTO request =
        ProjectStepEditRequestDTO.builder()
            .name("updated step for test")
            .projectId(UUID.fromString("bc6b9fee-e9d7-4692-853e-d6d2c00383b3"))
            .priorityValueId(UUID.fromString("26da6b3c-2079-47cf-a00f-06ea28702eb5"))
            .assignedEmployeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .deadline(deadline)
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
  void shouldReturn_403_editProjectStepById() throws Exception {
    String projectStepId = "e6c8acd6-a7bb-48a6-bad7-e4154b5010f0";
    String url = BASE_URL + "/" + projectStepId;
    Instant deadline = LocalDateTime.of(2025, 7, 29, 12, 0).toInstant(ZoneOffset.UTC);
    ProjectStepEditRequestDTO request =
        ProjectStepEditRequestDTO.builder()
            .name("updated step for test")
            .projectId(UUID.fromString("bc6b9fee-e9d7-4692-853e-d6d2c00383b3"))
            .priorityValueId(UUID.fromString("26da6b3c-2079-47cf-a00f-06ea28702eb5"))
            .assignedEmployeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .deadline(deadline)
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
  void shouldReturn_404_editProjectStepById() throws Exception {
    String projectStepId = "2b7f864c-6bc0-4ff9-aae5-bd2bd5a872ca";
    String url = BASE_URL + "/" + projectStepId;
    Instant deadline = LocalDateTime.of(2025, 7, 29, 12, 0).toInstant(ZoneOffset.UTC);
    ProjectStepEditRequestDTO request =
        ProjectStepEditRequestDTO.builder()
            .name("updated step for test")
            .projectId(UUID.fromString("bc6b9fee-e9d7-4692-853e-d6d2c00383b3"))
            .priorityValueId(UUID.fromString("26da6b3c-2079-47cf-a00f-06ea28702eb5"))
            .assignedEmployeeId(UUID.fromString("cff1680a-e821-4218-8ec6-b0b4ab941fb0"))
            .deadline(deadline)
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
    assertEquals(MSG.PROJECT_STEP_NOT_FOUND + projectStepId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_200_deleteProjectStepById_isDeletedFalse() throws Exception {
    String projectStepId = "fb91749e-5a5f-4953-bb85-b176d98fb2fa";
    String url = BASE_URL + "/" + projectStepId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("Deleted project step with id: " + projectStepId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_400_deleteProjectStepById_isDeletedFalse() throws Exception {
    String projectStepId = "invalid-uuid";
    String url = BASE_URL + "/" + projectStepId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(invalidUUIDError(), responseBody);
  }

  @Test
  void shouldReturn_401_deleteProjectStepById_isDeletedFalse() throws Exception {
    String projectStepId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + projectStepId;
    MvcResult response =
        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_deleteProjectStepById_isDeletedFalse() throws Exception {
    String projectStepId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + projectStepId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withBadJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_404_deleteProjectStepById_isDeletedFalse() throws Exception {
    String projectStepId = "4768856c-6cc0-40ad-8106-58ad8d2e9923";
    String url = BASE_URL + "/" + projectStepId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.PROJECT_STEP_NOT_FOUND + projectStepId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_deleteProjectStepById_isDeletedTrue() throws Exception {
    String projectStepId = "fda8d9fd-fe63-4b89-9c9a-96cd33e616ff";
    String url = BASE_URL + "/" + projectStepId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.PROJECT_STEP_NOT_FOUND + projectStepId, responseBody.getMessage());
  }
}
