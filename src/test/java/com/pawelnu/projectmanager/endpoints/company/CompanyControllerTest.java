package com.pawelnu.projectmanager.endpoints.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Slf4j
class CompanyControllerTest {

  public static final String INVALID_UUID =
      "Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type"
          + " 'java.util.UUID'; Invalid UUID string: invalid-uuid";
  public static final String FULL_AUTH_IS_REQUIRED =
      "Full authentication is required to access this resource";
  public static final String COMPANY_NOT_FOUND_WITH_ID = "Company not found with id: ";
  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private String jwtTokenWithAuthorities;
  private String jwtTokenWithoutAuthorities;

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

  private RequestPostProcessor withJwt() {
    return request -> {
      request.addHeader("Authorization", "Bearer " + jwtTokenWithAuthorities);
      request.addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
      return request;
    };
  }

  private RequestPostProcessor withBadJwt() {
    return request -> {
      request.addHeader("Authorization", "Bearer " + jwtTokenWithoutAuthorities);
      request.addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
      return request;
    };
  }

  @BeforeEach
  void generateJwtToken() {
    if (jwtTokenWithAuthorities == null) {
      jwtTokenWithAuthorities = jwtUtils.generateTokenFromUsername("test");
    }
    if (jwtTokenWithoutAuthorities == null) {
      jwtTokenWithoutAuthorities = jwtUtils.generateTokenFromUsername("user_with_no_authorities");
    }
  }

  @Test
  void shouldReturn_200_getCompanyById() throws Exception {
    String companyId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    MvcResult response =
        mockMvc
            .perform(get("/" + Path.API_COMPANIES + "/" + companyId).with(withJwt()))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    CompanyDTO responseBody = objectMapper.readValue(contentAsString, CompanyDTO.class);
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("Hayes-Welch", responseBody.getName());
  }

  @Test
  void shouldReturn_400_getCompanyById() throws Exception {
    MvcResult response =
        mockMvc
            .perform(get("/" + Path.API_COMPANIES + "/invalid-uuid").with(withJwt()))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    Map<String, String> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(INVALID_UUID, responseBody.get("message"));
  }

  @Test
  void shouldReturn_401_getCompanyById() throws Exception {
    String companyId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    MvcResult response =
        mockMvc.perform(get("/" + Path.API_COMPANIES + "/" + companyId)).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(FULL_AUTH_IS_REQUIRED, responseBody.getMessage());
  }

  @Test
  void shouldReturn_403_getCompanyById() throws Exception {
    String companyId = "cf578fec-006b-4604-a5e8-5ad1b3ea2be5";
    MvcResult response =
        mockMvc
            .perform(get("/" + Path.API_COMPANIES + "/" + companyId).with(withBadJwt()))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_404_getCompanyById() throws Exception {
    String companyId = "cf578fec-006b-4604-a5e8-5ad1b2ea2be5";
    MvcResult response =
        mockMvc
            .perform(get("/" + Path.API_COMPANIES + "/" + companyId).with(withJwt()))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(COMPANY_NOT_FOUND_WITH_ID + companyId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_getCompanyById_isDeletedTrue() throws Exception {
    String companyId = "84198896-de25-4d17-b47d-11a0c80fc396";
    MvcResult response =
        mockMvc
            .perform(get("/" + Path.API_COMPANIES + "/" + companyId).with(withJwt()))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    NotFoundException responseBody =
        objectMapper.readValue(contentAsString, NotFoundException.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(COMPANY_NOT_FOUND_WITH_ID + companyId, responseBody.getMessage());
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
    mockMvc
        .perform(
            post("/" + Path.API_COMPANIES)
                .with(withJwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value(request.getName()))
        .andExpect(jsonPath("$.nip").value(request.getNip()))
        .andExpect(jsonPath("$.regon").value(request.getRegon()))
        .andExpect(jsonPath("$.status").value("Active"))
        .andExpect(jsonPath("$.website").value(request.getWebsite()));
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
    mockMvc
        .perform(
            post("/" + Path.API_COMPANIES)
                .with(withJwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors.website").value("URL must start with https://"))
        .andExpect(jsonPath("$.errors.nip").value("NIP must contain exactly 10 digits"))
        .andExpect(jsonPath("$.errors.regon").value("REGON must contain exactly 9 digits"))
        .andExpect(jsonPath("$.errors.name").value("Name must be 3-255 characters"))
        .andExpect(
            jsonPath("$.errors.root.serverError")
                .value("Some of the provided values are not valid. Please fix them and retry."));
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
            .perform(
                post("/" + Path.API_COMPANIES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(FULL_AUTH_IS_REQUIRED, responseBody.getMessage());
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
                post("/" + Path.API_COMPANIES)
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

  @NotNull
  private static ReactAdminError accessDeniedError() {
    return new ReactAdminError("Access denied");
  }

  @Test
  void shouldReturn_200_getCompanyList() throws Exception {
    MvcResult response = mockMvc.perform(get("/" + Path.API_COMPANIES).with(withJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<CompanySimpleDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-24/30", headerContentRange);
    assertEquals(25, responseBody.size());
  }

  @Test
  void shouldReturn_200_getCompanyList_withFilters() throws Exception {
    Map<String, String> filter = Map.of("name", "berg");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc
            .perform(get("/" + Path.API_COMPANIES).with(withJwt()).param("filter", filterStrig))
            .andReturn();
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
                get("/" + Path.API_COMPANIES)
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
        mockMvc
            .perform(get("/" + Path.API_COMPANIES).with(withJwt()).param("range", rangeString))
            .andReturn();
    int status = response.getResponse().getStatus();
    String headerContentRange = response.getResponse().getHeader("Content-Range");
    String contentAsString = response.getResponse().getContentAsString();
    List<CompanySimpleDTO> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.OK.value(), status);
    assertEquals("items 0-0/30", headerContentRange);
    assertEquals(1, responseBody.size());
    assertEquals("Abernathy LLC", responseBody.getFirst().getName());
  }

  @Test
  void shouldReturn_200_getCompanyList_emptyResult() throws Exception {
    Map<String, String> filter = Map.of("name", "llc", "nip", "placeholder");
    String filterStrig = objectMapper.writeValueAsString(filter);
    MvcResult response =
        mockMvc
            .perform(get("/" + Path.API_COMPANIES).with(withJwt()).param("filter", filterStrig))
            .andReturn();
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
    MvcResult response = mockMvc.perform(get("/" + Path.API_COMPANIES)).andReturn();
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
    MvcResult response =
        mockMvc.perform(get("/" + Path.API_COMPANIES).with(withBadJwt())).andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_200_editCompanyById() throws Exception {
    String companyId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
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
                put("/" + Path.API_COMPANIES + "/" + companyId)
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
                put("/" + Path.API_COMPANIES + "/" + companyId)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    Map<String, String> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(INVALID_UUID, responseBody.get("message"));
  }

  @Test
  void shouldReturn_401_editCompanyById() throws Exception {
    String companyId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
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
                put("/" + Path.API_COMPANIES + "/" + companyId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    Map<String, String> responseBody =
        objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(FULL_AUTH_IS_REQUIRED, responseBody.get("message"));
  }

  @Test
  void shouldReturn_403_editCompanyById() throws Exception {
    String companyId = "ac1da9e4-7e4b-42ab-b9a5-b87cc4f30c2c";
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
                put("/" + Path.API_COMPANIES + "/" + companyId)
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
  void shouldReturn_404_editCompanyById() throws Exception {
    String companyId = "ac1da9e4-7e4b-42ab-b7a5-b87cc4f30c2c";
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
                put("/" + Path.API_COMPANIES + "/" + companyId)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(COMPANY_NOT_FOUND_WITH_ID + companyId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_200_deleteCompanyById_isDeletedFalse() throws Exception {
    String companyId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    MvcResult response =
        mockMvc
            .perform(
                delete("/" + Path.API_COMPANIES + "/" + companyId)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON))
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
    MvcResult response =
        mockMvc
            .perform(
                delete("/" + Path.API_COMPANIES + "/" + companyId)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    SimpleResponse responseBody = objectMapper.readValue(contentAsString, SimpleResponse.class);
    assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    assertEquals(INVALID_UUID, responseBody.getMessage());
  }

  @Test
  void shouldReturn_401_deleteCompanyById_isDeletedFalse() throws Exception {
    String companyId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    MvcResult response =
        mockMvc
            .perform(
                delete("/" + Path.API_COMPANIES + "/" + companyId)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    assertEquals(FULL_AUTH_IS_REQUIRED, responseBody.getMessage());
  }

  @Test
  void shouldReturn_403_deleteCompanyById_isDeletedFalse() throws Exception {
    String companyId = "4c7a2cc5-1e03-4337-8901-93c0b46585af";
    MvcResult response =
        mockMvc
            .perform(
                delete("/" + Path.API_COMPANIES + "/" + companyId)
                    .with(withBadJwt())
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.FORBIDDEN.value(), status);
    assertEquals(accessDeniedError(), responseBody);
  }

  @Test
  void shouldReturn_404_deleteCompanyById_isDeletedFalse() throws Exception {
    String companyId = "4c7a6cc5-1e03-4337-8901-93c0b46585af";
    MvcResult response =
        mockMvc
            .perform(
                delete("/" + Path.API_COMPANIES + "/" + companyId)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
    int status = response.getResponse().getStatus();
    String contentAsString = response.getResponse().getContentAsString();
    ReactAdminError responseBody = objectMapper.readValue(contentAsString, ReactAdminError.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), status);
    assertEquals(COMPANY_NOT_FOUND_WITH_ID + companyId, responseBody.getMessage());
  }

  @Test
  void shouldReturn_404_deleteCompanyById_isDeletedTrue() throws Exception {
    String companyId = "84198896-de25-4d17-b47d-11a0c80fc396";
    MvcResult response =
        mockMvc
            .perform(
                delete("/" + Path.API_COMPANIES + "/" + companyId)
                    .with(withJwt())
                    .contentType(MediaType.APPLICATION_JSON))
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
