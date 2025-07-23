package com.pawelnu.projectmanager.endpoints.company.employee;

import static com.pawelnu.projectmanager.utils.Utils.accessDeniedError;
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
import com.pawelnu.projectmanager.endpoints.company.employee.dto.EmployeeCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.dto.EmployeeDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.dto.EmployeeEditRequestDTO;
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
class EmployeeControllerTest {
  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_EMPLOYEES;

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
    assertEquals(unauthorizedError(), responseBody);
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

  @Test
  void shouldReturn_200_getEmployeeList() throws Exception {
    List<String> range = List.of("0", "1");
    String rangeString = objectMapper.writeValueAsString(range);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("range", rangeString)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<EmployeeDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-1", headerContentRange.substring(0, 9));
    assertEquals(2, responseBody.size());
  }

  @Test
  void shouldReturn_200_getEmployeeList_withFilters() throws Exception {
    Map<String, String> filter = Map.of("companyName", "sons", "firstName", "tom");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<EmployeeDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-1/2", headerContentRange);
    assertEquals(2, responseBody.size());
    assertEquals("Brakus and Sons", responseBody.getFirst().getCompanyName());
    assertEquals("Tom", responseBody.getFirst().getFirstName());
    assertEquals("Keeling", responseBody.getFirst().getLastName());
  }

  @Test
  void shouldReturn_200_getEmployeeList_withFiltersAndSort() throws Exception {
    List<String> sort = List.of("lastName", "DESC");
    Map<String, String> filter = Map.of("companyName", "boy%sons");
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
    List<EmployeeDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-1/2", headerContentRange);
    assertEquals(2, responseBody.size());
    assertEquals("Lean_Bruen23156", responseBody.getFirst().getUsername());
  }

  @Test
  void shouldReturn_200_getEmployeeList_withRange() throws Exception {
    List<String> range = List.of("0", "0");
    List<String> sort = List.of("lastName", "ASC");
    String rangeString = objectMapper.writeValueAsString(range);
    String sortString = objectMapper.writeValueAsString(sort);
    MvcResult response =
        mockMvc
            .perform(
                get(BASE_URL).with(withJwt()).param("sort", sortString).param("range", rangeString))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    List<EmployeeDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(1, responseBody.size());
    assertEquals("Dwain", responseBody.getFirst().getFirstName());
  }

  @Test
  void shouldReturn_200_getEmployeeList_emptyResult() throws Exception {
    Map<String, String> filter = Map.of("lastName", "not exists");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<EmployeeDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0--1/0", headerContentRange);
    assertEquals(0, responseBody.size());
  }

  @Test
  void shouldReturn_401_getEmployeeList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_getEmployeeList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_200_getEmployeeById() throws Exception {
    String employeeId = "94759f68-0be8-403e-803a-cd6b9cfa1c8f";
    String url = BASE_URL + "/" + employeeId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    EmployeeDTO responseBody = objectMapper.readValue(contentAsString, EmployeeDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(UUID.fromString(employeeId), responseBody.getId());
    assertEquals("Georgine", responseBody.getFirstName());
    assertEquals("Welch", responseBody.getLastName());
    assertEquals("Georgine_Welch85182", responseBody.getUsername());
    assertEquals("georgine.welch@example.com", responseBody.getEmail());
    assertEquals("(219) 720-6247", responseBody.getPhoneNumber());
    assertEquals("Wolff-Effertz", responseBody.getCompanyName());
  }

  @Test
  void shouldReturn_400_getEmployeeById() throws Exception {
    String employeeId = "invalid-uuid";
    String url = BASE_URL + "/" + employeeId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(MSG.INVALID_UUID, responseBody.getMessage());
  }

  @Test
  void shouldReturn_401_getEmployeeById() throws Exception {
    String employeeId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + employeeId;
    MvcResult response = mockMvc.perform(get(url)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_getEmployeeById() throws Exception {
    String employeeId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + employeeId;
    MvcResult response = mockMvc.perform(get(url).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_404_getEmployeeById() throws Exception {
    String employeeId = "bbc5d705-bfdf-4314-b926-30371ef10682";
    String url = BASE_URL + "/" + employeeId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.EMPLOYEE_NOT_FOUND + employeeId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_getEmployeeById_isDeletedTrue() throws Exception {
    String employeeId = "bbc5d705-bfdf-4314-b926-30371ef10682";
    String url = BASE_URL + "/" + employeeId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.EMPLOYEE_NOT_FOUND + employeeId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_200_editEmployeeById() throws Exception {
    String employeeId = "b4b7091a-0dc5-463c-b009-a7ffb1159e0d";
    String url = BASE_URL + "/" + employeeId;
    EmployeeEditRequestDTO request =
        EmployeeEditRequestDTO.builder()
            .firstName("updated firstName")
            .lastName("updated lastName")
            .username("upadted username")
            .email("updated.email@.com")
            .phoneNumber("123-123-123")
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
    EmployeeDTO responseBody = objectMapper.readValue(contentAsString, EmployeeDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(request.getFirstName(), responseBody.getFirstName());
    assertEquals(request.getLastName(), responseBody.getLastName());
    assertEquals(request.getUsername(), responseBody.getUsername());
    assertEquals(request.getEmail(), responseBody.getEmail());
    assertEquals(request.getPhoneNumber(), responseBody.getPhoneNumber());
    assertEquals("Brakus and Sons", responseBody.getCompanyName());
  }

  @Test
  void shouldReturn_400_editEmployeeById() throws Exception {
    String employeeId = "invalid-uuid";
    String url = BASE_URL + "/" + employeeId;
    EmployeeEditRequestDTO request =
        EmployeeEditRequestDTO.builder()
            .firstName("updated firstName")
            .lastName("updated lastName")
            .username("upadted username")
            .email("updated.email@.com")
            .phoneNumber("123-123-123")
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
    assertEquals(MSG.INVALID_UUID, responseBody.getMessage());
  }

  @Test
  void shouldReturn_401_editEmployeeById() throws Exception {
    String employeeId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
    String url = BASE_URL + "/" + employeeId;
    EmployeeEditRequestDTO request =
        EmployeeEditRequestDTO.builder()
            .firstName("updated firstName")
            .lastName("updated lastName")
            .username("upadted username")
            .email("updated.email@.com")
            .phoneNumber("123-123-123")
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
  void shouldReturn_403_editEmployeeById() throws Exception {
    String employeeId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
    String url = BASE_URL + "/" + employeeId;
    EmployeeEditRequestDTO request =
        EmployeeEditRequestDTO.builder()
            .firstName("updated firstName")
            .lastName("updated lastName")
            .username("upadted username")
            .email("updated.email@.com")
            .phoneNumber("123-123-123")
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
  void shouldReturn_404_editEmployeeById() throws Exception {
    String employeeId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
    String url = BASE_URL + "/" + employeeId;
    EmployeeEditRequestDTO request =
        EmployeeEditRequestDTO.builder()
            .firstName("updated firstName")
            .lastName("updated lastName")
            .username("upadted username")
            .email("updated.email@.com")
            .phoneNumber("123-123-123")
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
    assertEquals(MSG.EMPLOYEE_NOT_FOUND + employeeId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_200_deleteEmployeeById_isDeletedFalse() throws Exception {
    String employeeId = "a3f36913-36f6-4215-9f24-06323c990608";
    String url = BASE_URL + "/" + employeeId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("Deleted employee with id: " + employeeId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_400_deleteEmployeeById_isDeletedFalse() throws Exception {
    String employeeId = "invalid-uuid";
    String url = BASE_URL + "/" + employeeId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(MSG.INVALID_UUID, responseBody.getMessage());
  }

  @Test
  void shouldReturn_401_deleteEmployeeById_isDeletedFalse() throws Exception {
    String employeeId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + employeeId;
    MvcResult response =
        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_deleteEmployeeById_isDeletedFalse() throws Exception {
    String employeeId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + employeeId;
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
  void shouldReturn_404_deleteEmployeeById_isDeletedFalse() throws Exception {
    String employeeId = "4c7a6cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + employeeId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.EMPLOYEE_NOT_FOUND + employeeId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_deleteEmployeeById_isDeletedTrue() throws Exception {
    String employeeId = "bbc5d705-bfdf-4314-b926-30371ef10682";
    String url = BASE_URL + "/" + employeeId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.EMPLOYEE_NOT_FOUND + employeeId, responseBody.getMessage());
  }
}
