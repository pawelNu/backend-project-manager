package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import static com.pawelnu.projectmanager.utils.Utils.FULL_AUTH_IS_REQUIRED;
import static com.pawelnu.projectmanager.utils.Utils.accessDeniedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.dto.EmployeeAuthoritiesDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.dto.EmployeeAuthorityCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.dto.EmployeeAuthorityDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.dto.EmployeeAuthorityDeleteRequestDTO;
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

  // FIXME test
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

  @Test
  void shouldReturn_200_getEmployeeAuthorityList() throws Exception {
    List<String> range = List.of("0", "1");
    String rangeString = objectMapper.writeValueAsString(range);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("range", rangeString)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<EmployeeAuthorityDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-1", headerContentRange.substring(0, 9));
    assertEquals(2, responseBody.size());
  }

  @Test
  void shouldReturn_200_getEmployeeAuthorityList_withFilters() throws Exception {
    Map<String, String> filter = Map.of("authorityName", "value", "employeeLastName", "test");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<EmployeeAuthorityDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-4/5", headerContentRange);
    assertEquals(5, responseBody.size());
    assertEquals("test", responseBody.getFirst().getUsername());
    // FIXME test
    assertEquals("CATEGORY_VALUE_GET_BY_ID", responseBody.getFirst().getAuthorityNameBackend());
  }

  @Test
  void shouldReturn_200_getEmployeeAuthorityList_withFiltersAndSort() throws Exception {
    List<String> sort = List.of("employeeLastName", "DESC");
    Map<String, String> filter = Map.of("authorityName", "item");
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
    List<EmployeeAuthorityDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-0/1", headerContentRange);
    assertEquals(1, responseBody.size());
    assertEquals("withAuthToDelete", responseBody.getFirst().getUsername());
    assertEquals("ITEM_TO_FILTER", responseBody.getFirst().getAuthorityNameBackend());
  }

  @Test
  void shouldReturn_200_getEmployeeAuthorityList_withRange() throws Exception {
    List<String> range = List.of("0", "0");
    List<String> sort = List.of("employeeLastName", "ASC");
    String rangeString = objectMapper.writeValueAsString(range);
    String sortString = objectMapper.writeValueAsString(sort);
    MvcResult response =
        mockMvc
            .perform(
                get(BASE_URL).with(withJwt()).param("sort", sortString).param("range", rangeString))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    List<EmployeeAuthorityDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(1, responseBody.size());
    assertEquals("test", responseBody.getFirst().getUsername());
    // FIXME test
    assertEquals("AUTHORITY_GET_BY_ID", responseBody.getFirst().getAuthorityNameBackend());
  }

  @Test
  void shouldReturn_200_getEmployeeAuthorityList_emptyResult() throws Exception {
    Map<String, String> filter = Map.of("employeeLastName", "not exists");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<EmployeeAuthorityDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0--1/0", headerContentRange);
    assertEquals(0, responseBody.size());
  }

  @Test
  void shouldReturn_401_getEmployeeAuthorityList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    ReactAdminError expectedResponse =
        new ReactAdminError("Full authentication is required to access this resource");
    assertEquals(expectedResponse, responseBody);
  }

  @Test
  void shouldReturn_403_getEmployeeAuthorityList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_200_deleteEmployeeAuthority_isDeletedFalse() throws Exception {
    EmployeeAuthorityDeleteRequestDTO request =
        EmployeeAuthorityDeleteRequestDTO.builder()
            .employeeId(UUID.fromString("2da2d58f-ff96-465a-bd1f-f1a4aabda7ca"))
            .authorityIds(
                List.of(
                    UUID.fromString("c6974c09-8b90-4fa0-a9ac-d59e4ee9598b"),
                    UUID.fromString("142ba2ba-d9af-4d7e-b202-f3dedd55e0e2")))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                delete(BASE_URL)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(
        "Deleted employee authority with id: ca9f1e9d-c912-458a-b23f-e6059cac05bf for employee with"
            + " id: 2da2d58f-ff96-465a-bd1f-f1a4aabda7ca",
        responseBody.getMessage());
  }

  @Test
  void shouldReturn_400_deleteEmployeeAuthority_isDeletedFalse() throws Exception {
    EmployeeAuthorityDeleteRequestDTO request =
        EmployeeAuthorityDeleteRequestDTO.builder()
            .employeeId(UUID.fromString("2da2d58f-ff96-465a-bd1f-f1a4aabda7ca"))
            .authorityIds(null)
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                delete(BASE_URL)
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
  void shouldReturn_401_deleteEmployeeAuthority_isDeletedFalse() throws Exception {
    EmployeeAuthorityDeleteRequestDTO request =
        EmployeeAuthorityDeleteRequestDTO.builder()
            .employeeId(UUID.fromString("2da2d58f-ff96-465a-bd1f-f1a4aabda7ca"))
            .authorityIds(
                List.of(
                    UUID.fromString("c6974c09-8b90-4fa0-a9ac-d59e4ee9598b"),
                    UUID.fromString("142ba2ba-d9af-4d7e-b202-f3dedd55e0e2")))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(delete(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(Utils.FULL_AUTH_IS_REQUIRED, responseBody.getMessage());
  }

  @Test
  void shouldReturn_403_deleteEmployeeAuthority_isDeletedFalse() throws Exception {
    EmployeeAuthorityDeleteRequestDTO request =
        EmployeeAuthorityDeleteRequestDTO.builder()
            .employeeId(UUID.fromString("2da2d58f-ff96-465a-bd1f-f1a4aabda7ca"))
            .authorityIds(
                List.of(
                    UUID.fromString("c6974c09-8b90-4fa0-a9ac-d59e4ee9598b"),
                    UUID.fromString("142ba2ba-d9af-4d7e-b202-f3dedd55e0e2")))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                delete(BASE_URL)
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
  void shouldReturn_404_deleteEmployeeAuthority_isDeletedFalse() throws Exception {
    EmployeeAuthorityDeleteRequestDTO request =
        EmployeeAuthorityDeleteRequestDTO.builder()
            .employeeId(UUID.fromString("2da2d58f-ff96-465a-bd1f-f1a4aabda7ca"))
            .authorityIds(
                List.of(
                    UUID.fromString("c6974c09-8b90-4fa0-a9ac-d59e4ee9598b"),
                    UUID.fromString("142ba2ba-d9af-4d7e-b202-f3dedd55e0e2")))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                delete(BASE_URL)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(
        MSG.EMPLOYEE_AUTHORITIES_NOT_FOUND_MSG
            + "c6974c09-8b90-4fa0-a9ac-d59e4ee9598b, 142ba2ba-d9af-4d7e-b202-f3dedd55e0e2",
        responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_deleteEmployeeAuthority_isDeletedTrue() throws Exception {
    EmployeeAuthorityDeleteRequestDTO request =
        EmployeeAuthorityDeleteRequestDTO.builder()
            .employeeId(UUID.fromString("2da2d58f-ff96-465a-bd1f-f1a4aabda7ca"))
            .authorityIds(
                List.of(
                    UUID.fromString("45b037b8-0026-4625-8b84-33776ddd585f"),
                    UUID.fromString("4961b918-11c3-48fd-8ae4-dc2695866a6d")))
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(
                delete(BASE_URL)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(
        MSG.EMPLOYEE_AUTHORITIES_NOT_FOUND_MSG
            + "45b037b8-0026-4625-8b84-33776ddd585f, 4961b918-11c3-48fd-8ae4-dc2695866a6d",
        responseBody.getMessage());
  }
}
