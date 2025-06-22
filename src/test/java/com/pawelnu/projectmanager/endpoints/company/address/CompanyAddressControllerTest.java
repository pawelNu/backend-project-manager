package com.pawelnu.projectmanager.endpoints.company.address;

import static com.pawelnu.projectmanager.utils.Utils.FULL_AUTH_IS_REQUIRED;
import static com.pawelnu.projectmanager.utils.Utils.accessDeniedError;
import static com.pawelnu.projectmanager.utils.Utils.withBadJwt;
import static com.pawelnu.projectmanager.utils.Utils.withJwt;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.exception.model.ReactAdminBadRequestError;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.Utils;
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
class CompanyAddressControllerTest {
  @Autowired private JwtUtils jwtUtils;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private static final String BASE_URL = "/" + Path.API_COMPANY_ADDRESSES;

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
  void shouldReturn_201_createCategory() throws Exception {
    CompanyAddressCreateRequestDTO request =
        CompanyAddressCreateRequestDTO.builder()
            .companyId(UUID.fromString("489da321-243c-44fa-a14f-509ebfbad683"))
            .street("test street")
            .streetNumber("1")
            .city("test city")
            .zipCode("11-111")
            .country("Test Country")
            .phoneNumber("111-111-111")
            .emailAddress("test.test@email.test")
            .addressType("K")
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
    CompanyAddressDTO responseBody = objectMapper.readValue(contentAsString, CompanyAddressDTO.class);
    assertEquals(HttpStatus.CREATED.value(), status);
    assertEquals("Pouros Group", responseBody.getCompanyName());
    assertEquals(request.getStreet(), responseBody.getStreet());
    assertEquals(request.getStreetNumber(), responseBody.getStreetNumber());
    assertEquals(request.getCity(), responseBody.getCity());
    assertEquals(request.getZipCode(), responseBody.getZipCode());
    assertEquals(request.getCountry(), responseBody.getCountry());
    assertEquals(request.getPhoneNumber(), responseBody.getPhoneNumber());
    assertEquals(request.getEmailAddress(), responseBody.getEmailAddress());
    assertEquals(request.getAddressType(), responseBody.getAddressType());
  }

  @Test
  void shouldReturn_400_createCategory() throws Exception {
    CompanyAddressCreateRequestDTO request =
        CompanyAddressCreateRequestDTO.builder()
            .companyId(UUID.fromString("489da321-243c-44fa-a14f-509ebfbad683"))
            .street("test street")
            .streetNumber("1")
            .city("test city")
            .zipCode("11-111")
            .country("Test Country")
            .phoneNumber("111-111-111")
            .emailAddress("test.test@email.test")
            .addressType("K")
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
    assertEquals("Name should has 5-255 characters", responseBody.getErrors().get("name"));
  }

  @Test
  void shouldReturn_401_createCategory() throws Exception {
    CompanyAddressCreateRequestDTO request =
        CompanyAddressCreateRequestDTO.builder()
            .companyId(UUID.fromString("489da321-243c-44fa-a14f-509ebfbad683"))
            .street("test street")
            .streetNumber("1")
            .city("test city")
            .zipCode("11-111")
            .country("Test Country")
            .phoneNumber("111-111-111")
            .emailAddress("test.test@email.test")
            .addressType("K")
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
  void shouldReturn_403_createCategory() throws Exception {
    CompanyAddressCreateRequestDTO request =
        CompanyAddressCreateRequestDTO.builder()
            .companyId(UUID.fromString("489da321-243c-44fa-a14f-509ebfbad683"))
            .street("test street")
            .streetNumber("1")
            .city("test city")
            .zipCode("11-111")
            .country("Test Country")
            .phoneNumber("111-111-111")
            .emailAddress("test.test@email.test")
            .addressType("K")
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

  // TODO Test getList() {}

  // TODO Test getById() {}

  // TODO Test editById() {}

  // TODO Test deleteById() {}
}
