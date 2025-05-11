package com.pawelnu.projectmanager.endpoints.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.utils.Path;
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

  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private CompanyRepository companyRepository;
  @Autowired private CompanyService companyService;
  @Autowired private CompanyQueryRepository companyQueryRepository;
  @Autowired private ObjectMapper objectMapper;
  private String jwtTokenWithAuthorities;
  private String jwtTokenWithoutAuthorities;
  private UUID companyId = UUID.fromString("cf578fec-006b-4604-a5e8-5ad1b3ea2be5");

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
    mockMvc
        .perform(get("/" + Path.API_COMPANIES + "/" + companyId).with(withJwt()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value("Hayes-Welch"));
  }

  @Test
  void shouldReturn_400_getCompanyById() throws Exception {
    mockMvc
        .perform(get("/" + Path.API_COMPANIES + "/invalid-uuid").with(withJwt()))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message")
                .value(
                    "Method parameter 'id': Failed to convert value of type 'java.lang.String' to"
                        + " required type 'java.util.UUID'; Invalid UUID string: invalid-uuid"));
  }

  @Test
  void shouldReturn_401_getCompanyById() throws Exception {
    mockMvc
        .perform(get("/" + Path.API_COMPANIES + "/" + companyId))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));
  }

  @Test
  void shouldReturn_403_getCompanyById() throws Exception {
    mockMvc
        .perform(get("/" + Path.API_COMPANIES + "/" + companyId).with(withBadJwt()))
        .andExpect(status().isForbidden())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Access denied"));
  }

  @Test
  void shouldReturn_404_getCompanyById() throws Exception {
    mockMvc
        .perform(
            get("/" + Path.API_COMPANIES + "/cf578fec-006b-4604-a5e8-5ad1b2ea2be5").with(withJwt()))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message")
                .value("Company not found with id: cf578fec-006b-4604-a5e8-5ad1b2ea2be5"));
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
    mockMvc
        .perform(
            post("/" + Path.API_COMPANIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));
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
    mockMvc
        .perform(
            post("/" + Path.API_COMPANIES)
                .with(withBadJwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isForbidden())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Access denied"));
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
  void shouldReturn_400_getCompanyList() throws Exception {
    //  TODO tests for getList()
    mockMvc
        .perform(get("/" + Path.API_COMPANIES + "/invalid-uuid").with(withJwt()))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message")
                .value(
                    "Method parameter 'id': Failed to convert value of type 'java.lang.String' to"
                        + " required type 'java.util.UUID'; Invalid UUID string: invalid-uuid"));
  }

  @Test
  void shouldReturn_401_getCompanyList() throws Exception {
    //  TODO tests for getList()
    mockMvc
        .perform(get("/" + Path.API_COMPANIES + "/" + companyId))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));
  }

  @Test
  void shouldReturn_403_getCompanyList() throws Exception {
    //  TODO tests for getList()
    mockMvc
        .perform(get("/" + Path.API_COMPANIES + "/" + companyId).with(withBadJwt()))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Access denied"));
  }

  @Test
  void shouldReturn_404_getCompanyList() throws Exception {
    //  TODO tests for getList()
    mockMvc
        .perform(
            get("/" + Path.API_COMPANIES + "/cf578fec-006b-4604-a5e8-5ad1b2ea2be5").with(withJwt()))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message")
                .value("Company not found with id: cf578fec-006b-4604-a5e8-5ad1b2ea2be5"));
  }

  @Test
  void getAllCompanies() {}

  @Test
  void editById() {}

  @Test
  void deleteById() {}

  @Test
  void filterCompanies() {}

  @Test
  void getList() {}
}
