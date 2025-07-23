package com.pawelnu.projectmanager.endpoints.project;

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
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.ReactAdminBadRequestError;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.Utils;
import com.pawelnu.projectmanager.utils.Utils.Postgres;
import com.pawelnu.projectmanager.utils.Utils.SpringDataSource;
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
class ProjectControllerTest {

  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_PROJECTS;

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
    ProjectCreateRequestDTO request =
        ProjectCreateRequestDTO.builder()
            .name("test project name")
            .categoryValueId(UUID.fromString("84c3a3ac-c2d1-499e-8e33-c495d1faf1a7"))
            .companyId(UUID.fromString("78d3f0da-e4b5-4885-8c2a-24b1f85afe44"))
            .assignedEmployeeId(UUID.fromString("9f93938c-38f4-4472-b166-5ad41437db6e"))
            .priorityValueId(UUID.fromString("15207afc-0a06-4d5f-a086-fe98cccb341c"))
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
    ProjectDTO responseBody = objectMapper.readValue(contentAsString, ProjectDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertEquals("Test Company", responseBody.getCompanyName());
    assertEquals(request.getName(), responseBody.getName());
    assertEquals("project category", responseBody.getCategoryName());
    assertEquals(request.getCategoryValueId(), responseBody.getCategoryValueId());
    assertEquals("R&D", responseBody.getCategoryValue());
    assertEquals("project priority", responseBody.getPriorityName());
    assertEquals(request.getPriorityValueId(), responseBody.getPriorityValueId());
    assertEquals("LOW", responseBody.getPriorityValue());
    assertEquals(request.getAssignedEmployeeId(), responseBody.getAssignedEmployeeId());
    assertEquals("Todd Kunde", responseBody.getAssignedEmployee());
  }

  @Test
  void shouldReturn_400_createProject() throws Exception {
    ProjectCreateRequestDTO request =
        ProjectCreateRequestDTO.builder()
            .name("test")
            .categoryValueId(UUID.fromString("84c3a3ac-c2d1-499e-8e33-c495d1faf1a7"))
            .companyId(UUID.fromString("78d3f0da-e4b5-4885-8c2a-24b1f85afe44"))
            .assignedEmployeeId(UUID.fromString("9f93938c-38f4-4472-b166-5ad41437db6e"))
            .priorityValueId(UUID.fromString("15207afc-0a06-4d5f-a086-fe98cccb341c"))
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
    assertEquals("Project name has to have 5-255 characters", responseBody.getErrors().get("name"));
  }

  @Test
  void shouldReturn_401_createProject() throws Exception {
    ProjectCreateRequestDTO request =
        ProjectCreateRequestDTO.builder()
            .name("test project name")
            .categoryValueId(UUID.fromString("84c3a3ac-c2d1-499e-8e33-c495d1faf1a7"))
            .companyId(UUID.fromString("78d3f0da-e4b5-4885-8c2a-24b1f85afe44"))
            .assignedEmployeeId(UUID.fromString("9f93938c-38f4-4472-b166-5ad41437db6e"))
            .priorityValueId(UUID.fromString("15207afc-0a06-4d5f-a086-fe98cccb341c"))
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
    ProjectCreateRequestDTO request =
        ProjectCreateRequestDTO.builder()
            .name("test project name")
            .categoryValueId(UUID.fromString("84c3a3ac-c2d1-499e-8e33-c495d1faf1a7"))
            .companyId(UUID.fromString("78d3f0da-e4b5-4885-8c2a-24b1f85afe44"))
            .assignedEmployeeId(UUID.fromString("9f93938c-38f4-4472-b166-5ad41437db6e"))
            .priorityValueId(UUID.fromString("15207afc-0a06-4d5f-a086-fe98cccb341c"))
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

  @Test
  void shouldReturn_200_getProjectList() throws Exception {
    List<String> range = List.of("0", "1");
    String rangeString = objectMapper.writeValueAsString(range);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("range", rangeString)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<ProjectDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-1", headerContentRange.substring(0, 9));
    assertEquals(2, responseBody.size());
  }

  @Test
  void shouldReturn_200_getProjectList_withFilters() throws Exception {
    Map<String, String> filter = Map.of("name", "api", "categoryValue", "r&d");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<ProjectDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-0/1", headerContentRange);
    assertEquals(1, responseBody.size());
    assertEquals("R&D", responseBody.getFirst().getCategoryValue());
    assertEquals("Test Company", responseBody.getFirst().getCompanyName());
    assertEquals("test test", responseBody.getFirst().getAssignedEmployee());
    assertEquals("VERY_LOW", responseBody.getFirst().getPriorityValue());
  }

  @Test
  void shouldReturn_200_getProjectList_withFiltersAndSort() throws Exception {
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
    List<ProjectDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-1/2", headerContentRange);
    assertEquals(2, responseBody.size());
    assertEquals("migrate database", responseBody.getFirst().getName());
  }

  @Test
  void shouldReturn_200_getProjectList_withRange() throws Exception {
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
    List<ProjectDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(1, responseBody.size());
    assertEquals("deploy api", responseBody.getFirst().getName());
  }

  @Test
  void shouldReturn_200_getProjectList_emptyResult() throws Exception {
    Map<String, String> filter = Map.of("name", "not exists");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<ProjectDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0--1/0", headerContentRange);
    assertEquals(0, responseBody.size());
  }

  @Test
  void shouldReturn_401_getProjectList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_getProjectList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_200_getProjectById() throws Exception {
    String projectId = "dedeabdb-948c-46b2-b1aa-be34df8fae58";
    String url = BASE_URL + "/" + projectId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ProjectDTO responseBody = objectMapper.readValue(contentAsString, ProjectDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(UUID.fromString(projectId), responseBody.getId());
    assertEquals("develop frontend app", responseBody.getName());
    assertEquals("INTERNAL", responseBody.getCategoryValue());
    assertEquals("Hintz, Parisian and Sanford", responseBody.getCompanyName());
    assertEquals("Jerrold Brakus", responseBody.getAssignedEmployee());
    assertEquals("MEDIUM", responseBody.getPriorityValue());
  }

  @Test
  void shouldReturn_400_getProjectById() throws Exception {
    String projectId = "invalid-uuid";
    String url = BASE_URL + "/" + projectId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(invalidUUIDError(), responseBody);
  }

  @Test
  void shouldReturn_401_getProjectById() throws Exception {
    String projectId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + projectId;
    MvcResult response = mockMvc.perform(get(url)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_getProjectById() throws Exception {
    String projectId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + projectId;
    MvcResult response = mockMvc.perform(get(url).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_404_getProjectById() throws Exception {
    String projectId = "bbc5d705-bfdf-4314-b926-30371ef10682";
    String url = BASE_URL + "/" + projectId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.PROJECT_NOT_FOUND + projectId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_getProjectById_isDeletedTrue() throws Exception {
    String projectId = "bbc5d705-bfdf-4314-b926-30371ef10682";
    String url = BASE_URL + "/" + projectId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.PROJECT_NOT_FOUND + projectId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_200_editProjectById() throws Exception {
    String projectId = "b4b7091a-0dc5-463c-b009-a7ffb1159e0d";
    String url = BASE_URL + "/" + projectId;
    ProjectEditRequestDTO request =
        ProjectEditRequestDTO.builder()
            .name("test project name")
            .categoryValueId(UUID.fromString("84c3a3ac-c2d1-499e-8e33-c495d1faf1a7"))
            .companyId(UUID.fromString("78d3f0da-e4b5-4885-8c2a-24b1f85afe44"))
            .assignedEmployeeId(UUID.fromString("9f93938c-38f4-4472-b166-5ad41437db6e"))
            .priorityValueId(UUID.fromString("15207afc-0a06-4d5f-a086-fe98cccb341c"))
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
    ProjectDTO responseBody = objectMapper.readValue(contentAsString, ProjectDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    //    assertEquals(request.getFirstName(), responseBody.getFirstName());
    //    assertEquals(request.getLastName(), responseBody.getLastName());
    //    assertEquals(request.getUsername(), responseBody.getUsername());
    //    assertEquals(request.getEmail(), responseBody.getEmail());
    //    assertEquals(request.getPhoneNumber(), responseBody.getPhoneNumber());
    assertEquals("Brakus and Sons", responseBody.getCompanyName());
  }

  @Test
  void shouldReturn_400_editProjectById() throws Exception {
    String projectId = "invalid-uuid";
    String url = BASE_URL + "/" + projectId;
    ProjectEditRequestDTO request =
        ProjectEditRequestDTO.builder()
            .name("test project name")
            .categoryValueId(UUID.fromString("84c3a3ac-c2d1-499e-8e33-c495d1faf1a7"))
            .companyId(UUID.fromString("78d3f0da-e4b5-4885-8c2a-24b1f85afe44"))
            .assignedEmployeeId(UUID.fromString("9f93938c-38f4-4472-b166-5ad41437db6e"))
            .priorityValueId(UUID.fromString("15207afc-0a06-4d5f-a086-fe98cccb341c"))
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
  void shouldReturn_401_editProjectById() throws Exception {
    String projectId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
    String url = BASE_URL + "/" + projectId;
    ProjectEditRequestDTO request =
        ProjectEditRequestDTO.builder()
            .name("test project name")
            .categoryValueId(UUID.fromString("84c3a3ac-c2d1-499e-8e33-c495d1faf1a7"))
            .companyId(UUID.fromString("78d3f0da-e4b5-4885-8c2a-24b1f85afe44"))
            .assignedEmployeeId(UUID.fromString("9f93938c-38f4-4472-b166-5ad41437db6e"))
            .priorityValueId(UUID.fromString("15207afc-0a06-4d5f-a086-fe98cccb341c"))
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
  void shouldReturn_403_editProjectById() throws Exception {
    String projectId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
    String url = BASE_URL + "/" + projectId;
    ProjectEditRequestDTO request =
        ProjectEditRequestDTO.builder()
            .name("test project name")
            .categoryValueId(UUID.fromString("84c3a3ac-c2d1-499e-8e33-c495d1faf1a7"))
            .companyId(UUID.fromString("78d3f0da-e4b5-4885-8c2a-24b1f85afe44"))
            .assignedEmployeeId(UUID.fromString("9f93938c-38f4-4472-b166-5ad41437db6e"))
            .priorityValueId(UUID.fromString("15207afc-0a06-4d5f-a086-fe98cccb341c"))
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
  void shouldReturn_404_editProjectById() throws Exception {
    String projectId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
    String url = BASE_URL + "/" + projectId;
    ProjectEditRequestDTO request =
        ProjectEditRequestDTO.builder()
            .name("test project name")
            .categoryValueId(UUID.fromString("84c3a3ac-c2d1-499e-8e33-c495d1faf1a7"))
            .companyId(UUID.fromString("78d3f0da-e4b5-4885-8c2a-24b1f85afe44"))
            .assignedEmployeeId(UUID.fromString("9f93938c-38f4-4472-b166-5ad41437db6e"))
            .priorityValueId(UUID.fromString("15207afc-0a06-4d5f-a086-fe98cccb341c"))
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
    assertEquals(MSG.PROJECT_NOT_FOUND + projectId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_200_deleteProjectById_isDeletedFalse() throws Exception {
    String projectId = "a3f36913-36f6-4215-9f24-06323c990608";
    String url = BASE_URL + "/" + projectId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("Deleted project with id: " + projectId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_400_deleteProjectById_isDeletedFalse() throws Exception {
    String projectId = "invalid-uuid";
    String url = BASE_URL + "/" + projectId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(invalidUUIDError(), responseBody);
  }

  @Test
  void shouldReturn_401_deleteProjectById_isDeletedFalse() throws Exception {
    String projectId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + projectId;
    MvcResult response =
        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_deleteProjectById_isDeletedFalse() throws Exception {
    String projectId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + projectId;
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
  void shouldReturn_404_deleteProjectById_isDeletedFalse() throws Exception {
    String projectId = "4c7a6cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + projectId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.PROJECT_NOT_FOUND + projectId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_deleteProjectById_isDeletedTrue() throws Exception {
    String projectId = "bbc5d705-bfdf-4314-b926-30371ef10682";
    String url = BASE_URL + "/" + projectId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.PROJECT_NOT_FOUND + projectId, responseBody.getMessage());
  }

  // TODO test getList()
  // TODO test getById()
  // TODO test editById()
  // TODO test deleteById()
}
