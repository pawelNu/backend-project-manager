package com.pawelnu.projectmanager.endpoints.authority;

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
import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityDTO;
import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityEditRequestDTO;
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
class AuthorityControllerTest {

  public static final String FIELD_NAME_BACKEND = "nameBackend";
  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_AUTHORITIES;
  public static final String ADD_AUTHORITY_TO_EMPLOYEE = BASE_URL + "/add-authority-to-employee";

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
  void shouldReturn_201_createAuthority() throws Exception {
    String name = "FOR_TEST_AUTHORITY";
    AuthorityCreateRequestDTO request =
        AuthorityCreateRequestDTO.builder().nameBackend(name).build();
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
    AuthorityDTO responseBody = objectMapper.readValue(contentAsString, AuthorityDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertEquals(name, responseBody.getNameBackend());
    assertEquals(name.toLowerCase(), responseBody.getNameFrontend());
  }

  @Test
  void shouldReturn_201_createAuthority_getById() throws Exception {
    String name = "FOR_TEST_GET_BY_ID";
    AuthorityCreateRequestDTO request =
        AuthorityCreateRequestDTO.builder().nameBackend(name).build();
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
    AuthorityDTO responseBody = objectMapper.readValue(contentAsString, AuthorityDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertEquals(name, responseBody.getNameBackend());
    assertEquals("for_test_show".toLowerCase(), responseBody.getNameFrontend());
  }

  @Test
  void shouldReturn_201_createAuthority_getById2() throws Exception {
    String name = "FOR_TEST_GET_BY_ID";
    AuthorityCreateRequestDTO request =
        AuthorityCreateRequestDTO.builder().nameBackend(name).nameFrontend(name).build();
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
    AuthorityDTO responseBody = objectMapper.readValue(contentAsString, AuthorityDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertEquals(name, responseBody.getNameBackend());
    assertEquals(name, responseBody.getNameFrontend());
  }

  @Test
  void shouldReturn_400_createAuthority() throws Exception {
    AuthorityCreateRequestDTO request =
        AuthorityCreateRequestDTO.builder().nameBackend("TEST").build();
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
    assertEquals("Name should has 5-255 characters", responseBody.getErrors().get("nameBackend"));
  }

  @Test
  void shouldReturn_401_createAuthority() throws Exception {
    AuthorityCreateRequestDTO request =
        AuthorityCreateRequestDTO.builder().nameBackend("TEST").build();
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
  void shouldReturn_403_createAuthority() throws Exception {
    AuthorityCreateRequestDTO request =
        AuthorityCreateRequestDTO.builder().nameBackend("TEST").build();
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
  void shouldReturn_200_getAuthorityList() throws Exception {
    List<String> range = List.of("0", "19");
    String rangeString = objectMapper.writeValueAsString(range);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("range", rangeString)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<AuthorityDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-19", headerContentRange.substring(0, 10));
    assertEquals(20, responseBody.size());
  }

  @Test
  void shouldReturn_200_getAuthorityList_withFilters() throws Exception {
    String searchString = "AUTHORITIES_DELETE_BY_ID";
    Map<String, String> filter = Map.of(FIELD_NAME_BACKEND, searchString);
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<AuthorityDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-1/2", headerContentRange);
    assertEquals(2, responseBody.size());
    assertEquals(searchString, responseBody.getFirst().getNameBackend());
  }

  @Test
  void shouldReturn_200_getAuthorityList_withFiltersAndSort() throws Exception {
    List<String> sort = List.of(FIELD_NAME_BACKEND, "DESC");
    Map<String, String> filter = Map.of(FIELD_NAME_BACKEND, "authorities_get");
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
    List<AuthorityDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-3/4", headerContentRange);
    assertEquals(4, responseBody.size());
    assertEquals("EMPLOYEE_AUTHORITIES_GET_LIST", responseBody.getFirst().getNameBackend());
  }

  @Test
  void shouldReturn_200_getAuthorityList_withRange() throws Exception {
    List<String> range = List.of("0", "0");
    String rangeString = objectMapper.writeValueAsString(range);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("range", rangeString)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    List<AuthorityDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(1, responseBody.size());
    assertEquals("ADD_ITEM_TO_EMPLOYEE", responseBody.getFirst().getNameBackend());
  }

  @Test
  void shouldReturn_200_getAuthorityList_emptyResult() throws Exception {
    Map<String, String> filter = Map.of(FIELD_NAME_BACKEND, "user");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<AuthorityDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0--1/0", headerContentRange);
    assertEquals(0, responseBody.size());
  }

  @Test
  void shouldReturn_401_getAuthorityList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_getAuthorityList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_200_getAuthorityById() throws Exception {
    String authorityId = "06020f1c-f876-42f6-9dba-e3f0d1e9cd31";
    String url = BASE_URL + "/" + authorityId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    AuthorityDTO responseBody = objectMapper.readValue(contentAsString, AuthorityDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("AUTHORITIES_EDIT_BY_ID", responseBody.getNameBackend());
  }

  @Test
  void shouldReturn_400_getAuthorityById() throws Exception {
    String authorityId = "invalid-uuid";
    String url = BASE_URL + "/" + authorityId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(invalidUUIDError(), responseBody);
  }

  @Test
  void shouldReturn_401_getAuthorityById() throws Exception {
    String authorityId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + authorityId;
    MvcResult response = mockMvc.perform(get(url)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_getAuthorityById() throws Exception {
    String authorityId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + authorityId;
    MvcResult response = mockMvc.perform(get(url).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_404_getAuthorityById() throws Exception {
    String authorityId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + authorityId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.AUTHORITY_NOT_FOUND_MSG + authorityId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_getAuthorityById_isDeletedTrue() throws Exception {
    String authorityId = "84ad8217-9bc4-4244-8d23-d0354ddb9100";
    String url = BASE_URL + "/" + authorityId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.AUTHORITY_NOT_FOUND_MSG + authorityId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_200_editAuthorityById() throws Exception {
    String authorityId = "79700b85-7a8e-4c13-aae6-b2b9955d73ab";
    String url = BASE_URL + "/" + authorityId;
    AuthorityEditRequestDTO request =
        AuthorityEditRequestDTO.builder().nameBackend("ITEM_UPDATED").build();
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
    AuthorityDTO responseBody = objectMapper.readValue(contentAsString, AuthorityDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(UUID.fromString(authorityId), responseBody.getId());
    assertEquals(request.getNameBackend(), responseBody.getNameBackend());
    assertEquals(request.getNameFrontend(), responseBody.getNameFrontend());
  }

  @Test
  void shouldReturn_400_editAuthorityById() throws Exception {
    String authorityId = "invalid-uuid";
    String url = BASE_URL + "/" + authorityId;
    AuthorityEditRequestDTO request =
        AuthorityEditRequestDTO.builder().nameBackend("ITEM_UPDATED").build();
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
  void shouldReturn_401_editAuthorityById() throws Exception {
    String authorityId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
    String url = BASE_URL + "/" + authorityId;
    AuthorityEditRequestDTO request =
        AuthorityEditRequestDTO.builder().nameBackend("ITEM_UPDATED").build();
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
  void shouldReturn_403_editAuthorityById() throws Exception {
    String authorityId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
    String url = BASE_URL + "/" + authorityId;
    AuthorityEditRequestDTO request =
        AuthorityEditRequestDTO.builder().nameBackend("ITEM_UPDATED").build();
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
  void shouldReturn_404_editAuthorityById() throws Exception {
    String authorityId = "84ad8217-9bc4-4244-8d23-d0354ddb9100";
    String url = BASE_URL + "/" + authorityId;
    AuthorityEditRequestDTO request =
        AuthorityEditRequestDTO.builder().nameBackend("ITEM_UPDATED").build();
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
    assertEquals(MSG.AUTHORITY_NOT_FOUND_MSG + authorityId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_200_deleteAuthorityById_isDeletedFalse() throws Exception {
    String authorityId = "651762db-fff0-47ba-8c2b-3770f4c4a2fe";
    String url = BASE_URL + "/" + authorityId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("Deleted authority with id: " + authorityId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_400_deleteAuthorityById_isDeletedFalse() throws Exception {
    String authorityId = "invalid-uuid";
    String url = BASE_URL + "/" + authorityId;
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
  void shouldReturn_401_deleteAuthorityById_isDeletedFalse() throws Exception {
    String authorityId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + authorityId;
    MvcResult response =
        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(unauthorizedError(), responseBody);
  }

  @Test
  void shouldReturn_403_deleteAuthorityById_isDeletedFalse() throws Exception {
    String authorityId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + authorityId;
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
  void shouldReturn_404_deleteAuthorityById_isDeletedFalse() throws Exception {
    String authorityId = "4c7a6cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + authorityId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.AUTHORITY_NOT_FOUND_MSG + authorityId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_deleteAuthorityById_isDeletedTrue() throws Exception {
    String authorityId = "84ad8217-9bc4-4244-8d23-d0354ddb9100";
    String url = BASE_URL + "/" + authorityId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.AUTHORITY_NOT_FOUND_MSG + authorityId, responseBody.getMessage());
  }
}
