package com.pawelnu.projectmanager.endpoints.company;

import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
class CompanyControllerTest {

  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_COMPANIES;

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
  void shouldReturn_200_getCompanyById() throws Exception {
    String companyId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + companyId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    CompanyDTO responseBody = objectMapper.readValue(contentAsString, CompanyDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("Hayes-Welch", responseBody.getName());
  }

  @Test
  void shouldReturn_400_getCompanyById() throws Exception {
    String companyId = "/invalid-uuid";
    String url = BASE_URL + "/" + companyId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    Map<String, String> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(MSG.INVALID_UUID, responseBody.get("message"));
  }

  @Test
  void shouldReturn_401_getCompanyById() throws Exception {
    String companyId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + companyId;
    MvcResult response = mockMvc.perform(get(url)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(Utils.FULL_AUTH_IS_REQUIRED, responseBody.getMessage());
  }

  @Test
  void shouldReturn_403_getCompanyById() throws Exception {
    String companyId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    String url = BASE_URL + "/" + companyId;
    MvcResult response = mockMvc.perform(get(url).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_404_getCompanyById() throws Exception {
    String companyId = "cf578fec-006b-4604-a5e8-5ad1b2ea2be5";
    String url = BASE_URL + "/" + companyId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.COMPANY_NOT_FOUND + companyId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_getCompanyById_isDeletedTrue() throws Exception {
    String companyId = "84198896-de25-4d17-b47d-11a0c80fc396";
    String url = BASE_URL + "/" + companyId;
    MvcResult response = mockMvc.perform(get(url).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.COMPANY_NOT_FOUND + companyId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_201_createCompany() throws Exception {
    CompanyCreateRequestDTO request =
        CompanyCreateRequestDTO.builder()
            .name("Company test")
            .nip("1234567890")
            .regon("123456789")
            .website("https://company-test.com")
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
    CompanyDTO responseBody = objectMapper.readValue(contentAsString, CompanyDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertEquals(request.getName(), responseBody.getName());
    assertEquals(request.getNip(), responseBody.getNip());
    assertEquals(request.getRegon(), responseBody.getRegon());
    assertEquals("ACTIVE", responseBody.getStatus());
    assertEquals(request.getWebsite(), responseBody.getWebsite());
  }

  @Test
  void shouldReturn_400_createCompany() throws Exception {
    CompanyCreateRequestDTO request =
        CompanyCreateRequestDTO.builder()
            .name("Co")
            .nip("test")
            .regon("test")
            .website("http://company-test.com")
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
    assertEquals("URL must start with https://", responseBody.getErrors().get("website"));
    assertEquals("NIP must contain exactly 10 digits", responseBody.getErrors().get("nip"));
    assertEquals("REGON must contain exactly 9 digits", responseBody.getErrors().get("regon"));
    assertEquals("Name must be 3-255 characters", responseBody.getErrors().get("name"));
  }

  @Test
  void shouldReturn_401_createCompany() throws Exception {
    CompanyCreateRequestDTO request =
        CompanyCreateRequestDTO.builder()
            .name("Co")
            .nip("test")
            .regon("test")
            .website("http://company-test.com")
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
    assertEquals(Utils.FULL_AUTH_IS_REQUIRED, responseBody.getMessage());
  }

  @Test
  void shouldReturn_403_createCompany() throws Exception {
    CompanyCreateRequestDTO request =
        CompanyCreateRequestDTO.builder()
            .name("Company test")
            .nip("1234567890")
            .regon("123456789")
            .website("https://company-test.com")
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
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_200_getCompanyList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<CompanySimpleDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-24/29", headerContentRange);
    assertEquals(25, responseBody.size());
  }

  @Test
  void shouldReturn_200_getCompanyList_withFilters() throws Exception {
    Map<String, String> filter = Map.of("name", "berg");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<CompanySimpleDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-0/1", headerContentRange);
    assertEquals(1, responseBody.size());
    assertEquals("Padberg Inc", responseBody.getFirst().getName());
  }

  @Test
  void shouldReturn_200_getCompanyList_withFiltersAndSort() throws Exception {
    List<String> sort = List.of("name", "DESC");
    Map<String, String> filter = Map.of("name", "llc", "status", "active");
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
    List<CompanySimpleDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-1/2", headerContentRange);
    assertEquals(2, responseBody.size());
    assertEquals("Skiles LLC", responseBody.getFirst().getName());
    assertEquals("Schiller LLC", responseBody.get(1).getName());
  }

  @Test
  void shouldReturn_200_getCompanyList_withRange() throws Exception {
    List<String> range = List.of("0", "0");
    String rangeString = objectMapper.writeValueAsString(range);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("range", rangeString)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<CompanySimpleDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-0/29", headerContentRange);
    assertEquals(1, responseBody.size());
    assertEquals("Abernathy LLC", responseBody.getFirst().getName());
  }

  @Test
  void shouldReturn_200_getCompanyList_emptyResult() throws Exception {
    Map<String, String> filter = Map.of("name", "llc", "nip", "placeholder");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc.perform(get(BASE_URL).with(withJwt()).param("filter", filterStrig)).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<CompanySimpleDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0--1/0", headerContentRange);
    assertEquals(0, responseBody.size());
  }

  @Test
  void shouldReturn_401_getCompanyList() throws Exception {
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
  void shouldReturn_403_getCompanyList() throws Exception {
    MvcResult response = mockMvc.perform(get(BASE_URL).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(Utils.accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_200_editCompanyById() throws Exception {
    String companyId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
    String url = BASE_URL + "/" + companyId;
    CompanyEditRequestDTO request =
        CompanyEditRequestDTO.builder()
            .name("Updated company")
            .nip("9999999999")
            .regon("111111111")
            .website("https://updated-company.com")
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
    CompanyDTO responseBody = objectMapper.readValue(contentAsString, CompanyDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(UUID.fromString(companyId), responseBody.getId());
    assertEquals(request.getName(), responseBody.getName());
    assertEquals(request.getNip(), responseBody.getNip());
    assertEquals(request.getRegon(), responseBody.getRegon());
    assertEquals(request.getWebsite(), responseBody.getWebsite());
    assertEquals(2, responseBody.getAddresses().size());
  }

  @Test
  void shouldReturn_400_editCompanyById() throws Exception {
    String companyId = "invalid-uuid";
    String url = BASE_URL + "/" + companyId;
    CompanyEditRequestDTO request =
        CompanyEditRequestDTO.builder()
            .name("Updated company")
            .nip("9999999999")
            .regon("111111111")
            .website("https://updated-company.com")
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
    Map<String, String> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(MSG.INVALID_UUID, responseBody.get("message"));
  }

  @Test
  void shouldReturn_401_editCompanyById() throws Exception {
    String companyId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
    String url = BASE_URL + "/" + companyId;
    CompanyCreateRequestDTO request =
        CompanyCreateRequestDTO.builder()
            .name("Co")
            .nip("test")
            .regon("test")
            .website("http://company-test.com")
            .build();
    String requestBody = objectMapper.writeValueAsString(request);
    MvcResult response =
        mockMvc
            .perform(put(url).contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    Map<String, String> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(Utils.FULL_AUTH_IS_REQUIRED, responseBody.get("message"));
  }

  @Test
  void shouldReturn_403_editCompanyById() throws Exception {
    String companyId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
    String url = BASE_URL + "/" + companyId;
    CompanyCreateRequestDTO request =
        CompanyCreateRequestDTO.builder()
            .name("Company test")
            .nip("1234567890")
            .regon("123456789")
            .website("https://company-test.com")
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
  void shouldReturn_404_editCompanyById() throws Exception {
    String companyId = "ac1da9e4-7e4b-42ab-b7a5-b87cc4f30c2c";
    String url = BASE_URL + "/" + companyId;
    CompanyEditRequestDTO request =
        CompanyEditRequestDTO.builder()
            .name("Updated company")
            .nip("9999999999")
            .regon("111111111")
            .website("https://updated-company.com")
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
    assertEquals(MSG.COMPANY_NOT_FOUND + companyId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_200_deleteCompanyById_isDeletedFalse() throws Exception {
    String companyId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + companyId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals(
        "Deleted company with id: 4c7a2cc5-1e03-4337-8901-93c0b46585af", responseBody.getMessage());
  }

  @Test
  void shouldReturn_400_deleteCompanyById_isDeletedFalse() throws Exception {
    String companyId = "invalid-uuid";
    String url = BASE_URL + "/" + companyId;
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
  void shouldReturn_401_deleteCompanyById_isDeletedFalse() throws Exception {
    String companyId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + companyId;
    MvcResult response =
        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(Utils.FULL_AUTH_IS_REQUIRED, responseBody.getMessage());
  }

  @Test
  void shouldReturn_403_deleteCompanyById_isDeletedFalse() throws Exception {
    String companyId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + companyId;
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
  void shouldReturn_404_deleteCompanyById_isDeletedFalse() throws Exception {
    String companyId = "4c7a6cc5-1e03-4337-8901-93c0b46585af";
    String url = BASE_URL + "/" + companyId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(MSG.COMPANY_NOT_FOUND + companyId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_deleteCompanyById_isDeletedTrue() throws Exception {
    String companyId = "84198896-de25-4d17-b47d-11a0c80fc396";
    String url = BASE_URL + "/" + companyId;
    MvcResult response =
        mockMvc
            .perform(delete(url).with(withJwt()).contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(
        "Company not found with id: 84198896-de25-4d17-b47d-11a0c80fc396",
        responseBody.getMessage());
  }
}
